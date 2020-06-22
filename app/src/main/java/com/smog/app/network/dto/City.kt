package com.smog.app.network.dto

import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("commune") val commune: Commune
)