package com.smog.app.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import com.smog.app.network.Resource
import com.smog.app.network.dto.MeasureStation
import com.smog.app.network.dto.SensorData
import com.smog.app.network.scheduler.BaseSchedulerProvider
import com.smog.app.ui.common.viewmodel.BaseViewModel
import com.smog.app.ui.main.model.MainModel
import com.smog.app.utils.FindAllError
import com.smog.app.utils.SensorDataError

class MainViewModel(
    private val mainModel: MainModel,
    schedulerProvider: BaseSchedulerProvider
) : BaseViewModel(schedulerProvider) {

    private val stationLiveData = MutableLiveData<Resource<MeasureStation>>()

    private val sensorLiveData = MutableLiveData<Resource<List<SensorData>>>()

    fun getStationLiveData() = stationLiveData

    fun getSensorLiveData() = sensorLiveData

    fun retrieveNearestStation(latitude: Double, longitude: Double, districtName: String) {
        stationLiveData.postValue(Resource.loading())
        val disposable = mainModel.getNearestStation(latitude, longitude, districtName)
            .applyDefaultIOSchedulers()
            .subscribe({ response ->
                stationLiveData.postValue(Resource.success(response))
            }, { stationLiveData.postValue(Resource.error(FindAllError)) }
            )

        subscriptions.add(disposable)
    }

    fun retrieveSensorData(id: Int) {
        sensorLiveData.postValue(Resource.loading())
        val disposable = mainModel.getStationData(id)
            .applyDefaultIOSchedulers()
            .subscribe({ response -> sensorLiveData.postValue(Resource.success(response)) },
                {
                    sensorLiveData.postValue(Resource.error(SensorDataError(id)))
                }
            )

        subscriptions.add(disposable)
    }
}