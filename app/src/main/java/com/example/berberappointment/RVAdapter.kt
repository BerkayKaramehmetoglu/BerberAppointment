package com.example.berberappointment

import android.app.DatePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.berberappointment.berber.CreateBerber
import com.example.berberappointment.berber_shop.RVAdapterAppointment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class RVAdapter(
    private val mContext: Context,
    private val berberList: List<CreateBerber>
) : RecyclerView.Adapter<RVAdapter.BerberShopList>() {
    private lateinit var build: AlertDialog
    private val firebase = FirebaseDatabase.getInstance()
    private val referenceRegister = firebase.getReference("Register")
    private val designAppointmentPage =
        LayoutInflater.from(mContext).inflate(R.layout.activity_appointment_page, null)

    inner class BerberShopList(view: View) : RecyclerView.ViewHolder(view) {
        var shopName: TextView
        var button: Button

        init {
            shopName = view.findViewById(R.id.shopName)
            button = view.findViewById(R.id.updateButton)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BerberShopList {
        val design = LayoutInflater.from(mContext).inflate(R.layout.berber_list_card, parent, false)
        return BerberShopList(design)
    }

    override fun getItemCount(): Int {
        return berberList.size
    }

    fun getAllAppointmentDates(berber: String, callback: (List<LocalDateTime>) -> Unit) {
        val appointmentDates: MutableList<LocalDateTime> = mutableListOf()
        val formatter = DateTimeFormatter.ofPattern("d.M.yyyy HH.mm", Locale("tr", "TR"))

        referenceRegister.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    val appointmentBerber = dataSnapshot.child("appointmentBerber").getValue(String::class.java)
                    if(appointmentBerber != null && appointmentBerber != berber) {
                        continue
                    }
                    val appointmentDate =
                        dataSnapshot.child("appointmentDatetime").getValue(String::class.java)
                    appointmentDate?.let {
                        appointmentDates.add(LocalDateTime.parse(it, formatter))
                    }
                }

                callback(appointmentDates)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override fun onBindViewHolder(holder: BerberShopList, position: Int) {
        val berberShop = berberList[position]

        var appointmentDates: List<LocalDateTime>? = null

        val editText = designAppointmentPage.findViewById<EditText>(R.id.editTextDate)
        editText.setOnClickListener {

            val c = Calendar.getInstance()

            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                mContext,
                { view, year, monthOfYear, dayOfMonth ->
                    val dat = (dayOfMonth.toString() + "." + (monthOfYear + 1) + "." + year)
                    editText.setText(dat)

                    getAllAppointmentDates(berberShop.berberPNumber!!.toString()) { dates ->
                        appointmentDates = dates

                        val adapter = RVAdapterAppointment(
                            mContext,
                            berberShop.appointments,
                            build,
                            berberShop.berberPNumber,
                            dat,
                            editText,
                            appointmentDates!!
                        )
                        adapter.setAppointmentList(berberShop.appointments)

                        val recyclerView =
                            designAppointmentPage.findViewById<RecyclerView>(R.id.recyclerViewAppointment)
                        recyclerView.setHasFixedSize(true)

                        recyclerView.layoutManager =
                            LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)

                        recyclerView.adapter = adapter
                    }
                },
                year,
                month,
                day
            )
            datePickerDialog.datePicker.minDate = System.currentTimeMillis()
            datePickerDialog.show()
        }

        holder.button.setOnClickListener {
            val builder = AlertDialog.Builder(mContext)

            val designAppointmentButton =
                LayoutInflater.from(mContext).inflate(R.layout.appointment_button, null)

            val parent = designAppointmentPage.parent as? ViewGroup
            parent?.removeView(designAppointmentPage)

            builder.setView(designAppointmentPage)
            build = builder.create()

            val shopNameA =
                designAppointmentPage.findViewById<TextView>(R.id.appointmentShopName)
            val shopNumber =
                designAppointmentPage.findViewById<TextView>(R.id.appointmentNumber)
            val shopAddress =
                designAppointmentPage.findViewById<TextView>(R.id.appointmentAddress)
            val appointmentButton =
                designAppointmentButton.findViewById<Button>(R.id.appointmentButton)

            appointmentButton.text = berberShop.appointments.toString()
            shopNameA.text = berberShop.berberShopN.toString()
            shopNumber.text = berberShop.berberPNumber.toString()
            shopAddress.text = berberShop.berberShopA.toString()

            build.show()
        }

        holder.shopName.text = berberShop.berberShopN
    }

}

