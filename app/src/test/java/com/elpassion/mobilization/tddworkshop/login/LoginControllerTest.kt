@file:Suppress("IllegalIdentifier")

package com.elpassion.mobilization.tddworkshop.login

import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import io.reactivex.subjects.SingleSubject
import org.junit.Test

class LoginControllerTest {
    companion object {
        private val USER_EMAIL = "email@wp.pl"
        private val USER_PASSWORD = "password"
    }

    private val user = User(USER_EMAIL, USER_PASSWORD)
    private val loginCallSubject = SingleSubject.create<User>()
    private val repository = mock<Login.Repository>()
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
        login(email, password)
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
    fun `Not show home screen if user data are empty`() {
        login("", "")
        verify(view, never()).openHomeScreen()
    }

    @Test
    fun `Not show Home screen if call in progress`() {
        login()
        verify(view, never()).openHomeScreen()
    }

    @Test
    fun `Show error when login fails`() {
        login()
        loginCallSubject.onError(RuntimeException())
        verify(view).showLoginError()
    }

    @Test
    fun `Show Home screen after login success`() {
        login()
        loginCallSubject.onSuccess(user)
        verify(view).openHomeScreen()
    }

    @Test
    fun `Save User token after login success`() {
        login()
        loginCallSubject.onSuccess(user)
        verify(repository).saveUserToken(any<User>())
    }

    @Test
    fun `Don't save token after login fails`() {
        login()
        loginCallSubject.onError(RuntimeException())
        verify(repository, never()).saveUserToken(any<User>())
    }

    @Test
    fun `Hide loader after login success`() {
        login()
        loginCallSubject.onSuccess(user)
        verify(view).hideLoader()
    }

    @Test
    fun `Hide loader after login fails`() {
        login()
        loginCallSubject.onError(RuntimeException())
        verify(view).hideLoader()
    }

    private fun login(email: String = USER_EMAIL, password: String = USER_PASSWORD) {
        LoginController(api, view, repository).login(email, password)
    }
}

interface Login {
    interface Api {
        fun login(email: String, password: String): Single<User>
    }

    interface View {
        fun showEmptyEmailError()
        fun showLoader()
        fun hideLoader()
        fun openHomeScreen()
        fun showLoginError()
    }

    interface Repository {
        fun saveUserToken(user: User)
    }
}

class LoginController(private val api: Login.Api, private val view: Login.View, private val repository: Login.Repository) {
    fun login(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            api.login(email, password)
                    .doAfterTerminate(view::hideLoader)
                    .subscribe(this::handleSuccess, this::handleError)
            view.showLoader()
        }

        if (email.isEmpty()) {
            view.showEmptyEmailError()
        }
    }

    private fun handleError(t: Throwable) {
        view.showLoginError()
    }

    private fun handleSuccess(user: User) {
        repository.saveUserToken(user)
        view.openHomeScreen()
    }
}

data class User(val email: String, val token: String)