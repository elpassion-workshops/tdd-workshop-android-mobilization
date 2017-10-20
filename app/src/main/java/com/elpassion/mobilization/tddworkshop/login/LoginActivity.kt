package com.elpassion.mobilization.tddworkshop.login

import android.app.Activity
import android.os.Bundle
import com.elpassion.mobilization.tddworkshop.R
import kotlinx.android.synthetic.main.login_activity.*

class LoginActivity : Activity() {

    companion object {
        lateinit var api: Login.Api
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        loginButton.setOnClickListener {
            api.login(emailInput.text.toString(), passwordInput.text.toString())
        }
    }
}