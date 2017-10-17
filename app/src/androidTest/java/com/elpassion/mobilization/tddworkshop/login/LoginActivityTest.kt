package com.elpassion.mobilization.tddworkshop.login

import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.rule.ActivityTestRule
import android.text.InputType
import com.elpassion.android.commons.espresso.*
import com.elpassion.mobilization.tddworkshop.R
import org.junit.Rule
import org.junit.Test

class LoginActivityTest {

    @Rule @JvmField
    val rule = ActivityTestRule<LoginActivity>(LoginActivity::class.java)

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
                .check(ViewAssertions.matches(
                        ViewMatchers.withInputType(
                                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)))
                .isDisplayed()
                .replaceText("password")
                .hasText("password")
    }
}

