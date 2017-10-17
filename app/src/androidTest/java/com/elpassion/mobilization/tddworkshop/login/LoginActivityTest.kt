package com.elpassion.mobilization.tddworkshop.login

import android.support.test.rule.ActivityTestRule
import com.elpassion.android.commons.espresso.*
import com.elpassion.mobilization.tddworkshop.R
import com.elpassion.mobilization.tddworkshop.util.hasHiddenInput
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.subjects.SingleSubject
import org.junit.Rule
import org.junit.Test

class LoginActivityTest {

    private val loginSubject = SingleSubject.create<Login.User>()
    private val api = mock<Login.Api>().apply {
        whenever(login(any(), any())).thenReturn(loginSubject)
    }

    @Rule @JvmField
    val rule = object : ActivityTestRule<LoginActivity>(LoginActivity::class.java) {
        override fun beforeActivityLaunched() {
            LoginActivity.api = api
        }
    }

    @Test
    fun Display_email_header() {
        onText(R.string.email_header).isDisplayed()
    }

    @Test
    fun Display_email_input() {
        onId(R.id.emailInput)
                .isDisplayed()
                .replaceText("email@wp.pl")
                .hasText("email@wp.pl")
    }

    @Test
    fun Display_password_header() {
        onText(R.string.password_header).isDisplayed()
    }

    @Test
    fun Display_password_input() {
        onId(R.id.passwordInput)
                .hasHiddenInput()
                .isDisplayed()
                .replaceText("password")
                .hasText("password")
    }

    @Test
    fun Call_api_with_provided_email_and_password_on_login_click() {
        login()
        verify(api).login("email@email.pl", "password")
    }

    @Test
    fun Show_loader_on_login() {
        login()
        onId(R.id.loader).isDisplayed()
    }

    @Test
    fun Login_button_should_have_text() {
        onId(R.id.loginButton).hasText("Login")
    }

    @Test
    fun Not_show_loader_before_login() {
        onId(R.id.loader).isNotDisplayed()
    }

    @Test
    fun Hide_loader_on_error_from_api() {
        login()
        loginSubject.onError(RuntimeException())
        onId(R.id.loader).isNotDisplayed()
    }

    private fun login() {
        onId(R.id.emailInput).replaceText("email@email.pl")
        onId(R.id.passwordInput).replaceText("password")
        onId(R.id.loginButton).click()
    }
}
