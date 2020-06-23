package com.smog.app.dto

import com.smog.app.network.dto.MeasureStation
import com.smog.app.network.dto.SensorValue

data class DetailedSensorData(
    val measureStation: MeasureStation,
    val key: String,
    val latestValue: SensorValue
)