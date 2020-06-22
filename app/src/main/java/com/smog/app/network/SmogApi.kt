package com.smog.app.network

import com.smog.app.network.dto.MeasureStation
import com.smog.app.network.dto.SensorData
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface SmogApi {

    @GET("station/findAll")
    fun findAllStations(): Single<List<MeasureStation>>

    @GET("data/getData/{sensorId}")
    fun getSensorData(@Query("sensorId") sensorId: Int): Single<SensorData>
}