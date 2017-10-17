@file:Suppress("IllegalIdentifier")

package com.elpassion.mobilization.tddworkshop

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test

class LoginControllerTest {

    private val api = mock<Login.Api>()

    @Test
    fun `Call api on login`() {
        LoginController(api).onLogin()
        verify(api).login()
    }
}

interface Login {
    interface Api {
        fun login()
    }
}

class LoginController(private val api: Login.Api) {

    fun onLogin() {
        api.login()
    }
}
