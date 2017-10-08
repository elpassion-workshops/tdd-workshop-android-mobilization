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
        verify(api).call("login")
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

    private fun login(login: String = "login") {
        model.events.accept(Login.Event.LoginClicked(login))
    }
}

interface Login {
    sealed class Event {
        class LoginClicked(val login: String) : Event()
    }

    data class State(val loader: Boolean)

    interface Api {
        fun call(login: String)
    }
}

class LoginModel(api: Login.Api) : Model<Login.Event, Login.State>(Login.State(false)) {

    private val disposable =
            events
                    .ofType(Login.Event.LoginClicked::class.java)
                    .filter { it.login.isNotEmpty() }
                    .map { api.call(it.login) }
                    .map { Login.State(false) }
                    .subscribe(states)

    override fun onCleared() = Unit
}