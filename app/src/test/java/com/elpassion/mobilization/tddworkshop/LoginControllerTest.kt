@file:Suppress("IllegalIdentifier")

package com.elpassion.mobilization.tddworkshop

import com.elpassion.mobilization.tddworkshop.login.Login
import com.elpassion.mobilization.tddworkshop.login.LoginController
import com.nhaarman.mockito_kotlin.*
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subjects.SingleSubject
import org.junit.Test

class LoginControllerTest {

    private val loginSubject = SingleSubject.create<Login.User>()
    private val api = mock<Login.Api>().apply {
        whenever(login(any(), any())).thenReturn(loginSubject)
    }
    private val view = mock<Login.View>()
    private val repository = mock<Login.Repository>()
    private val loginController = LoginController(api, view, repository, Schedulers.trampoline(), Schedulers.trampoline())

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
        completeApiCall()
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
        LoginController(api, view, repository, ioScheduler, Schedulers.trampoline()).onLogin("email@wp.pl", "password")
        verify(view, never()).showLoader()
        ioScheduler.triggerActions()
        verify(view).showLoader()
    }

    @Test
    fun `Observe api result on main scheduler`() {
        val uiScheduler = TestScheduler()
        LoginController(api, view, repository, Schedulers.trampoline(), uiScheduler).onLogin("email@wp.pl", "password")
        completeApiCall()
        verify(view, never()).hideLoader()
        uiScheduler.triggerActions()
        verify(view).hideLoader()
    }

    @Test
    fun `Save returned user to repository`() {
        login()
        val user = Login.User(id = 1)
        completeApiCall(user)
        verify(repository).save(user)
    }

    private fun completeApiCall(user: Login.User = Login.User(id = 1)) {
        loginSubject.onSuccess(user)
    }

    private fun login(email: String = "email@wp.pl", password: String = "password") {
        loginController.onLogin(email, password)
    }
}