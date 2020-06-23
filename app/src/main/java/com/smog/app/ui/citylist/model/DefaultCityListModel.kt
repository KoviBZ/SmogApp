package com.smog.app.ui.citylist.model

import com.smog.app.dto.CitySection
import com.smog.app.network.SmogApi
import com.smog.app.network.dto.MeasureStation
import com.smog.app.network.dto.SensorData
import io.reactivex.Observable
import io.reactivex.Single

class DefaultCityListModel(
    private val smogApi: SmogApi
) : CityListModel {

    override fun findAllCities(): Single<List<CitySection>> {
        return smogApi.findAllStations().map { response ->
            val nameSet = HashSet<String>()
            val idSet = HashSet<Int>()
            val finalList = ArrayList<CitySection>()

            response.sortedBy {
                it.city.name
            }

            response.forEach { station ->
                val cityName = station.city.name
                if(!nameSet.contains(cityName)) {
                    if(nameSet.isNotEmpty()) {
                        val idList = arrayListOf<Int>()
                        idList.addAll(idSet)
                        finalList.add(CitySection(cityName, idList))
                        idSet.clear()
                    }
                    nameSet.add(station.city.name)
                }
                idSet.add(station.id)
            }

            finalList
        }
    }
}