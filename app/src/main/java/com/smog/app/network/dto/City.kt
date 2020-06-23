package com.smog.app.network.dto

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class City(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("commune") val commune: Commune
): Serializable