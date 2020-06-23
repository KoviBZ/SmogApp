package com.smog.app.ui.citydetails.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.smog.app.R
import com.smog.app.network.dto.MeasureStation
import com.smog.app.network.dto.SensorData
import com.smog.app.ui.app.SmogApplication
import com.smog.app.ui.citydetails.di.CityDetailsModule
import com.smog.app.ui.citydetails.viewmodel.CityDetailsViewModel
import com.smog.app.ui.common.view.BaseActivity
import com.smog.app.ui.main.view.SmogAdapter
import com.smog.app.utils.AppError
import com.smog.app.utils.Constants
import com.smog.app.utils.SensorDataError
import kotlinx.android.synthetic.main.activity_city_list.*
import javax.inject.Inject

class CityDetailsActivity : BaseActivity() {

    var stationId: Int = -1

    @Inject
    lateinit var viewModel: CityDetailsViewModel

    companion object {
        fun getIntent(context: Context, stationId: Int): Intent {
            val intent = Intent(context, CityDetailsActivity::class.java)
            intent.putExtra(Constants.SENSOR_ID_BUNDLE, stationId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_list)

        errorView = findViewById(R.id.error_view)
        progressBar = findViewById(R.id.progress_bar)

        intent?.let {
            stationId = it.getIntExtra(Constants.SENSOR_ID_BUNDLE, -1)
        }

        val appComponent = SmogApplication.getApplicationComponent()
        val cityDetailsComponent = appComponent.plusCityDetailsComponent(CityDetailsModule())
        cityDetailsComponent.inject(this)

        setupObservers()
        viewModel.retrieveSensorData(stationId)
    }

    override fun showErrorView(error: AppError) {
        val messageRes: Int
        val action: View.OnClickListener

        when (error) {
            is SensorDataError -> {
                messageRes = R.string.sensor_data_error_message
                action = View.OnClickListener { viewModel.retrieveSensorData(stationId) }
            }
            else -> throw IllegalArgumentException("AppError not supported")
        }

        initErrorView(messageRes, action)
    }

    private fun initUi(sensorData: List<SensorData>) {
        city_list_rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        city_list_rv.adapter = SmogAdapter(this, sensorData)
    }

    private fun setupObservers() {
        viewModel.getSensorLiveData().observe(this, initObserver { initUi(it) })
    }
}