package com.smog.app.ui.main.model

import com.smog.app.dto.DetailedSensorData
import com.smog.app.network.SmogApi
import com.smog.app.network.dto.MeasureStation
import com.smog.app.network.dto.SensorData
import com.smog.app.utils.SensorDataError
import com.smog.app.utils.SensorIdError
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
        cityName: String
    ): Single<DetailedSensorData> {
        return smogApi.findAllStations()
            .flatMap { stations ->
                val filteredList = stations.filter {
                    it.city.name == cityName
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

    override fun findNextStation(emptyId: Int): Single<DetailedSensorData> {
        val iterator = allStations.iterator()
        var lat: Double = 0.0
        var lon: Double = 0.0

        while (iterator.hasNext()) {
            val currentItem = iterator.next()
            if (currentItem.id == emptyId) {
                lat = currentItem.gegrLat
                lon = currentItem.gegrLon
                iterator.remove()
            }
        }

        return findNearestStation(lat, lon, allStations)
    }

    private fun findNearestStation(
        latitude: Double,
        longitude: Double,
        list: List<MeasureStation>
    ): Single<DetailedSensorData> {
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

        nearestStation?.let { station ->
            return smogApi.getSensorData(station.id)
                .map { sensorData ->
                    val sensorValues = sensorData.values.filter {
                        it.value > 0
                    }

                    if (sensorValues.isEmpty()) {
                        throw SensorDataError(station.id)
                    }

                    DetailedSensorData(
                        station,
                        sensorData.key,
                        sensorValues[0]
                    )
                }
        }

        throw SensorIdError
    }
}