package com.elpassion.mobilization.tddworkshop

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test

class LoginControllerTest {

    private val api = mock<Login.Api>()
    private val controller = LoginController(api)

    @Test
    fun shouldCallApiOnLogin() {
        controller.login("email")
        verify(api).login(any())
    }

    @Test
    fun shouldCallApiWithProvidedEmail() {
        val specificEmail = "specificEmail"
        controller.login(specificEmail)
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
