@file:Suppress("IllegalIdentifier")

package com.elpassion.mobilization.tddworkshop.login

import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import io.reactivex.subjects.SingleSubject
import org.junit.Test
class LoginControllerTest {

    private val subject = SingleSubject.create<Login.User>()
    private val api = mock<Login.Api>().apply {
        whenever(login(any(), any())).thenReturn(subject)
    }
    private val view = mock<Login.View>()
    private val navigator = mock<Navigator>()
    private val repository = mock<Repository>()

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
        mockSuccessfulLoginAsyncEnd()
        verify(view).hideLoader()
    }

    @Test
    fun `Show loader until login call finished`() {
        login()
        verify(view, never()).hideLoader()
    }

    @Test
    fun `Show error message when api returns error`() {
        mockErrorLoginAsyncAttempt()
        verify(view).showError()
    }

    @Test
    fun `Hide loader on api Error`() {
        mockErrorLoginAsyncAttempt()
        verify(view).hideLoader()
    }

    @Test
    fun `Open new screen on login success`() {
        mockSuccessfulLoginAsyncEnd()
        verify(navigator).openHome()
    }

    @Test
    fun `Save user to repository after succeed`() {
        mockSuccessfulLoginAsyncEnd()
        verify(repository).save(any<Login.User>())
    }

    @Test
    fun `Saved user object should have same data as api returned`() {
        val user = Login.User("name", "lastName")
        mockSuccessfulLoginAsyncEnd(user)
        verify(repository).save(user)
    }

    private fun mockErrorLoginAsyncAttempt() {
        login()
        subject.onError(Exception("Error"))
    }

    private fun mockSuccessfulLoginAsyncEnd(user: Login.User = Login.User("", "")) {
        login()
        subject.onSuccess(user)
    }

    private fun login(email: String = "email", password: String = "password") {
        LoginController(api, view, navigator,repository).login(email, password)
    }
}

interface Login {
    interface Api {
        fun login(login: String, password: String): Single<User>
    }

    interface View {
        fun showLoader()
        fun hideLoader()
        fun showError()
    }

    data class User(val userName: String, val userLastName: String)

}
interface Navigator {

    fun openHome()
}
interface Repository {
    fun save(user: Login.User)
}

class LoginController(private val api: Login.Api, private val view: Login.View, private val nav: Navigator, private val repo: Repository) {
    fun login(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            view.showLoader()
            api.login(email, password)
                    .doAfterTerminate {
                        view.hideLoader()
                    }
                    .subscribe({user ->
                        repo.save(user)
                        nav.openHome()
                    }, { throwable ->
                        view.showError()
                    })
        }
    }
}