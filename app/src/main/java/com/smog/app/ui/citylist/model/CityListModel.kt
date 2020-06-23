package com.smog.app.ui.citylist.model

import com.smog.app.dto.CitySection
import io.reactivex.Single

interface CityListModel {

    fun findAllCities(): Single<List<CitySection>>
}