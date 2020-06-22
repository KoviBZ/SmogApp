package com.smog.app.ui.app

import android.app.Application
import com.smog.app.ui.app.di.ApplicationComponent
import com.smog.app.ui.app.di.ApplicationModule
import com.smog.app.ui.app.di.DaggerApplicationComponent

class SmogApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        applicationComponent = buildComponent()
    }

    private fun buildComponent(): ApplicationComponent =
        DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule())
            .build()

    companion object {

        private lateinit var applicationComponent: ApplicationComponent

        @JvmStatic
        fun getApplicationComponent() = applicationComponent
    }
}