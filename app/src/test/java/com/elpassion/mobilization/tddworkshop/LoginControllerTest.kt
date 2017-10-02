package com.elpassion.mobilization.tddworkshop

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Assert
import org.junit.Test

class LoginControllerTest {

    private val api = mock<Login.Api>()

    @Test
    fun shouldInitLoginController() {
        Assert.assertNotNull(LoginController(api))
    }

    @Test
    fun shouldCallApiOnLogin() {
        LoginController(api).login()
        verify(api).login()
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
