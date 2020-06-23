package com.smog.app.ui.citylist.model

import com.smog.app.network.SmogApi
import com.smog.app.network.dto.MeasureStation
import com.smog.app.network.dto.SensorData
import io.reactivex.Observable
import io.reactivex.Single

class DefaultCityListModel(
    private val smogApi: SmogApi
) : CityListModel {

    override fun findAllCities(): Single<List<String>> {
        return smogApi.findAllStations().map { response ->
            val set = HashSet<String>()
            val finalList = ArrayList<String>()

            response.forEach {
                set.add(it.city.name)
            }

            set.forEach {
                finalList.add(it)
            }

            finalList
        }
    }
}