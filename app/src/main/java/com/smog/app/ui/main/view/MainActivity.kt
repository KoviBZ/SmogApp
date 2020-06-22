package com.smog.app.ui.main.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.*
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.smog.app.R
import com.smog.app.network.Resource
import com.smog.app.network.Status
import com.smog.app.ui.app.SmogApplication
import com.smog.app.ui.main.di.MainModule
import com.smog.app.ui.main.viewmodel.MainViewModel
import com.smog.app.utils.*
import com.smog.app.view.ErrorView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.Locale
import javax.inject.Inject

class MainActivity : AppCompatActivity(), LocationListener {

    lateinit var errorView: ErrorView
    lateinit var progressBar: ErrorView

    @Inject
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        errorView = findViewById(R.id.error_view)
        progressBar = findViewById(R.id.progress_bar)

        val appComponent = SmogApplication.getApplicationComponent()
        val mainComponent = appComponent.plusMainComponent(MainModule())
        mainComponent.inject(this)

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

    private fun getCoordinates() {
        try {
            val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10f, this)

        } catch (e: SecurityException) {
            e.printStackTrace()
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

    private fun showErrorView(error: AppError) {
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
                action = View.OnClickListener { getCoordinates() }
            }
        }

        initErrorView(messageRes, action)
    }

    private fun initErrorView(@StringRes message: Int, onClickListener: View.OnClickListener) {
        errorView.apply {
            enableRetryButton()
            setErrorMessage(getString(message))
            setRetryAction(onClickListener)
            visibility = View.VISIBLE
        }
    }

    private fun <T> initObserver(success: (T) -> Unit): Observer<Resource<T>> {
        return Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    hideErrorView()
                    hideProgress()
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    hideProgress()
                }
            }

            response.data?.let { success(it) }
            response.error?.let { showErrorView(it) }
        }
    }

    private fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    private fun hideErrorView() {
        errorView.visibility = View.GONE
    }
}