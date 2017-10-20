@file:Suppress("IllegalIdentifier")

package com.elpassion.mobilization.tddworkshop.login

import com.nhaarman.mockito_kotlin.*
import io.reactivex.Completable
import io.reactivex.subjects.CompletableSubject
import org.junit.Test

class LoginControllerTest {

    private val loginCallSubject = CompletableSubject.create()
    private val api = mock<Login.Api>().apply {
        whenever(this.login(any(), any())).thenReturn(loginCallSubject)
    }
    private val view = mock<Login.View>()

    @Test
    fun `Not call api if email is empty`() {
        login(email = "")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun `Not call api if password if empty`() {
        login(password = "")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun `Call api on login with user data`() {
        val email = "email@o.pl"
        val password = "pass"
        login(email, password )
        verify(api).login(email = email, password = password)
    }

    @Test
    fun `Show error if email is empty`() {
        login(email = "")
        verify(view).showEmptyEmailError()
    }

    @Test
    fun `Show loader after login`() {
        login()
        verify(view).showLoader()
    }

    @Test
    fun `Show Home screen after login success`() {
        login()
        loginCallSubject.onComplete()
        verify(view).openHomeScreen()
    }

    @Test
    fun `Not show home screen if user data are empty`() {
        login("", "")
        verify(view, never()).openHomeScreen()
    }

    @Test
    fun `Not show Home screem if call in progress`() {
        login()
        verify(view, never()).openHomeScreen()
    }

    private fun login(email: String = "email@wp.pl", password: String = "password") {
        LoginController(api,view).login(email, password)
    }
}

interface Login {
    interface Api {
        fun login(email: String, password: String) : Completable
    }

    interface View {
        fun showEmptyEmailError()
        fun showLoader()
        fun openHomeScreen()
    }
}

class LoginController(private val api: Login.Api, private val view: Login.View) {
    fun login(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            api.login(email, password).subscribe{
                view.openHomeScreen()
            }
            view.showLoader()
        }

        if (email.isEmpty()) {
            view.showEmptyEmailError()
        }
    }
}
