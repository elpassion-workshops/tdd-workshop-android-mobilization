@file:Suppress("IllegalIdentifier")

package com.elpassion.mobilization.tddworkshop.login

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test

class LoginControllerTest {

    private val api = mock<Login.Api>()
    private val view = mock<Login.View>()

    @Test
    fun `Call api on login`() {
        login()
        verify(api).login("email@wp.pl", "password")
    }

    @Test
    fun `Not call api if email is empty`() {
        login(email = "")
        verify(api, never()).login("email@wp.pl", "password")
    }

    @Test
    fun `Not call api if empty password`() {
        login(password = "")
        verify(api, never()).login("email@wp.pl", "password")
    }

    @Test
    fun `Not call api if email and password are empty`() {
        login(email = "", password = "")
        verify(api, never()).login("email@wp.pl", "password")
    }

    @Test
    fun `Show progress view on login action`() {
        login()
        verify(view).showProgressView()
    }

    @Test
    fun `Check email and password passed to api`() {
        login()
        verify(api).login("email@wp.pl", "password")
    }

    @Test
    fun `Show error when email is incorrect`() {
        login(email = "")
        verify(view).setEmailErrorMessage()
    }

    @Test
    fun `Show error when password is incorrect`() {
        login(password = "")
        verify(view).setPasswordErrorMessage()
    }

    @Test
    fun `Show dashboard when login success`() {
        login()
        verify(view).showDashboardView()
    }

    private fun login(email: String = "email@wp.pl", password: String = "password") {
        LoginController(api, view).login(email, password)
    }


}

interface Login {
    interface Api {
        fun login(email: String, password: String)
    }

    interface View {
        fun showProgressView()
        fun setEmailErrorMessage()
        fun setPasswordErrorMessage()
        fun showDashboardView()
    }
}


class LoginController(private val api: Login.Api, private val view: Login.View) {
    fun login(email: String, password: String) {
        when {
            email.isEmpty() -> view.setEmailErrorMessage()
            password.isEmpty() -> view.setPasswordErrorMessage()
            else -> {
                view.showProgressView()
                api.login("email@wp.pl", "password")
                view.showDashboardView()
            }
        }
    }
}