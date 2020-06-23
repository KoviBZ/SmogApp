package com.smog.app.ui.citylist.model

import io.reactivex.Single

interface CityListModel {

    fun findAllCities(): Single<List<String>>
}