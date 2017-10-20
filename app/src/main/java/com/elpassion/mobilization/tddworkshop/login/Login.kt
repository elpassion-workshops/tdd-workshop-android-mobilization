package com.elpassion.mobilization.tddworkshop.login

import io.reactivex.Single

interface Login {
    interface Api {
        fun login(email: String, password: String): Single<String>
    }

    interface View {
        fun showProgressView()
        fun setEmailErrorMessage()
        fun setPasswordErrorMessage()
        fun showDashboardView()
        fun showLoginFailedMessage()
        fun hideProgressView()
    }

    interface Repo {
        fun persistUserData(email: String, token: String)
    }
}