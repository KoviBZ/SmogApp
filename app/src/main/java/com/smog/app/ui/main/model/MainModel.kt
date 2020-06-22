package com.smog.app.ui.main.model

import com.smog.app.network.dto.MeasureStation
import io.reactivex.Single

interface MainModel {

    fun getNearestStationId(latitude: Double, longitude: Double, cityName: String): Single<Int>
}