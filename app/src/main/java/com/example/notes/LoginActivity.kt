package com.example.notes

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    var inputEmail: TextInputLayout? = null
    var inputPass: TextInputLayout? = null
    private var btnLogIn: Button? = null
    private var fAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        inputEmail = findViewById<View>(R.id.input_log_email) as TextInputLayout
        inputPass = findViewById<View>(R.id.input_log_pass) as TextInputLayout
        btnLogIn = findViewById<View>(R.id.btn_log) as Button
        fAuth = FirebaseAuth.getInstance()
        btnLogIn!!.setOnClickListener {
            val lEmail = inputEmail!!.editText!!.text.toString().trim { it <= ' ' }
            val lPass = inputPass!!.editText!!.text.toString().trim { it <= ' ' }
            if (!TextUtils.isEmpty(lEmail) && !TextUtils.isEmpty(lPass)) {
                logIn(lEmail, lPass)
            }
        }
    }

    private fun logIn(email: String, password: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Logging in, please wait...")
        progressDialog.show()
        fAuth!!.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    progressDialog.dismiss()
                    if (task.isSuccessful) {
                        val mainIntent = Intent(this@LoginActivity, Main::class.java)
                        startActivity(mainIntent)
                        finish()
                        Toast.makeText(this@LoginActivity, "Sign in successful", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@LoginActivity, "ERROR: " + task.exception!!.message, Toast.LENGTH_SHORT).show()
                    }
                }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }
}