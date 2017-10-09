package com.elpassion.mobilization.tddworkshop

import io.reactivex.observers.TestObserver
import org.junit.Assert

fun <T> TestObserver<T>.assertLastValue(expected: T) {
    Assert.assertEquals(expected, values().last())
}

fun <T> TestObserver<T>.assertLastValueThat(predicate: T.() -> Boolean) {
    Assert.assertTrue("${values().last()}\nDoes not match predicate", values().last().predicate())
}