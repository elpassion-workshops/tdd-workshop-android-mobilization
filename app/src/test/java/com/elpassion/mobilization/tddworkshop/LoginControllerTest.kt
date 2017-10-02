package com.elpassion.mobilization.tddworkshop

import org.junit.Assert
import org.junit.Test

class LoginControllerTest {

    @Test
    fun shouldInitLoginController() {
        Assert.assertNotNull(LoginController())
    }
}

class LoginController
