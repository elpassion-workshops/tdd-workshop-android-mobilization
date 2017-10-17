package com.elpassion.mobilization.tddworkshop.login

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.elpassion.android.view.hide
import com.elpassion.android.view.show
import com.elpassion.mobilization.tddworkshop.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.login_activity.*

class LoginActivity : AppCompatActivity(), Login.View {

    private val loginController by lazy {
        LoginController(api, this, object : Login.Repository {
            override fun save(user: Login.User) = Unit
        }, Schedulers.io(), AndroidSchedulers.mainThread())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        loginButton.setOnClickListener {
            loginController.onLogin(emailInput.text.toString(), passwordInput.text.toString())
        }
    }

    override fun showLoader() {
        loader.show()
    }

    override fun hideLoader() {
        loader.hide()
    }

    override fun openNextScreen() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoginCallError() = Unit

    override fun showEmptyEmailError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showEmptyPasswordError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        lateinit var api: Login.Api
    }
}