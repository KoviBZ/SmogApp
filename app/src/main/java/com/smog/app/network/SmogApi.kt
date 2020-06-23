package com.smog.app.network

import com.smog.app.network.dto.MeasureStation
import com.smog.app.network.dto.SensorData
import com.smog.app.network.dto.Station
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SmogApi {

    @GET("station/findAll")
    fun findAllStations(): Single<List<MeasureStation>>

    @GET("station/sensors/{stationId}")
    fun getStationSensors(@Path("stationId") sensorId: Int): Single<List<Station>>

    @GET("data/getData/{sensorId}")
    fun getSensorData(@Path("sensorId") sensorId: Int): Single<SensorData>
}