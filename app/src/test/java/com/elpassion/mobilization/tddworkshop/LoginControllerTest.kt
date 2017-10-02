package com.elpassion.mobilization.tddworkshop

import com.nhaarman.mockito_kotlin.*
import org.junit.Test

class LoginControllerTest {

    private val api = mock<Login.Api>()
    private val controller = LoginController(api)

    @Test
    fun shouldCallApiOnLogin() {
        login()
        verify(api).login(any(), any())
    }

    @Test
    fun shouldCallApiWithProvidedEmail() {
        val specificEmail = "specificEmail"
        login(email = specificEmail)
        verify(api).login(eq(specificEmail), any())
    }

    @Test
    fun shouldNotCallApiWhenEmailIsEmpty() {
        login(email = "")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldCallApiWithProvidedPassword() {
        val specificPassword = "specificPassword"
        login(password = specificPassword)
        verify(api).login(any(), eq(specificPassword))
    }

    private fun login(email: String = "email", password: String = "password") {
        controller.login(email, password)
    }
}

interface Login {
    interface Api {
        fun login(email: String, password: String)
    }
}

class LoginController(private val api: Login.Api) {
    fun login(email: String, password: String) {
        if (email.isNotBlank()) {
            api.login(email, password)
        }
    }
}
