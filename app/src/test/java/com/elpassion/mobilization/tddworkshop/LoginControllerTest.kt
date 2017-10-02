package com.elpassion.mobilization.tddworkshop

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test

class LoginControllerTest {

    private val api = mock<Login.Api>()
    private val controller = LoginController(api)

    @Test
    fun shouldCallApiOnLogin() {
        login()
        verify(api).login(any())
    }

    @Test
    fun shouldCallApiWithProvidedEmail() {
        val specificEmail = "specificEmail"
        login(specificEmail)
        verify(api).login(specificEmail)
    }

    @Test
    fun shouldNotCallApiWhenEmailIsEmpty() {
        login("")
        verify(api, never()).login(any())
    }

    private fun login(email: String = "email") {
        controller.login(email)
    }
}

interface Login {
    interface Api {
        fun login(email: String)
    }
}

class LoginController(private val api: Login.Api) {
    fun login(email: String) {
        if (email.isNotBlank()) {
            api.login(email)
        }
    }
}
