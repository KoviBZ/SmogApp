package com.smog.app.ui.main.model

import com.smog.app.network.SmogApi
import com.smog.app.network.dto.MeasureStation
import com.smog.app.network.dto.SensorData
import io.reactivex.Single
import kotlin.math.pow
import kotlin.math.sqrt

class DefaultMainModel(
    private val smogApi: SmogApi
) : MainModel {

    override fun getNearestStationId(
        latitude: Double,
        longitude: Double,
        cityName: String
    ): Single<SensorData> {
        return smogApi.findAllStations()
            .map { stations ->
                stations.filter {
                    it.city.name == cityName
                }
            }.flatMap {
                findNearestStation(latitude, longitude, it)
            }
    }

    private fun findNearestStation(
        latitude: Double,
        longitude: Double,
        list: List<MeasureStation>
    ): Single<SensorData> {
        var nearestStationId = 0
        var lastNearestDistance = 0.0

        list.forEach {
            val xSquare = (it.gegrLat - latitude).pow(2.0)
            val ySquare = (it.gegrLon - longitude).pow(2.0)

            val distance = sqrt(xSquare + ySquare)
            if(nearestStationId == 0 || distance < lastNearestDistance) {
                lastNearestDistance = distance
                nearestStationId = it.id
            }
        }

        return smogApi.getSensorData(nearestStationId)
    }
}