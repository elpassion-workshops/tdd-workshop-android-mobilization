package com.elpassion.mobilization.tddworkshop.login

import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withInputType
import android.support.test.rule.ActivityTestRule
import android.text.InputType.TYPE_CLASS_TEXT
import android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
import com.elpassion.android.commons.espresso.*
import com.elpassion.mobilization.tddworkshop.R
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import org.junit.Rule
import org.junit.Test

class LoginActivityTest {

    private val api = mock<Login.Api>()

    @JvmField
    @Rule
    val rule = object : ActivityTestRule<LoginActivity>(LoginActivity::class.java){
        override fun beforeActivityLaunched() {
            LoginActivity.api = api
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
    fun should_show_password_header(){
        onText(R.string.password_header).isDisplayed()
    }

    @Test
    fun should_call_api_after_button_click() {

        val email = "test@mail.com"
        val password = "haxxxPass"

        onId(R.id.emailInput)
                .replaceText("test@mail.com")
        onId(R.id.passwordInput)
                .replaceText("haxxxPass")

        onId(R.id.api_button)
                .click()

        verify(api).login(email, password)
    }
    @Test
    fun should_loader_display_after_button_click(){
        onId(R.id.api_button)
                .click()

        onId(R.id.loadingText).isDisplayed()
    }
}

