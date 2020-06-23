package com.smog.app.ui.citydetails.model

import com.smog.app.network.SmogApi
import com.smog.app.network.dto.SensorData
import io.reactivex.Observable
import io.reactivex.Single

class DefaultCityDetailsModel(
    private val smogApi: SmogApi
): CityDetailsModel {

    override fun getStationData(id: Int): Single<List<SensorData>> {
        return smogApi.getStationSensors(id)
            .flatMap { stations ->
                Observable.fromIterable(stations)
                    .flatMapSingle {
                        smogApi.getSensorData(it.id)
                    }.filter { sensorData ->
                        sensorData.values.isNotEmpty() && sensorData.values[0].value > 0.0
                    }.toList()
            }
    }
}