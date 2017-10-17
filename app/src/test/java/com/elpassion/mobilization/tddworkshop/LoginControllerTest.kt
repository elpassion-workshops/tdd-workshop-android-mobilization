@file:Suppress("IllegalIdentifier")

package com.elpassion.mobilization.tddworkshop

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
    fun `Not call api with empty email`() {
        login(email = "")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun `Not call api with empty password`() {
        login(password = "")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun `Call api with provided login and password`() {
        login("otheremail@wp.pl", "other password")
        verify(api).login("otheremail@wp.pl", "other password")
    }

    @Test
    fun `Show loader when api call starts`() {
        login()
        verify(view).showLoader()
    }

    @Test
    fun `Not show loader when email is empty`() {
        login(email = "")
        verify(view, never()).showLoader()
    }

    private fun login(email: String = "email@wp.pl", password: String = "password") {
        LoginController(api, view).onLogin(email, password)
    }
}

interface Login {
    interface Api {
        fun login(email: String, password: String)
    }

    interface View {
        fun showLoader()
    }
}

class LoginController(private val api: Login.Api, private val view: Login.View) {

    fun onLogin(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            view.showLoader()
            api.login(email, password)
        }
    }
}
