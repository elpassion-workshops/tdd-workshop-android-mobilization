@file:Suppress("IllegalIdentifier")

package com.elpassion.mobilization.tddworkshop

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test

class LoginControllerTest {

    private val api = mock<Login.Api>()

    @Test
    fun `Call api on login`() {
        login()
        verify(api).login()
    }

    @Test
    fun `Not call api with empty email`() {
        login(email = "")
        verify(api, never()).login()
    }

    @Test
    fun `Not call api with empty password`() {
        login(password = "")
        verify(api, never()).login()
    }

    private fun login(email: String = "email@wp.pl", password: String = "password") {
        LoginController(api).onLogin(email, password)
    }
}

interface Login {
    interface Api {
        fun login()
    }
}

class LoginController(private val api: Login.Api) {

    fun onLogin(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            api.login()
        }
    }
}
