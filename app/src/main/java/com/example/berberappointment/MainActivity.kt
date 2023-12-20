package com.example.berberappointment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.berberappointment.berber.Shaved
import com.example.berberappointment.berber.Shop
import com.example.berberappointment.berber.Worker
import com.example.berberappointment.databinding.ActivityMainBinding
import com.example.berberappointment.databinding.ActivityRegisterBinding
import com.example.berberappointment.register.Register
import com.example.berberappointment.register.RegisterActivity
import com.google.firebase.Firebase
import com.google.firebase.database.database


class MainActivity : AppCompatActivity() {
    private val firebase = Firebase.database
    private lateinit var design: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        design = DataBindingUtil.setContentView(this@MainActivity,R.layout.activity_main)

    }
}