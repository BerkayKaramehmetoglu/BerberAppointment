package com.example.berberappointment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.berberappointment.berber.Shaved
import com.example.berberappointment.customer.Customer
import com.google.firebase.Firebase
import com.google.firebase.database.database


class MainActivity : AppCompatActivity() {
    private val firebase = Firebase.database
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        referenceBerber("Tas Kafa", 50)
        referenceCustomer("Metin","Özel")


    }

    fun referenceBerber(shavedType: String, shavedPrice: Int) {
        val referanceBerber = firebase.getReference("Berber")
        val berber = Shaved(shavedType, shavedPrice)
        referanceBerber.push().setValue(berber)
    }

    fun referenceCustomer(isim: String, soyad: String) {
        val referanceCustomer = firebase.getReference("Customer")
        val customer = Customer(isim,soyad)
        referanceCustomer.push().setValue(customer)
    }
}