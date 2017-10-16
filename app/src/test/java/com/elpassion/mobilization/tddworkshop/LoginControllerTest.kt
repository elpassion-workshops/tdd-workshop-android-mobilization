package com.elpassion.mobilization.tddworkshop

import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subjects.SingleSubject
import org.junit.Assert
import org.junit.Test

class LoginControllerTest {

    private val apiSubject = SingleSubject.create<Unit>()
    private val api = mock<Login.Api> {
        on { login(any(), any()) } doReturn apiSubject
    }
    private val view = mock<Login.View>()
    private val subscribeOnScheduler = TestScheduler()
    private val observeOnScheduler = TestScheduler()
    private val controller = LoginController(api, view, subscribeOnScheduler, observeOnScheduler)

    @Test
    fun shouldCallApiOnLogin() {
        login()
        verify(api).login(any(), any())
    }

    @Test
    fun shouldCallApiWithProvidedEmail() {
        val specificEmail = "specificEmail"
        login(email = specificEmail)
        verify(api).login(eq(specificEmail), any())
    }

    @Test
    fun shouldNotCallApiWhenEmailIsEmpty() {
        login(email = "")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldCallApiWithProvidedPassword() {
        val specificPassword = "specificPassword"
        login(password = specificPassword)
        verify(api).login(any(), eq(specificPassword))
    }

    @Test
    fun shouldNotCallApiWhenPasswordIsEmpty() {
        login(password = "")
        verify(api, never()).login(any(), any())
    }

    @Test
    fun shouldShowEmptyEmailErrorWhenEmailIsEmpty() {
        login(email = "")
        verify(view).showEmptyEmailError()
    }

    @Test
    fun shouldShowEmptyPasswordErrorWhenPasswordIsEmpty() {
        login(password = "")
        verify(view).showEmptyPasswordError()
    }

    @Test
    fun shouldOpenNextScreenOnLoginSucceed() {
        login()
        stubApiWithSuccess()
        verify(view).showNextScreen()
    }

    @Test
    fun shouldNotOpenNextScreenWhenCredentialsAreInvalid() {
        login(email = "")
        verify(view, never()).showNextScreen()
    }

    @Test
    fun shouldShowErrorAfterApiReturnsError() {
        login()
        stubApiToReturnError()
        verify(view).showApiError()
    }

    @Test
    fun shouldNotShowErrorAfterApiReturnsSuccess() {
        login()
        stubApiWithSuccess()
        verify(view, never()).showApiError()
    }

    @Test
    fun shouldNotOpenNextScreenAfterApiReturnsError() {
        login()
        stubApiToReturnError()
        verify(view, never()).showNextScreen()
    }

    @Test
    fun shouldShowLoaderWhenCallingApi() {
        login()
        verify(view).showLoader()
    }

    @Test
    fun shouldHideLoaderWhenCallFinishes() {
        login()
        stubApiWithSuccess()
        verify(view).hideLoader()
    }

    @Test
    fun shouldNotHideLoaderBeforeCallEnds() {
        login()
        verify(view, never()).hideLoader()
    }

    @Test
    fun shouldHideLoaderWhenCallEndsWithError() {
        login()
        stubApiToReturnError()
        verify(view).hideLoader()
    }

    @Test
    fun shouldHideLoaderOnDestroy() {
        login()
        controller.onDestroy()
        verify(view).hideLoader()
    }

    @Test
    fun shouldNotHideLoaderOnDestroyWhenLoaderWasNotShown() {
        controller.onDestroy()
        verify(view, never()).hideLoader()
    }

    @Test
    fun shouldNotSubscribeToApiUntilSchedulerAllows() {
        login()
        Assert.assertFalse(apiSubject.hasObservers())
    }

    @Test
    fun shouldNotPropagateApiResponseToUiUntilSchedulerAllows() {
        login()
        apiSubject.onSuccess(Unit)
        subscribeOnScheduler.triggerActions()
        verify(view, never()).showNextScreen()
    }

    private fun stubApiToReturnError() {
        apiSubject.onError(RuntimeException())
        subscribeOnScheduler.triggerActions()
        observeOnScheduler.triggerActions()
    }

    private fun stubApiWithSuccess() {
        apiSubject.onSuccess(Unit)
        subscribeOnScheduler.triggerActions()
        observeOnScheduler.triggerActions()
    }

    private fun login(email: String = "email", password: String = "password") {
        controller.login(email, password)
    }
}

interface Login {
    interface Api {
        fun login(email: String, password: String): Single<Unit>
    }

    interface View {
        fun showEmptyEmailError()
        fun showEmptyPasswordError()
        fun showNextScreen()
        fun showApiError()
        fun showLoader()
        fun hideLoader()
    }
}

class LoginController(private val api: Login.Api,
                      private val view: Login.View,
                      private val subscribeOnScheduler: TestScheduler,
                      private val observeOnScheduler: TestScheduler) {
    private var disposable: Disposable? = null

    fun login(email: String, password: String) {
        if (email.isNotBlank() && password.isNotBlank()) {
            disposable = api.login(email, password)
                    .subscribeOn(subscribeOnScheduler)
                    .observeOn(observeOnScheduler)
                    .doOnSubscribe { view.showLoader() }
                    .doFinally { view.hideLoader() }
                    .subscribe({
                        view.showNextScreen()
                    }, {
                        view.showApiError()
                    })
        } else if (email.isEmpty()) {
            view.showEmptyEmailError()
        } else if (password.isEmpty()) {
            view.showEmptyPasswordError()
        }
    }

    fun onDestroy() {
        disposable?.dispose()
    }
}
