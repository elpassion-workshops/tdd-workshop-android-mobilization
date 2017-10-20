package com.elpassion.mobilization.tddworkshop.login

import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withInputType
import android.support.test.rule.ActivityTestRule
import android.text.InputType.*
import com.elpassion.android.commons.espresso.*
import com.elpassion.mobilization.tddworkshop.R
import org.junit.Rule
import org.junit.Test

class LoginActivityTest {

    @JvmField
    @Rule
    val rule = ActivityTestRule<LoginActivity>(LoginActivity::class.java)

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
                .replaceText("1234asd")
                .hasText("1234asd")
    }

    @Test
    fun should_password_input_has_type_masked() {
        onId(R.id.passwordInput)
                .check(matches(withInputType(TYPE_CLASS_TEXT or TYPE_TEXT_VARIATION_PASSWORD)))
    }

    @Test
    fun should_show_login_button() {
        onText(R.string.login_button_label).isDisplayed()
    }

}

