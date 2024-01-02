package com.example.berberappointment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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

class RVAdapter(
    private val mContext: Context,
    private val berberList: List<CreateBerber>
) : RecyclerView.Adapter<RVAdapter.BerberShopList>() {
    private lateinit var build: AlertDialog
    private lateinit var adapter: RVAdapterAppointment
    private var firebase = FirebaseDatabase.getInstance()
    private var referenceAppointment = firebase.getReference("CreateBerber")
    private lateinit var appointmentList: ArrayList<CreateBerber>
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

    override fun onBindViewHolder(holder: BerberShopList, position: Int) {
        val berberShop = berberList[position]

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

            val recyclerView =
                designAppointmentPage.findViewById<RecyclerView>(R.id.recyclerViewAppointment)
            recyclerView.setHasFixedSize(true)

            recyclerView.layoutManager =
                LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)

            referenceAppointment.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val appointmentList2 = ArrayList<CreateBerber>()
                    for (child in snapshot.children) {
                        val appointment = child.getValue(CreateBerber::class.java)
                        if (appointment != null) {
                            appointmentList2.add(appointment)
                        }
                    }
                    appointmentList = appointmentList2
                    adapter = RVAdapterAppointment(mContext, appointmentList)
                    recyclerView.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
            build.show()
        }
        holder.shopName.text = berberShop.berberShopN
    }
}
