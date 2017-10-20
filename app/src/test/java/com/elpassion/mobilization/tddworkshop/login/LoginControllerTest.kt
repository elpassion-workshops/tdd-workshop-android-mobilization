@file:Suppress("IllegalIdentifier")

package com.elpassion.mobilization.tddworkshop.login

import com.nhaarman.mockito_kotlin.*
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subjects.SingleSubject
import org.junit.Test

class LoginControllerTest {

    private val completableSubject = SingleSubject.create<String>()
    private val api = mock<Login.Api>().apply { whenever(login(any(), any())).thenReturn(completableSubject) }
    private val view = mock<Login.View>()
    private val repo = mock<Login.Repo>()

    @Test
    fun `Call api on login`() {
        login()
        verify(api).login("email@wp.pl", "password")
    }

    @Test
    fun `Not call api if email is empty`() {
        login(email = "")
        verify(api, never()).login("email@wp.pl", "password")
    }

    @Test
    fun `Not call api if empty password`() {
        login(password = "")
        verify(api, never()).login("email@wp.pl", "password")
    }

    @Test
    fun `Not call api if email and password are empty`() {
        login(email = "", password = "")
        verify(api, never()).login("email@wp.pl", "password")
    }

    @Test
    fun `Show progress view on login action`() {
        login()
        verify(view).showProgressView()
    }

    @Test
    fun `Check email and password passed to api`() {
        login()
        verify(api).login("email@wp.pl", "password")
    }

    @Test
    fun `Show error when email is incorrect`() {
        login(email = "")
        verify(view).setEmailErrorMessage()
    }

    @Test
    fun `Show error when password is incorrect`() {
        login(password = "")
        verify(view).setPasswordErrorMessage()
    }

    @Test
    fun `Show dashboard when login success`() {
        login()
        completableSubject.mockSuccess()
        verify(view).showDashboardView()
    }

    @Test
    fun `Not show dashboard when login in progress`() {
        login()
        verify(view, never()).showDashboardView()
    }

    @Test
    fun `Not show dashboard when login in failed`() {
        login()
        completableSubject.mockError()
        verify(view, never()).showDashboardView()
    }

    @Test
    fun `Show error when login in failed`() {
        login()
        completableSubject.mockError()
        verify(view).showLoginFailedMessage()
    }

    @Test
    fun `Persist user data`() {
        login(email = "test@test.te")
        completableSubject.mockSuccess()
        verify(repo).persistUserData(email = "test@test.te", token = "token")
    }

    @Test
    fun `Check login is performed on IOThread`() {
        val ioScheduler = TestScheduler()
        login(ioScheduler = ioScheduler)

        completableSubject.mockSuccess()
        verify(view, never()).showDashboardView()
        ioScheduler.triggerActions()
        verify(view).showDashboardView()

    }

    @Test
    fun `Check show dashboard is performed on UiThread`() {
        val uiScheduler = TestScheduler()
        login(uiScheduler = uiScheduler)

        completableSubject.mockSuccess()
        verify(view, never()).showDashboardView()
        uiScheduler.triggerActions()
        verify(view).showDashboardView()
    }

    @Test
    fun `Check if login break on view exit`() {
        val loginController = login()

        verify(view).showProgressView()
        verify(view, never()).hideProgressView()
        loginController.onDestroy()
        verify(view).hideProgressView()
        verify(view, never()).showDashboardView()

    }

    private fun login(email: String = "email@wp.pl", password: String = "password",
                      uiScheduler: Scheduler = Schedulers.trampoline(), ioScheduler: Scheduler = Schedulers.trampoline()): LoginController {
        val loginController = LoginController(api, view, repo, uiScheduler, ioScheduler)
        loginController.login(email, password)
        return loginController

    }

}

private fun <T> SingleSubject<T>.mockError() {
    this.onError(RuntimeException())
}

private fun SingleSubject<String>.mockSuccess() {
    this.onSuccess("token")
}