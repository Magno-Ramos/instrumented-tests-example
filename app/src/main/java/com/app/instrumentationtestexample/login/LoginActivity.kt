package com.app.instrumentationtestexample.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.app.instrumentationtestexample.main.MainActivity
import com.app.instrumentationtestexample.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_login?.setOnClickListener {
            if (validateFields()) {
                btn_login?.isEnabled = false
                progress_bar?.visibility = View.VISIBLE

                val email = edt_email?.text.toString().trim()
                val password = edt_password?.text.toString().trim()
                attemptLogin(email, password)
            }
        }
    }

    private fun attemptLogin(email: String, password: String) {
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun validateFields(): Boolean {
        if (edt_email.text.isNullOrBlank()) {
            edt_email.error = "Invalid Email"
            edt_email.requestFocus()
            return false
        }

        if (edt_password.text.isNullOrBlank()) {
            edt_password.error = "Invalid Password"
            edt_password.requestFocus()
            return false
        }

        return true
    }
}
