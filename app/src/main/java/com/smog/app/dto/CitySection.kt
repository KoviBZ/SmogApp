package com.smog.app.dto

import java.io.Serializable

data class CitySection(
    val name: String,
    val idList: ArrayList<Int>
): Serializable