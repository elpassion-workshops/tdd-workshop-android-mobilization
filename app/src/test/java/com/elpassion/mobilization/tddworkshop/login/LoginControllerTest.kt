@file:Suppress("IllegalIdentifier")

package com.elpassion.mobilization.tddworkshop.login

import com.nhaarman.mockito_kotlin.any
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
        verify(api).login(any(), any())
    }

    @Test
    fun `Not call api if email is empty`() {
        val emptyLogin = ""
        login(email = emptyLogin)
        verify(api, never()).login(any(), any())
    }

    private fun login(email: String = "email@wp.pl", password: String = "passwd") {
        LoginController(api, view).login(email, password)
    }

    @Test
    fun `Login with password and email`() {
        val email = "email@email.com"
        val password = "passwd"
        login(email, password)
        verify(api).login(email, password)
    }

    @Test
    fun `Controller should show loader when login API call will start`() {
        login()
        verify(view).showLoader()
    }

    @Test
    fun `Show error if email or password is empty`() {
        val emptyPassword = ""
        login(password = emptyPassword)
        verify(view).showError()
    }
}

interface Login {
    interface Api {
        fun login(email: String, password: String)
    }

    interface View {
        fun showLoader()
        fun showError()
    }
}

class LoginController(private val api: Login.Api, private val view: Login.View) {
    fun login(email: String, password: String) {
        view.showLoader()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            api.login(email, password)
        } else {
            view.showError()
        }
    }
}