package com.example.notes

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private var btnReg: Button? = null
    private var inName: TextInputLayout? = null
    private var inEmail: TextInputLayout? = null
    private var inPass: TextInputLayout? = null
    private var fAuth: FirebaseAuth? = null
    private var fUsersDatabase: DatabaseReference? = null
    private var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        btnReg = findViewById<View>(R.id.btn_reg) as Button
        inName = findViewById<View>(R.id.input_reg_name) as TextInputLayout
        inEmail = findViewById<View>(R.id.input_reg_email) as TextInputLayout
        inPass = findViewById<View>(R.id.input_reg_pass) as TextInputLayout
        fAuth = FirebaseAuth.getInstance()
        fUsersDatabase = FirebaseDatabase.getInstance().reference.child("Users")
        btnReg!!.setOnClickListener {
            val uname = inName!!.editText!!.text.toString().trim { it <= ' ' }
            val uemail = inEmail!!.editText!!.text.toString().trim { it <= ' ' }
            val upass = inPass!!.editText!!.text.toString().trim { it <= ' ' }
            if (uname.isEmpty() || uemail.isEmpty() || upass.isEmpty()) {
                checknullinput(uname, uemail, upass)
            } else {
                registerUser(uname, uemail, upass)
            }
            //  registerUser(uname, uemail, upass);
        }
    }

    private fun checknullinput(name: String, email: String, pass: String) {
        if (name.isEmpty()) {
            Log.e("test input ", " uname done check")
            inName!!.hint = " Username cannot be empty."
        }
        if (email == "") {
            Log.e("test input ", " uname done check")
            inEmail!!.hint = " Email cannot be empty."
        }
        if (pass == "") {
            Log.e("test input ", " uname done check")
            inPass!!.hint = " Password cannot be empty."
        }
    }

    private fun registerUser(name: String, email: String, password: String) {
        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("Processing your request, please wait...")
        progressDialog!!.show()
        fAuth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        fUsersDatabase!!.child(fAuth!!.currentUser!!.uid)
                                .child("basic").child("name").setValue(name)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        progressDialog!!.dismiss()
                                        val mainIntent = Intent(this@RegisterActivity, Main::class.java)
                                        startActivity(mainIntent)
                                        finish()
                                        Toast.makeText(this@RegisterActivity, "User created!", Toast.LENGTH_SHORT).show()
                                    } else {
                                        progressDialog!!.dismiss()
                                        Toast.makeText(this@RegisterActivity, "ERROR : " + task.exception!!.message, Toast.LENGTH_SHORT).show()
                                    }
                                }
                    } else {
                        progressDialog!!.dismiss()
                        Toast.makeText(this@RegisterActivity, "ERROR: " + task.exception!!.message, Toast.LENGTH_SHORT).show()
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