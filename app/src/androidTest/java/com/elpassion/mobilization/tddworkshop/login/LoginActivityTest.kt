package com.elpassion.mobilization.tddworkshop.login

import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withInputType
import android.support.test.rule.ActivityTestRule
import android.text.InputType.TYPE_CLASS_TEXT
import android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
import com.elpassion.android.commons.espresso.*
import com.elpassion.mobilization.tddworkshop.R
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.junit.Rule
import org.junit.Test

class LoginActivityTest {

    val api = mock<Login.Api>()
    val repository = mock<Login.Repository>()

    @Rule @JvmField
    val rule = object : ActivityTestRule<LoginActivity>(LoginActivity::class.java) {
        override fun beforeActivityLaunched() {
            LoginActivity.api = api
            LoginActivity.repository = repository
        }
    }

    @Test
    fun should_show_email_header() {
        onText(R.string.email_header).isDisplayed()
    }

    @Test
    fun should_show_email_input() {
        onId(R.id.emailInput)
                .replaceText("test@mail.com")
                .hasText("test@mail.com")
    }

    @Test
    fun should_email_input_has_type_email() {
        onId(R.id.emailInput)
                .check(matches(withInputType(TYPE_CLASS_TEXT or TYPE_TEXT_VARIATION_EMAIL_ADDRESS)))
    }

    @Test
    fun should_show_password_header() {
        onText(R.string.password_header).isDisplayed()
    }

    @Test
    fun should_show_password_input() {
        onId(R.id.passwordInput)
                .replaceText("pass")
                .hasText("pass")
    }

    @Test
    fun Call_api_with_provded_email_and_password_on_login_click() {
        onId(R.id.emailInput).replaceText("test@test.com")
        onId(R.id.passwordInput).replaceText("test@test.com")
        onId(R.id.loginButton).click()
        verify(api).login("test@test.com", "test@test.com")
    }

    @Test
    fun should_show_error_if_email_is_empty() {
        onId(R.id.emailInput).replaceText("")
        onId(R.id.loginButton).click()
        verify(api, never()).login(any(), any())
        onText(R.string.empty_email_message).isDisplayed()
    }
}

