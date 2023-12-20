package com.example.berberappointment.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.berberappointment.R
import com.example.berberappointment.databinding.ActivityLoginBinding
import com.example.berberappointment.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var design: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        design = DataBindingUtil.setContentView(this@LoginActivity,R.layout.activity_login)
        super.onCreate(savedInstanceState)

        val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
        design.accountText.setOnClickListener {
            startActivity(intent)
        }
    }
}