package com.smog.app.network.dto

import com.google.gson.annotations.SerializedName

data class Commune(
    @SerializedName("communeName") val communeName: String,
    @SerializedName("districtName") val districtName: String,
    @SerializedName("provinceName") val provinceName: String
)