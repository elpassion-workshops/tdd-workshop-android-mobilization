@file:Suppress("IllegalIdentifier")

package com.elpassion.mobilization.tddworkshop.login

import com.nhaarman.mockito_kotlin.*
import org.junit.Test

class LoginControllerTest {

    private val api = mock<Login.Api>()

    @Test
    fun `Call api on login`() {
        login()
        verify(api).login(any(), any())
    }

    @Test
    fun `Not call api if email is empty`() {
        login(email = "")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun `Not call api if password is empty`() {
        login(password = "")
        verify(api, never()).login(any(),  any())
    }

    @Test
    fun `Call api if email and password are provided`() {
        login()
        verify(api).login(any(),  any())
    }

    private fun login(email: String = "email@wp.pl", password: String = "testPassword") {
        LoginController(api).login(email, password)
    }
}

interface Login {
    interface Api {
        fun login(email: String, password: String)
    }
}

class LoginController(private val api: Login.Api) {

    fun login(email: String, password : String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            api.login(email, password)
        }
    }
}