package com.example.berberappointment.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.berberappointment.R
import com.example.berberappointment.databinding.ActivityRegisterBinding
import com.example.berberappointment.login.LoginActivity
import com.google.firebase.Firebase
import com.google.firebase.database.database

class RegisterActivity : AppCompatActivity() {
    private lateinit var design: ActivityRegisterBinding
    private val firebase = Firebase.database
    private val referanceRegister = firebase.getReference("Register")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        design = DataBindingUtil.setContentView(this@RegisterActivity, R.layout.activity_register)

        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        design.registerBackB.setOnClickListener{
         startActivity(intent)
        }

    }

}