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
        verify(api, never()).login(eq(""), any())
    }

    @Test
    fun `Not call api if password is empty`(){
        login(password = "")
        verify(api, never()).login("", "password")
    }

    @Test
    fun `Call api with provided email and password`() {
        val email = "email@wp.pl"
        val password = "password"
        login(email, password)
        verify(api).login(email, password)
    }

    private fun login(email: String = "email@wp.pl", password: String="password") {
        LoginController(api).login(email, password)
    }
}

interface Login {
    interface Api {
        fun login(login: String, password: String)
    }
}

class LoginController(private val api: Login.Api) {
    fun login(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            api.login(email, password)
        }
    }
}