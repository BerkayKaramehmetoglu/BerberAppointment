package com.example.berberappointment.berber_shop

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.berberappointment.R
import com.example.berberappointment.register.Register

class RVAdapterSetAppo(
    private var mContext: Context, private var setAppo: List<Register>
) : RecyclerView.Adapter<RVAdapterSetAppo.DesignGet>() {

    inner class DesignGet(view: View) : RecyclerView.ViewHolder(view) {

        var appoName: TextView
        var appoLastName: TextView
        var appoPhoneNumber: TextView
        var appoDateTime: TextView

        init {
            appoName = view.findViewById(R.id.appoName)
            appoLastName = view.findViewById(R.id.appoLastName)
            appoPhoneNumber = view.findViewById(R.id.appoPhoneNumber)
            appoDateTime = view.findViewById(R.id.appoDateTime)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DesignGet {
        val designAppo = LayoutInflater.from(mContext).inflate(R.layout.user_appointment,parent,false)
        return DesignGet(designAppo)
    }

    override fun getItemCount(): Int {
        return setAppo.size
    }

    override fun onBindViewHolder(holder: DesignGet, position: Int) {
        val appoList = setAppo[position]

        holder.appoName.text = appoList.userName
        holder.appoLastName.text = appoList.lastName
        holder.appoPhoneNumber.text = appoList.phoneNumber.toString()
        holder.appoDateTime.text = appoList.appointmentDatetime
    }
}