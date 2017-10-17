package com.elpassion.mobilization.tddworkshop.util

import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import android.text.InputType

fun ViewInteraction.hasHiddenInput() =
        check(ViewAssertions.matches(
                ViewMatchers.withInputType(
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)))