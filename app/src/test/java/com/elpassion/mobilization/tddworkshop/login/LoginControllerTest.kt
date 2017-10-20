@file:Suppress("IllegalIdentifier")

package com.elpassion.mobilization.tddworkshop.login

import com.nhaarman.mockito_kotlin.*
import io.reactivex.Completable
import io.reactivex.subjects.CompletableSubject
import org.junit.Test

class LoginControllerTest {

    private val api = mock<Login.Api>()
    private val view = mock<Login.View>()
    private val loginSubject = CompletableSubject.create()

    @Test
    fun `Call api on login`() {
        login()
        verify(api).login(any(), any())
    }

    @Test
    fun `Not call api if email is empty`() {
        val emptyLogin = ""
        login(email = emptyLogin)
        verify(api, never()).login(any(), any())
    }

    private fun login(email: String = "email@wp.pl", password: String = "passwd") {
        whenever(api.login(any(), any())).thenReturn(loginSubject)
        LoginController(api, view).login(email, password)
    }

    @Test
    fun `Login with password and email`() {
        val email = "email@email.com"
        val password = "passwd"
        login(email, password)
        verify(api).login(email, password)
    }

    @Test
    fun `Controller should show loader when login API call will start`() {
        login()
        verify(view).showLoader()
    }

    @Test
    fun `Controller should hide loader on view after successful API login call`() {
        login()
        loginSubject.onComplete()
        verify(view).hideLoader()
    }

    @Test
    fun `Dont hide loader until login is finished`() {
        login()
        verify(view, never()).hideLoader()
    }
}
o
interface Login {
    interface Api {
        fun login(email: String, password: String): Completable
    }

    interface View {
        fun showLoader()
        fun hideLoader()
        fun showError()
    }
}

class LoginController(private val api: Login.Api, private val view: Login.View) {
    fun login(email: String, password: String) {
        view.showLoader()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            api.login(email, password).subscribe { onApiLoginCompleted() }
        } else {
            view.showError()
        }
    }

    private fun onApiLoginCompleted() {
        view.hideLoader()
    }
}