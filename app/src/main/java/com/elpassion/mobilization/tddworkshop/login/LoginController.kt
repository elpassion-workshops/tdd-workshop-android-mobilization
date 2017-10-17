package com.elpassion.mobilization.tddworkshop.login

import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable

class LoginController(private val api: Login.Api, private val view: Login.View, private val repository: Login.Repository, private val ioScheduler: Scheduler, private val uiScheduler: Scheduler) {

    private var disposable: Disposable? = null

    fun onLogin(email: String, password: String) {
        if (email.isEmpty()) {
            view.showEmptyEmailError()
        }
        if (password.isEmpty()) {
            view.showEmptyPasswordError()
        }
        if (email.isNotEmpty() && password.isNotEmpty()) {
            disposable = api.login(email, password)
                    .doOnSubscribe { view.showLoader() }
                    .subscribeOn(ioScheduler)
                    .observeOn(uiScheduler)
                    .doFinally { view.hideLoader() }
                    .doOnSuccess { repository.save(it) }
                    .subscribe(
                            { view.openNextScreen() },
                            { view.showLoginCallError() })
        }
    }

    fun onDestroy() {
        disposable?.dispose()
    }
}