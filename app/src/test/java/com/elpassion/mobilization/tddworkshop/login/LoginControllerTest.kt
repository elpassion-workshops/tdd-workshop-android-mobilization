@file:Suppress("IllegalIdentifier")

package com.elpassion.mobilization.tddworkshop.login

import com.nhaarman.mockito_kotlin.*
import io.reactivex.Completable
import io.reactivex.subjects.CompletableSubject
import org.junit.Test

class LoginControllerTest {

    private val subject = CompletableSubject.create()
    private val api = mock<Login.Api>().apply {
        whenever(login(any(), any())).thenReturn(subject)
    }
    private val view = mock<Login.View>()
    private val navigator = mock<Navigator>()

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
    fun `Not call api if password is empty`() {
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
    fun `Hide loader after calling login`() {
        login()
        subject.onComplete()
        verify(view).hideLoader()
    }

    @Test
    fun `Show loader until login call finished`() {
        login()
        verify(view, never()).hideLoader()
    }

    @Test
    fun `Show error message when api returns error`() {
        login()
        subject.onError(Exception("Error"))
        verify(view).showError()
    }

    @Test
    fun `Hide loader on api Error`() {
        login()
        subject.onError(Exception("Error"))
        verify(view).hideLoader()
    }

    @Test
    fun `Open new screen on login success`() {
        login()
        subject.onComplete()
        verify(navigator).openHome()

    }

    private fun login(email: String = "email", password: String = "password") {
        LoginController(api, view, navigator).login(email, password)
    }
}

interface Login {
    interface Api {
        fun login(login: String, password: String): Completable
    }

    interface View {
        fun showLoader()
        fun hideLoader()
        fun showError()
    }
}

interface Navigator {
    fun openHome()
}

class LoginController(private val api: Login.Api, private val view: Login.View, private val nav: Navigator) {
    fun login(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            view.showLoader()
            api.login(email, password)
                    .doAfterTerminate {
                        view.hideLoader()
                    }
                    .subscribe({
                        nav.openHome()
                    }, { throwable ->
                        view.showError()
                    })
        }
    }
}