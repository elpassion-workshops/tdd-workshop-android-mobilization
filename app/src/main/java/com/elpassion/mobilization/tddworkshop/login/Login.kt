package com.elpassion.mobilization.tddworkshop.login

import io.reactivex.Single

interface Login {
    interface Api {
        fun login(email: String, password: String): Single<User>
    }

    interface View {
        fun showLoader()
        fun openNextScreen()
        fun showLoginCallError()
        fun showEmptyEmailError()
        fun showEmptyPasswordError()
        fun hideLoader()
    }

    interface Repository {
        fun save(user: User)
    }

    data class User(val id: Int)
}