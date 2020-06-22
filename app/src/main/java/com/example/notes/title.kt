package com.example.notes

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class title : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_title)
        Handler().postDelayed({
            val main = Intent(this@title, StartActivity::class.java)
            startActivity(main)
            finish()
        }, title.Companion.splash_time_out.toLong())
    }

    companion object {
        private const val splash_time_out = 1000
    }
}