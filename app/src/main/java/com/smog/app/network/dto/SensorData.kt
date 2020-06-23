package com.smog.app.network.dto

import com.google.gson.annotations.SerializedName

data class SensorData(
    @SerializedName("key") val key: String,
    @SerializedName("values") val values: List<SensorValue>
)