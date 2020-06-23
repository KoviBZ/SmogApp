package com.smog.app.ui.main.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.*
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import com.smog.app.R
import com.smog.app.dto.DetailedSensorData
import com.smog.app.ui.app.SmogApplication
import com.smog.app.ui.citylist.view.CityListActivity
import com.smog.app.ui.common.view.BaseActivity
import com.smog.app.ui.main.di.MainModule
import com.smog.app.ui.main.viewmodel.MainViewModel
import com.smog.app.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import javax.inject.Inject

class MainActivity : BaseActivity(), LocationListener {

    @Inject
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        errorView = findViewById(R.id.error_view)
        progressBar = findViewById(R.id.progress_bar)

        see_all_button.setOnClickListener {
            startActivity(Intent(this, CityListActivity::class.java))
        }

        val appComponent = SmogApplication.getApplicationComponent()
        val mainComponent = appComponent.plusMainComponent(MainModule())
        mainComponent.inject(this)

        setupObservers()
        obtainLocalization()
    }

    private fun obtainLocalization() {
        val localizationPermission = Manifest.permission.ACCESS_FINE_LOCATION

        if (ActivityCompat.checkSelfPermission(
                this, localizationPermission
            ) != PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(localizationPermission),
                Constants.LOCALIZATION_REQUEST_CODE
            )
        } else {
            getCoordinates()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == Constants.LOCALIZATION_REQUEST_CODE) {
            if (grantResults.size == 1 && grantResults[0] == PERMISSION_GRANTED) {
                getCoordinates()
            } else {
                // Failure Stuff
            }
        }
    }

    override fun onLocationChanged(location: Location) {
        val geoCoder = Geocoder(this, Locale.getDefault())
        val lat = location.latitude
        val lon = location.longitude
        val address: List<Address> = geoCoder.getFromLocation(lat, lon, 1)

        val cityName = address[0].locality

        viewModel.retrieveSensorId(lat, lon, cityName)
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        //intentionally left empty to prevent crash
    }

    override fun showErrorView(error: AppError) {
        val messageRes: Int
        val action: View.OnClickListener

        when (error) {
            is PermissionError -> {
                content.visibility = View.GONE

                messageRes = R.string.permission_error_message
                action = View.OnClickListener { obtainLocalization() }
            }
            is LocalizationError -> {
                content.visibility = View.GONE

                messageRes = R.string.location_error_message
                action = View.OnClickListener { getCoordinates() }
            }
            is SensorIdError -> {
                messageRes = R.string.sensor_id_error_message
                action = View.OnClickListener { getCoordinates() }
            }
            is SensorDataError -> {
                messageRes = R.string.sensor_data_error_message
                action = View.OnClickListener { viewModel.findNextStation(error.wrongId) }
            }
            else -> throw IllegalArgumentException("AppError not supported")
        }

        initErrorView(messageRes, action)
    }

    private fun FAKEobtainData() {
        viewModel.retrieveSensorId(50.088900, 19.934860, "Kraków")
    }

    private fun getCoordinates() {
        try {
            val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10f, this)

        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun initUi(sensorData: DetailedSensorData) {
        city.setValue(sensorData.measureStation.city.name)
        sensorData.measureStation.addressStreet?.let { street.setValue(it) }
        date.setValue(sensorData.latestValue.date)

        smog_component.text =
            String.format("[h]Gęstość %s wynosi:", sensorData.key)
        smog_density.text = "${sensorData.latestValue.value}"
    }

    private fun setupObservers() {
        viewModel.getSensorLiveData().observe(this, initObserver { initUi(it) })
    }
}