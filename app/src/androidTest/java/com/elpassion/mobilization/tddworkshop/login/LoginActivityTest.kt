package com.elpassion.mobilization.tddworkshop.login

import android.support.test.rule.ActivityTestRule
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
                .typeText("test@mail.com")
                .hasText("test@mail.com")
    }
}

