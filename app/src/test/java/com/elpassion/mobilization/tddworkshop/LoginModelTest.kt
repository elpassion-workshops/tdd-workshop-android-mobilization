package com.elpassion.mobilization.tddworkshop

import org.junit.Assert
import org.junit.Test

class LoginModelTest {

    @Test
    fun shouldInitLoginModel() {
        Assert.assertNotNull(LoginModel())
    }
}

class LoginModel