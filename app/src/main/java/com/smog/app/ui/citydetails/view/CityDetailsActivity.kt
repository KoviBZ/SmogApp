package com.smog.app.ui.citydetails.view

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

class CityDetailsActivity : BaseActivity() {

    companion object {
        fun getIntent(context: Context, cityName: String): Intent {
            val intent = Intent(context, CityDetailsActivity::class.java)
            intent.putExtra("CITY_NAME_BUNDLE", cityName)
            return intent
        }
    }

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

//        val appComponent = SmogApplication.getApplicationComponent()
//        val mainComponent = appComponent.plusMainComponent(MainModule())
//        mainComponent.inject(this)

        setupObservers()
    }

    override fun showErrorView(error: AppError) {
        val messageRes: Int
        val action: View.OnClickListener

        when (error) {
            is SensorDataError -> {
                messageRes = R.string.sensor_data_error_message
                action = View.OnClickListener { viewModel.findNextStation(error.wrongId) }
            }
            else -> throw IllegalArgumentException("AppError not supported")
        }

        initErrorView(messageRes, action)
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