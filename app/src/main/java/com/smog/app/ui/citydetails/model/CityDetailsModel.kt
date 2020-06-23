package com.smog.app.ui.citydetails.model

import com.smog.app.network.dto.SensorData
import io.reactivex.Single

interface CityDetailsModel {

    fun getSensorsForCity(idList: List<Int>): Single<List<SensorData>>
}