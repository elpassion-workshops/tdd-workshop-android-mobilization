@file:Suppress("IllegalIdentifier")

package com.elpassion.mobilization.tddworkshop.login

import com.nhaarman.mockito_kotlin.*
import io.reactivex.Completable
import io.reactivex.subjects.CompletableSubject
import org.junit.Test

class LoginControllerTest {

    private val loginSubject = CompletableSubject.create()

    private val api = mock<Login.Api>().apply {
        whenever(login(any(), any())).thenReturn(loginSubject)
    }

    private val view = mock<Login.View>()

    @Test
    fun `Call api on login`() {
        login()
        verify(api).login(any(), any())
    }

    @Test
    fun `Not call api if email is empty`() {
        login(email = "")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun `Not call api if password is empty`() {
        login(password = "")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun `Call api with provided email and password`() {
        val email = "email@e.p"
        val password = "password"
        login(email, password)
        verify(api).login(email, password)
    }

    @Test
    fun `Show loader on api call`() {
        login()
        verify(view).showLoader()
    }

    @Test
    fun `Not show loader when not calling api`() {
        login(email = "", password = "")
        verify(view, never()).showLoader()
    }


    @Test
    fun `Hide loader when api call is finished`() {
        login()
        loginSubject.onComplete()
        verify(view).hideLoader()
    }

    @Test
    fun `Show main screen after successful login`() {
        login()
        loginSubject.onComplete()
        verify(view).openMainScreen()
    }

    @Test
    fun `Hide loader when login call failed`() {
        login()
        loginSubject.onError(RuntimeException())
        verify(view).hideLoader()
    }

    @Test
    fun `Show error when login call failed`() {
        login()
        loginSubject.onError(RuntimeException())
        verify(view).showError()
    }

    private fun login(email: String = "email@wp.pl", password: String = "password") {
        LoginController(api, view).login(email, password)
    }
}

interface Login {
    interface Api {
        fun login(email: String, password: String): Completable
    }

    interface View {
        fun showLoader()
        fun hideLoader()
        fun openMainScreen()
        fun showError()
    }
}

class LoginController(private val api: Login.Api, private val view: Login.View) {
    fun login(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            view.showLoader()
            api.login(email, password).subscribe({
                view.hideLoader()
                view.openMainScreen()
            }, {
                view.hideLoader()
                view.showError()
            })
        }
    }
}