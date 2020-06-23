package com.smog.app.ui.citylist.viewmodel

import androidx.lifecycle.MutableLiveData
import com.smog.app.dto.CitySection
import com.smog.app.network.Resource
import com.smog.app.network.scheduler.BaseSchedulerProvider
import com.smog.app.ui.citylist.model.CityListModel
import com.smog.app.ui.common.viewmodel.BaseViewModel
import com.smog.app.utils.FindAllError

class CityListViewModel(
    private val model: CityListModel,
    schedulerProvider: BaseSchedulerProvider
): BaseViewModel(schedulerProvider) {

    private val citiesLiveData = MutableLiveData<Resource<List<CitySection>>>()

    fun getCitiesLiveData() = citiesLiveData

    fun findAllCitiesDetailed() {
        citiesLiveData.postValue(Resource.loading())
        val disposable = model.findAllCities()
            .applyDefaultIOSchedulers()
            .subscribe(
                { response -> citiesLiveData.postValue(Resource.success(response)) },
                {
                    citiesLiveData.postValue(Resource.error(FindAllError))
                }
            )

        subscriptions.add(disposable)
    }
}