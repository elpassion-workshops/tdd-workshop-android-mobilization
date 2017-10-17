package com.elpassion.mobilization.tddworkshop

import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withInputType
import android.support.test.rule.ActivityTestRule
import android.text.InputType
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

    @Test
    fun shouldLoginInputHasImeTypeEmail() {
        onId(R.id.loginInput).check(matches(withInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS or InputType.TYPE_CLASS_TEXT)))
    }

    @Test
    fun shouldShowPasswordHeader() {
        onText("password").isDisplayed()
    }
}