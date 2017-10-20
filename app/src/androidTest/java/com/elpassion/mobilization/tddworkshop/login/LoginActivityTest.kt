package com.elpassion.mobilization.tddworkshop.login

import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withInputType
import android.support.test.rule.ActivityTestRule
import android.text.InputType
import android.text.InputType.TYPE_CLASS_TEXT
import android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
import com.elpassion.android.commons.espresso.*
import com.elpassion.mobilization.tddworkshop.R
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Rule
import org.junit.Test

class LoginActivityTest {

    private val api = mock<Login.Api>()

    @JvmField
    @Rule
    val rule = object : ActivityTestRule<LoginActivity>(LoginActivity::class.java) {
        override fun beforeActivityLaunched() {
            LoginActivity.api = api;
        }
    }

    @Test
    fun should_show_email_header() {
        shouldShowHeader(R.string.email_header)
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
        shouldShowHeader(R.string.password_header)
    }

    @Test
    fun should_show_password_input() {
        onId(R.id.passwordInput).check(matches(withInputType(TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)))
    }

    @Test
    fun should_call_api_login_on_login_button(){
        onId(R.id.emailInput).replaceText("mock@email.com")
        onId(R.id.passwordInput).replaceText("mockPassword")
        onId(R.id.continueButton).click()

        verify(api).login("mock@email.com","mockPassword")
    }

    @Test
    fun should_show_loader_on_login_button(){
        onId(R.id.emailInput).replaceText("mock@email.com")
        onId(R.id.passwordInput).replaceText("mockPassword")
        onId(R.id.continueButton).click()

        onText(R.string.loading).isDisplayed()
    }

    private fun shouldShowHeader(id: Int) {
        onText(id).isDisplayed()
    }
}

