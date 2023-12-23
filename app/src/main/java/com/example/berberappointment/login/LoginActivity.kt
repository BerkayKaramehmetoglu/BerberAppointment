package com.example.berberappointment.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import kotlin.math.log

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
            // Kullanıcının geçerli bir telefon numarası ve şifre girdiğinden emin olun
            val phoneNumber = design.loginPhoneN.text.toString()
            val password = design.loginPassword.text.toString()

            if (phoneNumber.isNotBlank() && password.isNotBlank()) {
                // Giriş işlemi için fonksiyonu çağır
                loginAccess(phoneNumber.toLong(), password.toInt())
            } else {
                Toast.makeText(
                    this@LoginActivity,
                    "Please enter valid phone number and password",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    }

    fun loginAccess(userPhoneN: Long, userPassword: Int) {
        Log.e("aaa", "hata")
        val intentM = Intent(this@LoginActivity, MainActivity::class.java)

        Log.e("aaa", userPhoneN.toString())
        val access = referenceRegister
            .orderByChild("phoneNumber")
            .equalTo(userPhoneN.toDouble())

        access.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.e("aaa", "hata2")
                for (variable in snapshot.children) {
                    Log.e("aaa", "hata3")
                    val register = variable.getValue(Register::class.java)

                    Log.e("aaa", register.toString())

                    if (register != null) {
                        Log.e("aaa", "hata4")
                        if (register.phoneNumber == userPhoneN && register.password == userPassword) {
                            Log.e("aaa", "hata5")
                            Toast.makeText(
                                this@LoginActivity,
                                "Welcome ${register.userName}",
                                Toast.LENGTH_LONG
                            ).show()
                            startActivity(intentM)

                            //backstage ayarla login yaptıktan sonra geri dönemesinler
                        } else {
                            Toast.makeText(this@LoginActivity, "Error", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Error", Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("aaa", "hata1")
            }

        })

    }

    //registerde iki tane şifre saklama
    //sha256 kullanma şifreleri gizleme

}