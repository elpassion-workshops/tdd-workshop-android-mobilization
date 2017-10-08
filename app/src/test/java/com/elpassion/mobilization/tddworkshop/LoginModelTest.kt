package com.elpassion.mobilization.tddworkshop

import org.junit.Assert
import org.junit.Test

class LoginModelTest {

    @Test
    fun shouldInitLoginModel() {
        Assert.assertNotNull(LoginModel())
    }

    @Test
    fun shouldStartWithIdleState() {
        LoginModel().states.test().assertLastValue(Login.State(false))
    }
}

interface Login {
    sealed class Event
    data class State(val loader: Boolean)
}

class LoginModel : Model<Login.Event, Login.State>(Login.State(false)) {
    override fun onCleared() = Unit
}