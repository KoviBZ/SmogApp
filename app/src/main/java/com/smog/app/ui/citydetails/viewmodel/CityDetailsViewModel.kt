package com.smog.app.ui.citydetails.viewmodel

import androidx.lifecycle.MutableLiveData
import com.smog.app.network.Resource
import com.smog.app.network.dto.SensorData
import com.smog.app.network.scheduler.BaseSchedulerProvider
import com.smog.app.ui.citydetails.model.CityDetailsModel
import com.smog.app.ui.common.viewmodel.BaseViewModel
import com.smog.app.utils.SensorDataError

class CityDetailsViewModel(
    private val model: CityDetailsModel,
    schedulerProvider: BaseSchedulerProvider
): BaseViewModel(schedulerProvider) {

    private val sensorLiveData = MutableLiveData<Resource<List<SensorData>>>()

    fun getSensorLiveData() = sensorLiveData

    fun retrieveSensorData(id: Int) {
        sensorLiveData.postValue(Resource.loading())
        val disposable = model.getStationData(id)
            .applyDefaultIOSchedulers()
            .subscribe({ response -> sensorLiveData.postValue(Resource.success(response)) },
                {
                    sensorLiveData.postValue(Resource.error(SensorDataError(id)))
                }
            )

        subscriptions.add(disposable)
    }
}