package com.smog.app.ui.citylist.di

import com.smog.app.ui.citylist.view.CityListActivity
import dagger.Subcomponent

@Subcomponent(modules = [CityListModule::class])
interface CityListComponent {

    fun inject(cityListActivity: CityListActivity)
}