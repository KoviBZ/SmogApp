package com.smog.app.ui.main.model

import com.smog.app.dto.DetailedSensorData
import com.smog.app.network.dto.SensorData
import io.reactivex.Single

interface MainModel {

    fun getNearestStation(latitude: Double, longitude: Double, cityName: String): Single<DetailedSensorData>

    fun findNextStation(emptyId: Int): Single<DetailedSensorData>
}