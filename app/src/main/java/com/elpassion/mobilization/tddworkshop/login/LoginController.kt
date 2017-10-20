package com.elpassion.mobilization.tddworkshop.login

import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable

class LoginController(private val api: Login.Api, private val view: Login.View, private val repo: Login.Repo,
                      private val uiScheduler: Scheduler, private val ioScheduler: Scheduler) {

    private val compositeDisposable by lazy { CompositeDisposable() }

    fun login(email: String, password: String) {
        when {
            email.isEmpty() -> view.setEmailErrorMessage()
            password.isEmpty() -> view.setPasswordErrorMessage()
            else -> {
                compositeDisposable.add(api.login(email, password).
                        subscribeOn(ioScheduler).
                        doOnSubscribe { view.showProgressView() }.
                        doOnSuccess { repo.persistUserData(email, it) }.
                        doFinally { view.hideProgressView() }.
                        observeOn(uiScheduler).
                        subscribe({
                            view.showDashboardView()
                        }, {
                            view.showLoginFailedMessage()
                        }))
            }
        }
    }

    fun onDestroy() {
        compositeDisposable.clear()
    }
}