package com.smog.app.network.dto

import com.google.gson.annotations.SerializedName
import com.smog.app.network.dto.City
import java.io.Serializable

data class MeasureStation(
    @SerializedName("id") val id: Int,
    @SerializedName("stationName") val stationName: String,
    @SerializedName("gegrLat") val gegrLat: Double,
    @SerializedName("gegrLon") val gegrLon: Double,
    @SerializedName("city") val city: City,
    @SerializedName("addressStreet") val addressStreet: String?
): Serializable



