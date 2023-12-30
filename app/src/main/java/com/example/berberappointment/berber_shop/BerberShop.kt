package com.example.berberappointment.berber_shop

import Dexter
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.example.berberappointment.R
import com.example.berberappointment.berber.CreateBerber
import com.example.berberappointment.databinding.ActivityBerberShopBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class BerberShop : AppCompatActivity() {
    private lateinit var design: ActivityBerberShopBinding
    private val firebase = FirebaseDatabase.getInstance()
    private val referenceCreateBerber = firebase.getReference("CreateBerber")
    private lateinit var myDexter: Dexter
    private lateinit var appointmentListLocal: MutableList<LocalTime>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        design = DataBindingUtil.setContentView(this@BerberShop, R.layout.activity_berber_shop)

        myDexter = Dexter(this@BerberShop, design.shopImageBerber)

        berberShop()

        design.shopImageBerber.setOnClickListener {
            myDexter.pickImage.launch("image/*")
        }

        //loadImageFromFirebaseStorage()

        design.createBtn.setOnClickListener {
            val startTimeText = design.startTime.text.toString()
            val endTimeText = design.endTime.text.toString()
            val cuttingTime = design.cuttingTime.text.toString()

            if (startTimeText.isNotBlank() && endTimeText.isNotBlank()) {
                if (cuttingTime.toInt() > 0) {
                    createAppointment(startTimeText, endTimeText, cuttingTime.toInt())
                } else {
                    Toast.makeText(this@BerberShop, "Enter Valid Cutting Time", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(
                    this@BerberShop,
                    "Please Enter Valid Start Time and End Time",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun berberShop() {
        val access = referenceCreateBerber
            .orderByChild("berberPNumber")
            .equalTo(5555555557.toDouble())

        val pickImage =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let { selectedImage ->
                    myDexter.uploadImageToFirebaseStorage(selectedImage)
                }
            }
        design.shopImageBerber.setOnClickListener {
            pickImage.launch("image/*")
        }

        access.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (variable in snapshot.children) {
                        val createBerber = variable.getValue(CreateBerber::class.java)
                        if (createBerber != null) {
                            design.berberShopToolbar.title = createBerber.berberShopN
                            design.berberShopToolbar.subtitle =
                                createBerber.berberPNumber.toString()
                            design.berberName.text = createBerber.berberName
                            design.berberLastName.text = createBerber.berberLName
                            design.berberShopAddress.text = createBerber.berberShopA
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    /*private fun loadImageFromFirebaseStorage() {
        val storageRef = FirebaseStorage.getInstance().getReference("shopImage")
        storageRef.child("fbe4e760-2dc8-45a2-bbc2-cd4db5bfa40b.jpg").downloadUrl.addOnSuccessListener { uri ->
            Picasso.get().load(uri).into(design.shopImageBerber)
        }.addOnFailureListener { _ ->
            Toast.makeText(this@BerberShop, "Fotoğraf yüklenirken bir hata oluştu", Toast.LENGTH_SHORT).show()
        }
    }*/

    //berber randevu alanlar
    //berbe telefonla girildiğinde istenilen sayfaya atsın
    //her berberin seçtiği resime göre dükkan resmi getirme

    private fun createAppointment(
        startTime: String,
        endTime: String,
        cuttingTime: Int,
    ) {
        val formatter = DateTimeFormatter.ofPattern("HH.mm")

        val startTimes = LocalTime.parse(startTime, formatter)
        val endTimes = LocalTime.parse(endTime, formatter)
        val cuttingTimes = cuttingTime.toString().toLong()

        var currentAppointmentTime = startTimes
        val appointmentList2 = mutableListOf<LocalTime>()

        while (currentAppointmentTime.isBefore(endTimes)) {
            appointmentList2.add(currentAppointmentTime)
            currentAppointmentTime = currentAppointmentTime.plusMinutes(cuttingTimes)
        }

        appointmentListLocal = appointmentList2

        val appointmentCreateList = CreateBerber.appointment(appointmentListLocal,5333333333)
        referenceCreateBerber.push().setValue(appointmentCreateList)

        Toast.makeText(this@BerberShop, "Appointment Created", Toast.LENGTH_SHORT).show()
    }
}