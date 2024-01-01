package com.example.berberappointment.berber_shop

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
import java.util.Locale

class BerberShop : AppCompatActivity() {

    private lateinit var design: ActivityBerberShopBinding
    private val firebase = FirebaseDatabase.getInstance()
    private val referenceCreateBerber = firebase.getReference("CreateBerber")
    private lateinit var appointmentListLocal: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        design = DataBindingUtil.setContentView(this@BerberShop, R.layout.activity_berber_shop)

        setClickListeners()
        loadDataFromFirebase()
    }

    private fun setClickListeners() {
        design.createBtn.setOnClickListener {
            val startTimeText = design.startTime.text.toString()
            val endTimeText = design.endTime.text.toString()
            val cuttingTime = design.cuttingTime.text.toString()

            if (startTimeText.isNotBlank() && endTimeText.isNotBlank()) {
                if (cuttingTime.toInt() > 0) {
                    val appointmentList =
                        createAppointment(startTimeText, endTimeText, cuttingTime.toInt())
                    updateCreateBerberAppointment(appointmentList)
                } else {
                    showToast("Enter Valid Cutting Time")
                }
            } else {
                showToast("Please Enter Valid Start Time and End Time")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@BerberShop, message, Toast.LENGTH_SHORT).show()
    }

    private fun createAppointment(
        startTime: String,
        endTime: String,
        cuttingTime: Int
    ): AppointmentBerber {
        val formatter = DateTimeFormatter.ofPattern("HH.mm", Locale("tr", "TR"))

        val startTimes = LocalTime.parse(startTime, formatter)
        val endTimes = LocalTime.parse(endTime, formatter)
        val cuttingTimes = cuttingTime.toLong()

        var currentAppointmentTime = startTimes
        val appointmentList2 = mutableListOf<String>()

        while (currentAppointmentTime.isBefore(endTimes)) {
            appointmentList2.add(currentAppointmentTime.format(formatter))
            currentAppointmentTime = currentAppointmentTime.plusMinutes(cuttingTimes)
        }

        appointmentListLocal = appointmentList2

        return AppointmentBerber(appointmentListLocal)
    }

    private fun updateCreateBerberAppointment(appointmentList: AppointmentBerber) {
        val berberPNumber = 5333333333.toDouble()

        val query = referenceCreateBerber.orderByChild("berberPNumber").equalTo(berberPNumber)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (variable in snapshot.children) {
                        variable.ref.child("appointments").setValue(appointmentList.appointmentList)
                        showToast("Appointment Added")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
    private fun loadDataFromFirebase() {
        val berberPNumber = 5333333333.toDouble()

        val query = referenceCreateBerber.orderByChild("berberPNumber").equalTo(berberPNumber)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (variable in snapshot.children) {
                        val createBerber = variable.getValue(CreateBerber::class.java)
                        createBerber?.let { updateUI(it) }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun updateUI(createBerber: CreateBerber) {
        // UI elemanlarını güncelle
        design.berberShopToolbar.title = createBerber.berberShopN.toString()
        design.berberShopToolbar.subtitle = createBerber.berberPNumber.toString()
        design.berberName.text = createBerber.berberName.toString()
        design.berberLastName.text = createBerber.berberLName.toString()
        design.berberShopAddress.text = createBerber.berberShopA.toString()
    }
}
