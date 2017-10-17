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
        login("")
        verify(api, never()).login()
    }

    private fun login(email: String = "email@wp.pl") {
        LoginController(api).onLogin(email)
    }
}

interface Login {
    interface Api {
        fun login()
    }
}

class LoginController(private val api: Login.Api) {

    fun onLogin(email: String) {
        if (email.isNotEmpty()) {
            api.login()
        }
    }
}
