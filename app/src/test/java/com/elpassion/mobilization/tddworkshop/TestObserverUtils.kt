package com.elpassion.mobilization.tddworkshop

import io.reactivex.observers.TestObserver
import org.junit.Assert

fun <T> TestObserver<T>.assertLastValue(expected: T) {
    Assert.assertEquals(expected, values().last())
}