package com.smog.app.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import com.smog.app.dto.DetailedSensorData
import com.smog.app.network.Resource
import com.smog.app.network.dto.SensorData
import com.smog.app.network.scheduler.BaseSchedulerProvider
import com.smog.app.ui.common.viewmodel.BaseViewModel
import com.smog.app.ui.main.model.MainModel
import com.smog.app.utils.AppError
import com.smog.app.utils.SensorIdError

class MainViewModel(
    private val mainModel: MainModel,
    schedulerProvider: BaseSchedulerProvider
): BaseViewModel(schedulerProvider) {

    private val sensorLiveData = MutableLiveData<Resource<DetailedSensorData>>()

    fun getSensorLiveData() = sensorLiveData

    fun retrieveSensorId(latitude: Double, longitude: Double, cityName: String) {
        sensorLiveData.postValue(Resource.loading())
        val disposable = mainModel.getNearestStation(latitude, longitude, cityName)
            .applyDefaultIOSchedulers()
            .subscribe({ response ->
                sensorLiveData.postValue(Resource.success(response))
            }, {
                if(it is AppError) {
                    sensorLiveData.postValue(Resource.error(it))
                } else {
                    sensorLiveData.postValue(Resource.error(SensorIdError))
                }
            })

        subscriptions.add(disposable)
    }

    fun findNextStation(emptyId: Int) {
        sensorLiveData.postValue(Resource.loading())
        val disposable = mainModel.findNextStation(emptyId)
            .applyDefaultIOSchedulers()
            .subscribe({ response ->
                sensorLiveData.postValue(Resource.success(response))
            }, {
                if(it is AppError) {
                    sensorLiveData.postValue(Resource.error(it))
                } else {
                    sensorLiveData.postValue(Resource.error(SensorIdError))
                }
            })

        subscriptions.add(disposable)
    }
}