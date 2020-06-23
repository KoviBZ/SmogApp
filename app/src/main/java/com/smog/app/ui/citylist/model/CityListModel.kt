package com.smog.app.ui.citylist.model

import com.smog.app.network.dto.MeasureStation
import io.reactivex.Single

interface CityListModel {

    fun findAllCities(): Single<List<MeasureStation>>
}