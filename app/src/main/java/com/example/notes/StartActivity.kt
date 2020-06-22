package com.example.notes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class StartActivity : AppCompatActivity() {
    private var btnReg: Button? = null
    private var btnLog: Button? = null
    private var fAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        btnReg = findViewById<View>(R.id.start_reg_btn) as Button
        btnLog = findViewById<View>(R.id.start_log_btn) as Button
        fAuth = FirebaseAuth.getInstance()
        updateUI()
        btnLog!!.setOnClickListener { login() }
        btnReg!!.setOnClickListener { register() }
    }

    private fun register() {
        val regIntent = Intent(this@StartActivity, RegisterActivity::class.java)
        startActivity(regIntent)
    }

    private fun login() {
        val logIntent = Intent(this@StartActivity, LoginActivity::class.java)
        startActivity(logIntent)
    }

    private fun updateUI() {
        if (fAuth!!.currentUser != null) {
            Log.i("StartActivity", "fAuth != null")
            val startIntent = Intent(this@StartActivity, Main::class.java)
            startActivity(startIntent)
            finish()
        } else {
            Log.i("StartActivity", "fAuth == null")
        }
    }
}