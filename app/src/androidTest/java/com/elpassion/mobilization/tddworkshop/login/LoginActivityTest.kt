package com.elpassion.mobilization.tddworkshop.login

import android.support.test.rule.ActivityTestRule
import com.elpassion.android.commons.espresso.isDisplayed
import com.elpassion.android.commons.espresso.onText
import com.elpassion.mobilization.tddworkshop.R
import org.junit.Rule
import org.junit.Test

class LoginActivityTest {

    @Rule @JvmField
    val rule = ActivityTestRule<LoginActivity>(LoginActivity::class.java)

    @Test
    fun Have_email_header() {
        onText(R.string.email_header).isDisplayed()
    }
}

