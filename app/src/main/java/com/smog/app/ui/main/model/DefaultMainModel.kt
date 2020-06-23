package com.smog.app.ui.main.model

import com.smog.app.network.SmogApi
import com.smog.app.network.dto.MeasureStation
import com.smog.app.network.dto.SensorData
import io.reactivex.Observable
import io.reactivex.Single
import kotlin.math.pow
import kotlin.math.sqrt

class DefaultMainModel(
    private val smogApi: SmogApi
) : MainModel {

    private val allStations: MutableList<MeasureStation> = mutableListOf()

    override fun getNearestStation(
        latitude: Double,
        longitude: Double,
        adminArea: String
    ): Single<MeasureStation> {
        return smogApi.findAllStations()
            .flatMap { stations ->
                val filteredList = stations.filter {
                    it.city.commune.provinceName.equals(adminArea, ignoreCase = true)
                }

                allStations.clear()
                if (filteredList.isEmpty()) {
                    allStations.addAll(stations)
                } else {
                    allStations.addAll(filteredList)
                }

                findNearestStation(latitude, longitude, allStations)
            }
    }

    private fun findNearestStation(
        latitude: Double,
        longitude: Double,
        list: List<MeasureStation>
    ): Single<MeasureStation> {
        var nearestStation: MeasureStation? = null
        var lastNearestDistance = 0.0

        list.forEach {
            val xSquare = (it.gegrLat - latitude).pow(2.0)
            val ySquare = (it.gegrLon - longitude).pow(2.0)

            val distance = sqrt(xSquare + ySquare)
            if (nearestStation == null || distance < lastNearestDistance) {
                lastNearestDistance = distance
                nearestStation = it
            }
        }

        return Single.just(nearestStation)
    }

    override fun getSensorsData(stationId: Int): Single<List<SensorData>> {
        return smogApi.getStationSensors(stationId)
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