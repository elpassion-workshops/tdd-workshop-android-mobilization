@file:Suppress("IllegalIdentifier")

package com.elpassion.mobilization.tddworkshop

import com.nhaarman.mockito_kotlin.*
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subjects.CompletableSubject
import org.junit.Test

class LoginControllerTest {

    private val loginSubject = CompletableSubject.create()
    private val api = mock<Login.Api>().apply {
        whenever(login(any(), any())).thenReturn(loginSubject)
    }
    private val view = mock<Login.View>()
    private val loginController = LoginController(api, view, Schedulers.trampoline())

    @Test
    fun `Call api on login`() {
        login()
        verify(api).login(any(), any())
    }

    @Test
    fun `Not call api with empty email`() {
        login(email = "")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun `Not call api with empty password`() {
        login(password = "")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun `Call api with provided login and password`() {
        login("otheremail@wp.pl", "other password")
        verify(api).login("otheremail@wp.pl", "other password")
    }

    @Test
    fun `Show loader when api call starts`() {
        login()
        verify(view).showLoader()
    }

    @Test
    fun `Not show loader when email is empty`() {
        login(email = "")
        verify(view, never()).showLoader()
    }

    @Test
    fun `Open next screen on successful api call`() {
        login()
        loginSubject.onComplete()
        verify(view).openNextScreen()
    }

    @Test
    fun `Not open next screen before api call finished`() {
        login()
        verify(view, never()).openNextScreen()
    }

    @Test
    fun `Show error on api call error`() {
        login()
        loginSubject.onError(RuntimeException())
        verify(view).showLoginCallError()
    }

    @Test
    fun `Show empty email error`() {
        login(email = "")
        verify(view).showEmptyEmailError()
    }

    @Test
    fun `Show empty password error`() {
        login(password = "")
        verify(view).showEmptyPasswordError()
    }

    @Test
    fun `Hide loader when api call finished with error`() {
        login()
        loginSubject.onError(RuntimeException())
        verify(view).hideLoader()
    }

    @Test
    fun `Hide loader on destroy if call is still in progress`() {
        login()
        loginController.onDestroy()
        verify(view).hideLoader()
    }

    @Test
    fun `Not hide loader on destroy if call is not in progress`() {
        loginController.onDestroy()
        verify(view, never()).hideLoader()
    }

    @Test
    fun `Call api on on given scheduler`() {
        val ioScheduler = TestScheduler()
        LoginController(api, view, ioScheduler).onLogin("email@wp.pl", "password")
        verify(view, never()).showLoader()
        ioScheduler.triggerActions()
        verify(view).showLoader()
    }

    private fun login(email: String = "email@wp.pl", password: String = "password") {
        loginController.onLogin(email, password)
    }
}

interface Login {
    interface Api {
        fun login(email: String, password: String): Completable
    }

    interface View {
        fun showLoader()
        fun openNextScreen()
        fun showLoginCallError()
        fun showEmptyEmailError()
        fun showEmptyPasswordError()
        fun hideLoader()
    }
}

class LoginController(private val api: Login.Api, private val view: Login.View, private val ioScheduler: Scheduler) {

    private var disposable: Disposable? = null

    fun onLogin(email: String, password: String) {
        if (email.isEmpty()) {
            view.showEmptyEmailError()
        }
        if (password.isEmpty()) {
            view.showEmptyPasswordError()
        }
        if (email.isNotEmpty() && password.isNotEmpty()) {
            disposable = api.login(email, password)
                    .doOnSubscribe { view.showLoader() }
                    .doFinally { view.hideLoader() }
                    .subscribeOn(ioScheduler)
                    .subscribe(
                            { view.openNextScreen() },
                            { view.showLoginCallError() })
        }
    }

    fun onDestroy() {
        disposable?.dispose()
    }
}
