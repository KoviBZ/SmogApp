package com.smog.app.ui.main.view

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.*
import android.os.Bundle
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.smog.app.R
import com.smog.app.network.dto.MeasureStation
import com.smog.app.network.dto.SensorData
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == Constants.GPS_REQUEST_CODE) {
            obtainLocalization()
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
                showErrorView(PermissionError)
            }
        }
    }

    override fun onLocationChanged(location: Location) {
        val geoCoder = Geocoder(this, Locale.getDefault())
        val lat = location.latitude
        val lon = location.longitude
        val address: List<Address> = geoCoder.getFromLocation(lat, lon, 1)

        val districtName = address[0].adminArea

        viewModel.retrieveNearestStation(lat, lon, districtName)
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
            is FindAllError -> {
                content.visibility = View.GONE

                messageRes = R.string.location_error_message
                action = View.OnClickListener { getCoordinates() }
            }
            is SensorDataError -> {
                messageRes = R.string.sensor_data_error_message
                action = View.OnClickListener { viewModel.retrieveSensorData(error.id) }
            }
        }

        initErrorView(messageRes, action)
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

    private fun getCoordinates() {
        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            try {
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10f, this)
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        } else {
            AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage(R.string.gps_dialog)
                .setNeutralButton(android.R.string.ok
                ) { dialog, _ ->
                    startActivityForResult(Intent(ACTION_LOCATION_SOURCE_SETTINGS), Constants.GPS_REQUEST_CODE)
                    dialog.dismiss()
                }.create()
                .show()
        }
    }

    private fun onStationSuccess(measureStation: MeasureStation) {
        city.setValue(measureStation.city.name)
        measureStation.addressStreet?.let { street.setValue(it) }

        viewModel.retrieveSensorData(measureStation.id)
    }

    private fun onSensorDataSuccess(sensorList: List<SensorData>) {
        smog_rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        smog_rv.adapter = SmogAdapter(this, sensorList)
    }

    private fun setupObservers() {
        viewModel.getStationLiveData().observe(this, initObserver { onStationSuccess(it) })

        viewModel.getSensorLiveData().observe(this, initObserver { onSensorDataSuccess(it) })
    }
}