@file:Suppress("IllegalIdentifier")

package com.elpassion.mobilization.tddworkshop.login

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test

class LoginControllerTest {

    @Test
    fun `Call api on login`() {
        val api = mock<Login.Api>()
        LoginController(api).login("emial@wp.pl")
        verify(api, times(1)).login()
    }

    @Test
    fun `Not call api if email is empty`() {
        val api = mock<Login.Api>()
        LoginController(api).login(email = "")
        verify(api, never()).login()
    }
}

interface Login {
    interface Api {
        fun login()
    }
}

class LoginController(private val api: Login.Api) {
    fun login(email: String) {
        if (email.isNotEmpty()) {
            api.login()
        }
    }
}