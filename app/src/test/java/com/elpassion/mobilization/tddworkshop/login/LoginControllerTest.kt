@file:Suppress("IllegalIdentifier")

package com.elpassion.mobilization.tddworkshop.login

import com.nhaarman.mockito_kotlin.*
import io.reactivex.Completable
import io.reactivex.subjects.CompletableSubject
import org.junit.Test

class LoginControllerTest {

    private val apiSubject = CompletableSubject.create()
    private val api = mock<Login.Api> {
        on { login(any(), any()) } doReturn apiSubject
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
    fun `Call api if email and password are provided`() {
        login()
        verify(api).login(any(), any())
    }

    @Test
    fun `Show loader when sending request api`() {
        login()
        verify(view).showLoader()
    }

    @Test
    fun `Show next screen when api request returned success`() {
        login()
        apiSubject.onComplete()
        verify(view).showAfterLoginScreen()
    }

    @Test
    fun `Do not show next screen until api returns status`() {
        login()
        verify(view, never()).showAfterLoginScreen()
    }

    @Test
    fun `Show error screen on api request error status`() {
        login()
        apiSubject.onError(RuntimeException())
        verify(view).showError()
    }

    @Test
    fun `Hide loader when request success `(){
        login()
        apiSubject.onComplete()
        verify(view).hideLoader()
    }

    private fun login(email: String = "email@wp.pl", password: String = "testPassword") {
        LoginController(api, view).login(email, password)
    }
}

interface Login {
    interface Api {
        fun login(email: String, password: String): Completable
    }

    interface View {
        fun showLoader()
        fun showAfterLoginScreen()
        fun showError()
        fun hideLoader()
    }
}

class LoginController(private val api: Login.Api, private val view: Login.View) {

    fun login(email: String, password: String) {

        if (email.isNotEmpty() && password.isNotEmpty()) {
            api.login(email, password)
                    .subscribe({
                        view.showAfterLoginScreen()
                        view.hideLoader()
                    }, {
                        view.showError()
                    })
            view.showLoader()
        }
    }
}