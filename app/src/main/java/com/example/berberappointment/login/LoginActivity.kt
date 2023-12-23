package com.example.berberappointment.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.berberappointment.MainActivity
import com.example.berberappointment.R
import com.example.berberappointment.databinding.ActivityLoginBinding
import com.example.berberappointment.register.Register
import com.example.berberappointment.register.RegisterActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.example.berberappointment.utils.Utils

class LoginActivity : AppCompatActivity() {
    private lateinit var design: ActivityLoginBinding
    private val firebase = FirebaseDatabase.getInstance()
    private val referenceRegister = firebase.getReference("Register")
    override fun onCreate(savedInstanceState: Bundle?) {
        design = DataBindingUtil.setContentView(this@LoginActivity, R.layout.activity_login)
        super.onCreate(savedInstanceState)

        val intentR = Intent(this@LoginActivity, RegisterActivity::class.java)

        design.accountText.setOnClickListener {
            startActivity(intentR)
        }

        design.loginButton.setOnClickListener {
            val phoneNumber = design.loginPhoneN.text.toString()
            val password = design.loginPassword.text.toString()

            if (phoneNumber.isNotBlank() && password.isNotBlank()) {
                loginAccess(phoneNumber.toLong(), password)
            } else {
                Toast.makeText(
                    this@LoginActivity,
                    "Please Enter Valid Phone Number and Password",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun loginAccess(userPhoneN: Long, userPassword: String) {
        val intentM = Intent(this@LoginActivity, MainActivity::class.java)
        val access = referenceRegister
            .orderByChild("phoneNumber")
            .equalTo(userPhoneN.toDouble())

        access.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (variable in snapshot.children) {
                    val register = variable.getValue(Register::class.java)

                    if (register != null) {
                        if (register.phoneNumber == userPhoneN && register.password == Utils.sha256(
                                userPassword
                            )
                        ) {
                            Toast.makeText(
                                this@LoginActivity,
                                "Welcome ${register.userName}",
                                Toast.LENGTH_LONG
                            ).show()
                            startActivity(intentM)
                            finish()
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "Password and Phone Number Do not Match",
                                Toast.LENGTH_LONG
                            ).show()
                            break
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Error", Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@LoginActivity, "Failed to Load Data", Toast.LENGTH_LONG).show()
            }

        })

    }


}