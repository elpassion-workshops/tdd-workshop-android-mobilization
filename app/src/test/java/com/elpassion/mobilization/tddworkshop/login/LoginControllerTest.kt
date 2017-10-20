@file:Suppress("IllegalIdentifier")

package com.elpassion.mobilization.tddworkshop.login

import com.nhaarman.mockito_kotlin.*
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subjects.SingleSubject
import org.junit.Assert
import org.junit.Test

class LoginControllerTest {

    private val loginSubject = SingleSubject.create<String>()

    private val api = mock<Login.Api>().apply {
        whenever(login(any(), any())).thenReturn(loginSubject)
    }

    private val view = mock<Login.View>()
    private val repository = mock<Login.Repository>()

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
        endLoginWithSuccess()
        verify(view).hideLoader()
    }

    @Test
    fun `Show main screen after successful login`() {
        login()
        endLoginWithSuccess()
        verify(view).openMainScreen()
    }

    private fun endLoginWithSuccess(token: String = "token") {
        loginSubject.onSuccess(token)
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

    @Test
    fun `Persist authentication token`() {
        login()
        val token = "token"
        endLoginWithSuccess(token)
        verify(repository).saveToken(token)
    }

    @Test
    fun `Api should be called on provided scheduler`() {
        val testScheduler = TestScheduler()
        login(ioScheduler =  testScheduler)
        Assert.assertFalse(loginSubject.hasObservers())
        testScheduler.triggerActions()
        Assert.assertTrue(loginSubject.hasObservers())
    }

    @Test
    fun `UI changes should be called on main thread`() {
        val testUiScheduler = TestScheduler()
        val testIoScheduler = TestScheduler()
        login(uiScheduler = testUiScheduler, ioScheduler = testIoScheduler)
        loginSubject.onSuccess("token")
        testIoScheduler.triggerActions()
        verify(view, never()).openMainScreen()
        testUiScheduler.triggerActions()
        verify(view).openMainScreen()
    }

    @Test
    fun `Should not show error when onDestroy during api call`() {
        val ioScheduler = TestScheduler()
        login(ioScheduler = ioScheduler).onDestroy()
        ioScheduler.triggerActions()
        loginSubject.onError(RuntimeException())
        verify(view, never()).showError()
    }

    private fun login(email: String = "email@wp.pl", password: String = "password",
                      ioScheduler: Scheduler = Schedulers.trampoline(),
                      uiScheduler: Scheduler = Schedulers.trampoline()): LoginController {
        val loginController = LoginController(api, view, repository, ioScheduler, uiScheduler)
        loginController.login(email, password)
        return loginController
    }
}

interface Login {
    interface Api {
        fun login(email: String, password: String): Single<String>
    }

    interface View {
        fun showLoader()
        fun hideLoader()
        fun openMainScreen()
        fun showError()
    }

    interface Repository {
        fun saveToken(token: String)

    }
}

class LoginController(private val api: Login.Api,
                      private val view: Login.View,
                      private val repository: Login.Repository,
                      private val ioScheduler: Scheduler,
                      private val uiScheduler: Scheduler) {

    private var disposable: Disposable? = null

    fun login(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
             disposable = api.login(email, password)
                    .subscribeOn(ioScheduler)
                    .observeOn(uiScheduler)
                    .doOnSubscribe { view.showLoader() }
                    .doAfterTerminate { view.hideLoader() }
                    .doOnSuccess { repository.saveToken(it) }
                    .subscribe(this::onSuccess, this::onError)
        }
    }

    private fun onSuccess(token: String) {
        view.openMainScreen()
    }

    private fun onError(e: Throwable) {
        view.showError()
    }

    fun onDestroy() {
        disposable?.dispose()
    }
}