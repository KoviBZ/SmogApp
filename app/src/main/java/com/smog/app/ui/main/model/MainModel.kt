package com.smog.app.ui.main.model

import com.smog.app.network.dto.MeasureStation
import com.smog.app.network.dto.SensorData
import io.reactivex.Single

interface MainModel {

    fun getNearestStation(latitude: Double, longitude: Double, adminArea: String): Single<MeasureStation>

    fun getStationData(stationId: Int): Single<List<SensorData>>
}