package com.smog.app.ui.citydetails.di

import com.smog.app.network.SmogApi
import com.smog.app.network.di.NetworkModule
import com.smog.app.network.scheduler.BaseSchedulerProvider
import com.smog.app.ui.citydetails.model.CityDetailsModel
import com.smog.app.ui.citydetails.model.DefaultCityDetailsModel
import com.smog.app.ui.citydetails.viewmodel.CityDetailsViewModel
import com.smog.app.ui.citylist.model.CityListModel
import com.smog.app.ui.citylist.viewmodel.CityListViewModel
import dagger.Module
import dagger.Provides

@Module(includes = [NetworkModule::class])
class CityDetailsModule {

    @Provides
    fun provideCityDetailsViewModel(
        model: CityDetailsModel,
        schedulerProvider: BaseSchedulerProvider
    ): CityDetailsViewModel = CityDetailsViewModel(model, schedulerProvider)

    @Provides
    fun provideCityDetailsModel(
        smogApi: SmogApi
    ): CityDetailsModel = DefaultCityDetailsModel(smogApi)
}