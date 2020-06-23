package com.smog.app.ui.citydetails.viewmodel

import androidx.lifecycle.MutableLiveData
import com.smog.app.dto.DetailedSensorData
import com.smog.app.network.Resource
import com.smog.app.network.dto.SensorData
import com.smog.app.network.scheduler.BaseSchedulerProvider
import com.smog.app.ui.citydetails.model.CityDetailsModel
import com.smog.app.ui.common.viewmodel.BaseViewModel
import com.smog.app.ui.main.model.MainModel
import com.smog.app.utils.AppError
import com.smog.app.utils.SensorIdError

class CityDetailsViewModel(
    private val model: CityDetailsModel,
    schedulerProvider: BaseSchedulerProvider
): BaseViewModel(schedulerProvider) {

    private val sensorLiveData = MutableLiveData<Resource<List<SensorData>>>()

    fun getSensorLiveData() = sensorLiveData

    fun retrieveSensorsForCity(idList: List<Int>) {
        sensorLiveData.postValue(Resource.loading())
        val disposable = model.getSensorsForCity(idList)
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