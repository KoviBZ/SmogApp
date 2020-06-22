package com.smog.app.ui.app.di

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
}