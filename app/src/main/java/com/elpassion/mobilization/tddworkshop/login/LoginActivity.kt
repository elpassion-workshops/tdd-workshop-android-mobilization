package com.elpassion.mobilization.tddworkshop.login

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.elpassion.android.view.show
import com.elpassion.mobilization.tddworkshop.R
import kotlinx.android.synthetic.main.login_activity.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        loginButton.setOnClickListener {
            loader.show()
            api.login(emailInput.text.toString(), passwordInput.text.toString())
        }
    }

    companion object {
        lateinit var api: Login.Api
    }
}