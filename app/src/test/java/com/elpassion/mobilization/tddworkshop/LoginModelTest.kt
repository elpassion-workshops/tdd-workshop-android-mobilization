package com.elpassion.mobilization.tddworkshop

import org.junit.Test

class LoginModelTest {

    private val model = LoginModel()

    @Test
    fun shouldStartWithIdleState() {
        model.states.test().assertLastValue(Login.State(false))
    }
}

interface Login {
    sealed class Event
    data class State(val loader: Boolean)
}

class LoginModel : Model<Login.Event, Login.State>(Login.State(false)) {
    override fun onCleared() = Unit
}