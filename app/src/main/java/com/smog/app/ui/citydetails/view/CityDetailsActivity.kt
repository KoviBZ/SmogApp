package com.smog.app.ui.citydetails.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.smog.app.R
import com.smog.app.dto.CitySection
import com.smog.app.dto.DetailedSensorData
import com.smog.app.network.dto.SensorData
import com.smog.app.ui.app.SmogApplication
import com.smog.app.ui.citydetails.viewmodel.CityDetailsViewModel
import com.smog.app.ui.citylist.view.CityListActivity
import com.smog.app.ui.common.view.BaseActivity
import com.smog.app.ui.main.viewmodel.MainViewModel
import com.smog.app.utils.AppError
import com.smog.app.utils.SensorDataError
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class CityDetailsActivity : BaseActivity() {

    lateinit var citySection: CitySection

    @Inject
    lateinit var viewModel: CityDetailsViewModel

    companion object {
        fun getIntent(context: Context, cityName: CitySection): Intent {
            val intent = Intent(context, CityDetailsActivity::class.java)
            intent.putExtra("CITY_NAME_BUNDLE", cityName)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        errorView = findViewById(R.id.error_view)
        progressBar = findViewById(R.id.progress_bar)

        intent?.let {
            citySection = it.getSerializableExtra("CITY_NAME_BUNDLE") as CitySection
        }

        see_all_button.setOnClickListener {
            startActivity(Intent(this, CityListActivity::class.java))
        }

        val appComponent = SmogApplication.getApplicationComponent()
//        val mainComponent = appComponent.plusMainComponent(MainModule())
//        mainComponent.inject(this)

        setupObservers()
        viewModel.retrieveSensorsForCity(citySection.idList)
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

    //TODO
    private fun initUi(sensorData: List<SensorData>) {

    }

    private fun setupObservers() {
        viewModel.getSensorLiveData().observe(this, initObserver { initUi(it) })
    }
}