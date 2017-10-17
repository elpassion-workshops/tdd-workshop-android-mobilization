package com.elpassion.mobilization.tddworkshop

import android.support.test.rule.ActivityTestRule
import com.elpassion.android.commons.espresso.*
import org.junit.Rule
import org.junit.Test

class LoginActivityTest {
    @JvmField
    @Rule
    val rule = ActivityTestRule<LoginActivity>(LoginActivity::class.java)

    @Test
    fun shouldShowLoginHeader() {
        onText("login").isDisplayed()
    }

    @Test
    fun shouldShowLoginInput() {
        onId(R.id.loginInput).isDisplayed()
    }

    @Test
    fun shouldLoginInputBeWritable() {
        onId(R.id.loginInput).typeText("my email").hasText("my email")
    }
}