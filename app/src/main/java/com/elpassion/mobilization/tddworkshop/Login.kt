package com.elpassion.mobilization.tddworkshop

import io.reactivex.Single

interface Login {
    interface Api {
        fun login(email: String, password: String): Single<String>
    }

    interface View {
        fun showEmptyEmailError()
        fun showEmptyPasswordError()
        fun showNextScreen()
        fun showApiError()
        fun showLoader()
        fun hideLoader()
    }

    interface AuthTokenRepository {
        fun save(token: String)
    }
}