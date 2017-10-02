package com.elpassion.mobilization.tddworkshop

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test

class LoginControllerTest {

    private val api = mock<Login.Api>()

    @Test
    fun shouldCallApiOnLogin() {
        LoginController(api).login("email")
        verify(api).login(any())
    }

    @Test
    fun shouldCallApiWithProvidedEmail() {
        val specificEmail = "specificEmail"
        LoginController(api).login(specificEmail)
        verify(api).login(specificEmail)
    }
}

interface Login {
    interface Api {
        fun login(email: String)
    }
}

class LoginController(private val api: Login.Api) {
    fun login(email: String) {
        api.login(email)
    }
}
