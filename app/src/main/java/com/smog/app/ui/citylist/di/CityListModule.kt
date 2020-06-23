package com.smog.app.ui.citylist.di

import com.smog.app.network.SmogApi
import com.smog.app.network.di.NetworkModule
import com.smog.app.network.scheduler.BaseSchedulerProvider
import com.smog.app.ui.citylist.model.CityListModel
import com.smog.app.ui.citylist.model.DefaultCityListModel
import com.smog.app.ui.citylist.viewmodel.CityListViewModel
import dagger.Module
import dagger.Provides

@Module(includes = [NetworkModule::class])
class CityListModule {

    @Provides
    fun provideCityListViewModel(
        model: CityListModel,
        schedulerProvider: BaseSchedulerProvider
    ): CityListViewModel = CityListViewModel(model, schedulerProvider)

    @Provides
    fun provideCityListModel(
        smogApi: SmogApi
    ): CityListModel = DefaultCityListModel(smogApi)
}