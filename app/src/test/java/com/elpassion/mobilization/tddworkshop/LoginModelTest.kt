@file:Suppress("IllegalIdentifier")

package com.elpassion.mobilization.tddworkshop

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test

class LoginModelTest {

    private val api = mock<Login.Api>()
    private val model = LoginModel(api)

    @Test
    fun `Should start with idle state`() {
        model.states.test().assertLastValue(Login.State(false))
    }

    @Test
    fun `Should call api on login`() {
        login()
        verify(api).call("email")
    }

    @Test
    fun `Should not call api until login`() {
        verify(api, never()).call(any())
    }

    @Test
    fun `Should not call api with empty login`() {
        login("")
        verify(api, never()).call(any())
    }

    @Test
    fun `Should call api with provided login`() {
        login("provided login")
        verify(api).call("provided login")
    }

    @Test
    fun `Should not call api when password is empty`() {
        login(password = "")
        verify(api, never()).call(any())
    }

    private fun login(email: String = "email", password: String = "password") {
        model.events.accept(Login.Event.LoginClicked(email, password))
    }
}

interface Login {
    sealed class Event {
        class LoginClicked(val email: String, val password: String) : Event()
    }

    data class State(val loader: Boolean)

    interface Api {
        fun call(email: String)
    }
}

class LoginModel(api: Login.Api) : Model<Login.Event, Login.State>(Login.State(false)) {

    private val disposable =
            events
                    .ofType(Login.Event.LoginClicked::class.java)
                    .filter { it.email.isNotEmpty() }
                    .filter { it.password.isNotEmpty() }
                    .map { api.call(it.email) }
                    .map { Login.State(false) }
                    .subscribe(states)

    override fun onCleared() = Unit
}