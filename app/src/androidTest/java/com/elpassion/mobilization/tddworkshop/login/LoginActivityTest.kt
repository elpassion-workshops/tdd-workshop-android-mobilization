package com.elpassion.mobilization.tddworkshop.login

import android.support.test.rule.ActivityTestRule
import com.elpassion.android.commons.espresso.*
import com.elpassion.mobilization.tddworkshop.R
import com.elpassion.mobilization.tddworkshop.util.hasHiddenInput
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
                .hasHiddenInput()
                .isDisplayed()
                .replaceText("password")
                .hasText("password")
    }
}