package com.example.berberappointment.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.berberappointment.R
import com.example.berberappointment.databinding.ActivityRegisterBinding
import com.example.berberappointment.login.LoginActivity
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import java.security.MessageDigest
import java.util.Base64
import com.example.berberappointment.utils.Utils
import com.example.berberappointment.utils.Utils.Companion.sha256

class RegisterActivity : AppCompatActivity() {
    private lateinit var design: ActivityRegisterBinding
    private val firebase = FirebaseDatabase.getInstance()
    private val referanceRegister = firebase.getReference("Register")

    companion object {
        private const val MIN_PASSWORD_LENGTH = 8
        private const val MIN_PHONE_LENGTH = 11
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        design = DataBindingUtil.setContentView(this@RegisterActivity, R.layout.activity_register)

        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        design.registerBackB.setOnClickListener {
            startActivity(intent)
        }

        design.registerButton.setOnClickListener {
            val registerUserN = design.registerUserN.text.toString()
            val registerUserLN = design.registerLastN.text.toString()
            val registerUserPh = design.registerPhoneN.text.toString()
            val registerUserPs = design.registerPassword.text.toString()
            val registerUserRPs = design.registerRPassword.text.toString()

            val hashedPassword = sha256(registerUserPs) //SHA-256

            if (registerUserN.isNotBlank() && registerUserLN.isNotBlank() && registerUserPh.isNotBlank() && registerUserPs.isNotBlank() && registerUserRPs.isNotBlank()) {

                if (registerUserPs == registerUserRPs) {

                    if (registerUserPs.length < MIN_PASSWORD_LENGTH) {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Invalid Password Length",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else if (registerUserPh.length < MIN_PHONE_LENGTH){
                        Toast.makeText(
                            this@RegisterActivity,
                            "Invalid Phone Number Length",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else {
                        userRegister(
                            registerUserN,
                            registerUserLN,
                            registerUserPh.toLong(),
                            hashedPassword,
                        )
                    }
                    Toast.makeText(this, "Registration Successful ", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@RegisterActivity, "Password Did not Match", Toast.LENGTH_LONG)
                        .show()
                }
            } else {
                Toast.makeText(
                    this@RegisterActivity,
                    "Please Fill in The Required Fields",
                    Toast.LENGTH_LONG
                ).show()
            }
        }


    }

    private fun userRegister(
        userName: String,
        userLastN: String,
        userPhoneN: Long,
        userPassword: String,
    ) {
        val register = Register(userName, userLastN, userPhoneN, userPassword, isStaff = true)

        referanceRegister.push().setValue(register)
    }
}