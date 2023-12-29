package com.example.berberappointment

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.berberappointment.berber.CreateBerber
import de.hdodenhof.circleimageview.CircleImageView


class RVAdapter(private val mContext: Context, private val berberList: List<CreateBerber>) :
    RecyclerView.Adapter<RVAdapter.BerberShopList>() {
    private lateinit var build: AlertDialog

    inner class BerberShopList(view: View) : RecyclerView.ViewHolder(view) {
        var shopImage: CircleImageView
        var shopName: TextView
        var button: Button

        init {
            shopImage = view.findViewById(R.id.shopImage)
            shopName = view.findViewById(R.id.shopName)
            button = view.findViewById(R.id.appointmentB)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVAdapter.BerberShopList {
        val design = LayoutInflater.from(mContext).inflate(R.layout.berber_list_card, parent, false)

        return BerberShopList(design)
    }

    override fun getItemCount(): Int {
        return berberList.size
    }

    override fun onBindViewHolder(holder: RVAdapter.BerberShopList, position: Int) {
        val berberShop = berberList[position]

        holder.button.setOnClickListener {
            val builder = AlertDialog.Builder(mContext)

            val designAppointmentPage =
                LayoutInflater.from(mContext).inflate(R.layout.activity_appointment_page, null)

            builder.setView(designAppointmentPage)

            build = builder.create()

            val shopNameA =
                designAppointmentPage.findViewById<TextView>(R.id.appointmentShopName)
            val shopNumber =
                designAppointmentPage.findViewById<TextView>(R.id.appointmentNumber)
            val shopAddress =
                designAppointmentPage.findViewById<TextView>(R.id.appointmentAdress)
            shopNameA.text = berberShop.berberShopN.toString()
            shopNumber.text = berberShop.berberPNumber.toString()
            shopAddress.text = berberShop.berberShopA.toString()

            build.show()

        }
        holder.shopName.text = berberShop.berberShopN

    }
}