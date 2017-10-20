package com.elpassion.mobilization.tddworkshop.login

import android.app.Activity
import android.os.Bundle
import android.support.design.widget.Snackbar
import com.elpassion.mobilization.tddworkshop.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.login_activity.*

class LoginActivity : Activity(), Login.View {
    override fun showLoader() {

    }

    override fun openNextScreen() {

    }

    override fun showLoginCallError() {

    }

    override fun showEmptyEmailError() {

    }

    override fun showEmptyPasswordError() {
        Snackbar.make(findViewById(R.id.layout), R.string.empty_email_message, Snackbar.LENGTH_LONG).show()
    }

    override fun hideLoader() {

    }

    companion object {
        lateinit var api: Login.Api
        lateinit var repository: Login.Repository
    }
    val controller: LoginController by lazy {
        LoginController(api, this, repository, Schedulers.io(), AndroidSchedulers.mainThread())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        loginButton.setOnClickListener {
            controller.onLogin(emailInput.text.toString(), passwordInput.text.toString())
        }
    }
}