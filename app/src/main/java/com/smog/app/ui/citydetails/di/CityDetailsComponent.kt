package com.smog.app.ui.citydetails.di

import com.smog.app.ui.citydetails.view.CityDetailsActivity
import com.smog.app.ui.citylist.view.CityListActivity
import dagger.Subcomponent

@Subcomponent(modules = [CityDetailsModule::class])
interface CityDetailsComponent {

    fun inject(cityDetailsActivity: CityDetailsActivity)
}