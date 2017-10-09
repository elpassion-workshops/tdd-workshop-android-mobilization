@file:Suppress("IllegalIdentifier")

package com.elpassion.mobilization.tddworkshop

import com.jakewharton.rxrelay2.BehaviorRelay
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Observable
import io.reactivex.rxkotlin.withLatestFrom
import org.junit.Test

class LoginModelTest {

    private val api = mock<Login.Api>()
    private val model = LoginModel(api)

    @Test
    fun `Should start with idle state`() {
        model.states.test().assertLastValue(Login.State(false, false, false))
    }

    @Test
    fun `Should call api on login`() {
        login()
        verify(api).call("email", "password")
    }

    @Test
    fun `Should not call api until login`() {
        verify(api, never()).call(any(), any())
    }

    @Test
    fun `Should not call api with empty login`() {
        login(email = "")
        verify(api, never()).call(any(), any())
    }

    @Test
    fun `Should call api with provided login`() {
        login(email = "provided email")
        verify(api).call(eq("provided email"), any())
    }

    @Test
    fun `Should not call api when password is empty`() {
        login(password = "")
        verify(api, never()).call(any(), any())
    }

    @Test
    fun `Should call api with provided password`() {
        login(password = "provided password")
        verify(api).call(any(), eq("provided password"))
    }

    @Test
    fun `Should show empty email error`() {
        login(email = "")
        assertLastState { emptyEmailError }
    }

    @Test
    fun `Should show empty password error`() {
        login(password = "")
        assertLastState { emptyPasswordError }
    }

    @Test
    fun `Should remove empty email error after logging when email provided`() {
        login(email = "")
        login(email = "email@test.pl")
        assertLastState { !emptyEmailError }
    }

    private fun login(email: String = "email", password: String = "password") {
        model.events.accept(Login.Event.LoginClicked(email, password))
    }

    private fun assertLastState(predicate: Login.State.() -> Boolean) {
        model.states.test().assertLastValueThat(predicate)
    }
}

interface Login {
    sealed class Event {
        class LoginClicked(val email: String, val password: String) : Event()
    }

    data class State(val loader: Boolean, val emptyEmailError: Boolean, val emptyPasswordError: Boolean)

    interface Api {
        fun call(email: String, password: String)
    }
}

class LoginModel(private val api: Login.Api) : Model<Login.Event, Login.State>(Login.State(loader = false, emptyEmailError = false, emptyPasswordError = false)) {

    init {
        Observable.merge(
                loginWithCredentials(),
                loginWithEmptyEmail(),
                loginWithEmptyPassword()
        ).subscribe(states)
    }

    private fun loginWithCredentials(): Observable<Login.State> {
        return events
                .ofType(Login.Event.LoginClicked::class.java)
                .filter { it.email.isNotEmpty() }
                .filter { it.password.isNotEmpty() }
                .map { api.call(it.email, it.password) }
                .onLatestFrom(states) {
                    this
                }
    }

    private fun loginWithEmptyEmail(): Observable<Login.State> {
        return events.ofType(Login.Event.LoginClicked::class.java)
                .onLatestFrom(states) {
                    copy(emptyEmailError = it.email.isEmpty())
                }
    }

    private fun loginWithEmptyPassword(): Observable<Login.State> {
        return events.ofType(Login.Event.LoginClicked::class.java)
                .onLatestFrom(states) {
                    copy(emptyPasswordError = it.password.isEmpty())
                }
    }

    override fun onCleared() = Unit
}

private fun <T, State> Observable<T>.onLatestFrom(states: BehaviorRelay<State>, action: State.(T) -> State) =
        withLatestFrom(states, { item, state -> state.action(item) })
