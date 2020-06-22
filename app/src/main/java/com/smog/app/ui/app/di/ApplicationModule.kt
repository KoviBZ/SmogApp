package com.smog.app.ui.app.di

import com.smog.app.network.scheduler.BaseSchedulerProvider
import com.smog.app.network.scheduler.SchedulerProvider
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule {

    @Provides
    fun provideSchedulerProvider(): BaseSchedulerProvider = SchedulerProvider()
}