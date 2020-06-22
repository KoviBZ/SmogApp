package com.smog.app.network.dto

import com.google.gson.annotations.SerializedName

data class SensorValue(
    @SerializedName("data") val name: String,
    @SerializedName("value") val value: Double
)