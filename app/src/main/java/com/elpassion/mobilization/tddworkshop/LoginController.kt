package com.elpassion.mobilization.tddworkshop

import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.TestScheduler

class LoginController(private val api: Login.Api,
                      private val view: Login.View,
                      private val subscribeOnScheduler: TestScheduler,
                      private val observeOnScheduler: TestScheduler,
                      private val authRepository: Login.AuthTokenRepository) {
    private var disposable: Disposable? = null

    fun login(email: String, password: String) {
        if (email.isNotBlank() && password.isNotBlank()) {
            disposable = api.login(email, password)
                    .subscribeOn(subscribeOnScheduler)
                    .observeOn(observeOnScheduler)
                    .doOnSubscribe { view.showLoader() }
                    .doFinally { view.hideLoader() }
                    .doOnSuccess { authRepository.save(it) }
                    .subscribe({
                        view.showNextScreen()
                    }, {
                        view.showApiError()
                    })
        } else if (email.isEmpty()) {
            view.showEmptyEmailError()
        } else if (password.isEmpty()) {
            view.showEmptyPasswordError()
        }
    }

    fun onDestroy() {
        disposable?.dispose()
    }
}