package com.smog.app.ui.main.di

import com.smog.app.ui.main.view.MainActivity
import dagger.Subcomponent

@Subcomponent(modules = [MainModule::class])
interface MainComponent {

    fun inject(mainActivity: MainActivity)
}