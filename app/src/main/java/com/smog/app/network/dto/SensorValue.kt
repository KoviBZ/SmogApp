package com.smog.app.network.dto

import com.google.gson.annotations.SerializedName

data class SensorValue(
    @SerializedName("date") val date: String,
    @SerializedName("value") val value: Double
)