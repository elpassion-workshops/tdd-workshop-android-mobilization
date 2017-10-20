package com.elpassion.mobilization.tddworkshop.login

import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withInputType
import android.support.test.rule.ActivityTestRule
import android.text.InputType.*
import com.elpassion.android.commons.espresso.*
import com.elpassion.mobilization.tddworkshop.R
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Rule
import org.junit.Test

class LoginActivityTest {

    @JvmField
    @Rule
    val rule =object: ActivityTestRule<LoginActivity>(LoginActivity::class.java){
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            LoginActivity.api = api
        }
    }

    val api = mock<Login.Api>()

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
        onId(R.id.passwordHeader)
                .hasText(R.string.password_header)
    }

    @Test
    fun should_show_password_input() {
        onId(R.id.passwordInput)
                .replaceText("password")
                .hasText("password")
    }

    @Test
    fun should_password_input_has_type_password() {
        onId(R.id.passwordInput)
                .check(matches(withInputType(TYPE_CLASS_TEXT or TYPE_TEXT_VARIATION_PASSWORD)))
    }

    @Test
    fun should_hide_loader_on_start() {
        onId(R.id.loader).isNotDisplayed()
    }

    @Test
    fun shouldShowLoginButton() {
        onId(R.id.loginButton).isDisplayed()
    }

    @Test
    fun should_button_click_with_eamil_and_password_typed_make_api_call() {
        onId(R.id.loginButton).click()
        verify(api).login(any(), any())
    }


}

