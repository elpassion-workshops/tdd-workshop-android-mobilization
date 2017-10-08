@file:Suppress("IllegalIdentifier")

package com.elpassion.mobilization.tddworkshop

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
        model.events.accept(Login.Event.LoginClicked)
        verify(api).call()
    }

    @Test
    fun `Should not call api until login`() {
        verify(api, never()).call()
    }
}

interface Login {
    sealed class Event {
        object LoginClicked : Event()
    }

    data class State(val loader: Boolean)

    interface Api {
        fun call()
    }
}

class LoginModel(api: Login.Api) : Model<Login.Event, Login.State>(Login.State(false)) {

    private val disposable =
            events
                    .map { api.call() }
                    .map { Login.State(false) }
                    .subscribe(states)

    override fun onCleared() = Unit
}