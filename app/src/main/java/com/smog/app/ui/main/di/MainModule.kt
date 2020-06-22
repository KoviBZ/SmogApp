package com.smog.app.ui.main.di

import com.smog.app.network.SmogApi
import com.smog.app.network.di.NetworkModule
import com.smog.app.network.scheduler.BaseSchedulerProvider
import com.smog.app.ui.main.model.DefaultMainModel
import com.smog.app.ui.main.model.MainModel
import com.smog.app.ui.main.viewmodel.MainViewModel
import dagger.Module
import dagger.Provides

@Module(includes = [NetworkModule::class])
class MainModule {

    @Provides
    fun provideMainViewModel(
        model: MainModel,
        schedulerProvider: BaseSchedulerProvider
    ): MainViewModel = MainViewModel(model, schedulerProvider)

    @Provides
    fun provideMainModel(
        smogApi: SmogApi
    ): MainModel = DefaultMainModel(smogApi)
}