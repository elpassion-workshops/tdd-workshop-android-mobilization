@file:Suppress("IllegalIdentifier")

package com.elpassion.mobilization.tddworkshop.login

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test

class LoginControllerTest {

    @Test
    fun `Call api on login`() {
        val api = mock<Login.Api>()
        LoginController(api).login()
        verify(api, times(1)).login()
    }
}

interface Login {
    interface Api {
        fun login()
    }

}

class LoginController(private val api: Login.Api) {
    fun login() {
        api.login()
    }
}