package com.smog.app.ui.citylist.view

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.smog.app.R
import com.smog.app.network.dto.MeasureStation
import com.smog.app.ui.app.SmogApplication
import com.smog.app.ui.citydetails.view.CityDetailsActivity
import com.smog.app.ui.citylist.di.CityListModule
import com.smog.app.ui.citylist.viewmodel.CityListViewModel
import com.smog.app.ui.common.view.BaseActivity
import com.smog.app.utils.AppError
import com.smog.app.utils.FindAllError
import com.smog.app.utils.OnItemClickListener
import kotlinx.android.synthetic.main.activity_city_list.*
import javax.inject.Inject

class CityListActivity: BaseActivity(), OnItemClickListener<MeasureStation> {

    @Inject
    lateinit var viewModel: CityListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_list)

        errorView = findViewById(R.id.error_view)
        progressBar = findViewById(R.id.progress_bar)

        val appComponent = SmogApplication.getApplicationComponent()
        val cityListComponent = appComponent.plusCityListComponent(CityListModule())
        cityListComponent.inject(this)

        setupObservers()
        viewModel.findAllCitiesDetailed()
    }

    override fun showErrorView(error: AppError) {
        val messageRes: Int
        val action: View.OnClickListener

        when (error) {
            is FindAllError -> {
                messageRes = R.string.find_all_error_message
                action = View.OnClickListener { viewModel.findAllCitiesDetailed() }
            }
            else -> throw IllegalArgumentException("AppError not supported")
        }

        initErrorView(messageRes, action)
    }

    private fun onAllCitiesSuccess(list: List<MeasureStation>) {
        city_list_rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        city_list_rv.adapter = CityAdapter(this, list, this)
    }

    private fun setupObservers() {
        viewModel.getCitiesLiveData().observe(this, initObserver { onAllCitiesSuccess(it) })
    }

    override fun onItemClick(item: MeasureStation) {
        startActivity(CityDetailsActivity.getIntent(this, item.id))
    }
}