@file:Suppress("IllegalIdentifier")

package com.elpassion.mobilization.tddworkshop.login

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
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

    private fun login(email: String = "email@wp.pl", password: String = "passwd") {
        LoginController(api).login(email, password)
    }

    @Test
    fun `Login with password and email`() {
        login()
        verify(api).login(any(), any())
    }
}

interface Login {
    interface Api {
        fun login(email: String, password: String)
    }
}

class LoginController(private val api: Login.Api) {
    fun login(email: String, password: String) {
        if (email.isNotEmpty()) {
            api.login(email, password)
        }
    }
}