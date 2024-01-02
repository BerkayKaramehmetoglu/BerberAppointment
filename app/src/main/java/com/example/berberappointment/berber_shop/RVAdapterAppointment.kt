package com.example.berberappointment.berber_shop

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.berberappointment.R
import com.example.berberappointment.berber.CreateBerber

class RVAdapterAppointment(private val mContext: Context, private val appointmentList: MutableList<CreateBerber>) :
    RecyclerView.Adapter<RVAdapterAppointment.AppointmentBtn>() {

    inner class AppointmentBtn(view: View) : RecyclerView.ViewHolder(view) {
        var appointmentBtn: Button = view.findViewById(R.id.appointmentButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentBtn {
        val design = LayoutInflater.from(mContext).inflate(R.layout.appointment_button, parent, false)
        return AppointmentBtn(design)
    }

    override fun getItemCount(): Int {
        return appointmentList.sumBy { it.appointments?.size ?: 0 }
    }

    override fun onBindViewHolder(holder: AppointmentBtn, position: Int) {
        val appointmentTime = getAppointmentTime(position)

        holder.appointmentBtn.text = appointmentTime
    }

    private fun getAppointmentTime(position: Int): String {
        var currentPosition = position
        for (createBerber in appointmentList) {
            val appointments = createBerber.appointments ?: emptyList()

            if (currentPosition < appointments.size) {
                return appointments[currentPosition]
            } else {
                currentPosition -= appointments.size
            }
        }
        return ""
    }

    fun updateData(newList: List<CreateBerber>) {
        appointmentList.clear()
        appointmentList.addAll(newList)
        notifyDataSetChanged()
    }
}
