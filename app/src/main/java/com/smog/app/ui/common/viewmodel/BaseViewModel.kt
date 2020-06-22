package com.smog.app.ui.common.viewmodel

import com.smog.app.network.scheduler.BaseSchedulerProvider
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel(val schedulerProvider: BaseSchedulerProvider) {

    protected val subscriptions: CompositeDisposable = CompositeDisposable()

    fun clearSubscriptions() {
        subscriptions.clear()
    }

    fun disposeSubscriptions() {
        subscriptions.dispose()
    }

    fun <RESPONSE> Single<RESPONSE>.applyDefaultIOSchedulers(): Single<RESPONSE> {
        return this
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
    }
}