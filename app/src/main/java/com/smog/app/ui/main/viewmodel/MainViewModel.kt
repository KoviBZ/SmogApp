package com.smog.app.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import com.smog.app.network.Resource
import com.smog.app.network.scheduler.BaseSchedulerProvider
import com.smog.app.ui.common.viewmodel.BaseViewModel
import com.smog.app.ui.main.model.MainModel
import com.smog.app.utils.SensorIdError

class MainViewModel(
    private val mainModel: MainModel,
    schedulerProvider: BaseSchedulerProvider
): BaseViewModel(schedulerProvider) {

    private val sensorIdLiveData = MutableLiveData<Resource<Int>>()

    fun getSensorIdLiveData() = sensorIdLiveData

    fun retrieveSensorId(latitude: Double, longitude: Double, cityName: String) {
        sensorIdLiveData.postValue(Resource.loading())
        val disposable = mainModel.getNearestStationId(latitude, longitude, cityName)
            .applyDefaultIOSchedulers()
            .subscribe({ response ->
                sensorIdLiveData.postValue(Resource.success(response))
            }, { error ->
                sensorIdLiveData.postValue(Resource.error(SensorIdError))
            })

        subscriptions.add(disposable)
    }
}