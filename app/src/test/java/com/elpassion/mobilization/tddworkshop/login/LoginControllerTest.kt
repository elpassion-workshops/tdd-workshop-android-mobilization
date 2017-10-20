@file:Suppress("IllegalIdentifier")

package com.elpassion.mobilization.tddworkshop.login

import com.nhaarman.mockito_kotlin.*
import org.junit.Test

class LoginControllerTest {

    private val api = mock<Login.Api>()
    private val view = mock<Login.View>()

    @Test
    fun `Call api on login`() {
        login()
        verify(api).login(any(), any())
    }

    @Test
    fun `Not call api if email is empty`() {
        login(email = "")
        verify(api, never()).login(eq(""), any())
    }

    @Test
    fun `Not call api if password is empty`(){
        login(password = "")
        verify(api, never()).login(any(), eq(""))
    }

    @Test
    fun `Call api with provided email and password`() {
        val email = "email"
        val password = "password"

        login(email, password)

        verify(api).login(email, password)
    }

    @Test
    fun `Show loader before call login`() {
        login()
        verify(view).showLoader()
    }

    @Test
    fun `Hide loader after calling login`(){
        login()
        verify(view).hideLoader()
    }

    private fun login(email: String = "email", password: String="password") {
        LoginController(api,view).login(email, password)
    }
}

interface Login {
    interface Api {
        fun login(login: String, password: String)
    }

    interface View {
        fun showLoader()
        fun hideLoader()
    }
}


class LoginController(private val api: Login.Api, private val view : Login.View) {
    fun login(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            view.showLoader()
            api.login(email, password)
            view.hideLoader()
        }
    }
}