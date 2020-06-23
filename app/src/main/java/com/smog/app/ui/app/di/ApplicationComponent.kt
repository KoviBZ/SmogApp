package com.smog.app.ui.app.di

import com.smog.app.ui.citydetails.di.CityDetailsComponent
import com.smog.app.ui.citydetails.di.CityDetailsModule
import com.smog.app.ui.citylist.di.CityListComponent
import com.smog.app.ui.citylist.di.CityListModule
import com.smog.app.ui.main.di.MainComponent
import com.smog.app.ui.main.di.MainModule
import dagger.Component

@Component(
    modules = [
        ApplicationModule::class
    ]
)
interface ApplicationComponent {

    fun plusMainComponent(mainModule: MainModule): MainComponent

    fun plusCityListComponent(cityListModule: CityListModule): CityListComponent

    fun plusCityDetailsComponent(cityDetailsModule: CityDetailsModule): CityDetailsComponent
}