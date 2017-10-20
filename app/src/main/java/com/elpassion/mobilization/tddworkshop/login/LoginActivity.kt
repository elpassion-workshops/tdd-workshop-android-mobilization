package com.elpassion.mobilization.tddworkshop.login

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.elpassion.mobilization.tddworkshop.R
import kotlinx.android.synthetic.main.login_activity.*

class LoginActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
    }

    companion object {
        lateinit var api: Login.Api
    }

    fun doLogin(view: View) {
        loadingBar.visibility = View.VISIBLE
        api.login(emailInput.text.toString(), passwordInput.text.toString())
    }
}