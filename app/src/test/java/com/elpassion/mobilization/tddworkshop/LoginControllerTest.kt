package com.elpassion.mobilization.tddworkshop

import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import io.reactivex.subjects.SingleSubject
import org.junit.Test

class LoginControllerTest {

    private val apiSubject = SingleSubject.create<Unit>()
    private val api = mock<Login.Api> {
        on { login(any(), any()) } doReturn apiSubject
    }
    private val view = mock<Login.View>()
    private val controller = LoginController(api, view)

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
        apiSubject.onSuccess(Unit)
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
        apiSubject.onError(RuntimeException())
        verify(view).showApiError()
    }

    @Test
    fun shouldNotShowErrorAfterApiReturnsSuccess() {
        login()
        apiSubject.onSuccess(Unit)
        verify(view, never()).showApiError()
    }

    @Test
    fun shouldNotOpenNextScreenAfterApiReturnsError() {
        login()
        apiSubject.onError(RuntimeException())
        verify(view, never()).showNextScreen()
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
    }
}

class LoginController(private val api: Login.Api,
                      private val view: Login.View) {
    fun login(email: String, password: String) {
        if (email.isNotBlank() && password.isNotBlank()) {
            api.login(email, password)
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
}
