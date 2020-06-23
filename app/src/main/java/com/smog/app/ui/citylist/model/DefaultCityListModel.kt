package com.smog.app.ui.citylist.model

import com.smog.app.network.SmogApi
import com.smog.app.network.dto.MeasureStation
import io.reactivex.Single

class DefaultCityListModel(
    private val smogApi: SmogApi
) : CityListModel {

    override fun findAllCities(): Single<List<MeasureStation>> {
        return smogApi.findAllStations().map { response ->
            response.sortedBy { it.city.name }
        }
    }
}