package com.elpassion.mobilization.tddworkshop.login

import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withInputType
import android.support.test.rule.ActivityTestRule
import android.text.InputType.TYPE_CLASS_TEXT
import android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
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

    private fun shouldShowHeader(id: Int) {
        onText(id).isDisplayed()
    }
}

