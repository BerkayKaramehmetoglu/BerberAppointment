package com.example.berberappointment.berber_shop

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.berberappointment.R
import com.example.berberappointment.login.LoginActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class RVAdapterAppointment(
    private val mContext: Context,
    private var appointmentList: List<String>?,
    private val build: AlertDialog,
    private val berberPhone: Long?,
    private val date: String,
    private val editText: EditText,
    private val appointmentDates: List<LocalDateTime>
) :
    RecyclerView.Adapter<RVAdapterAppointment.AppointmentBtn>() {

    private val firebase = FirebaseDatabase.getInstance()
    private val referenceRegister = firebase.getReference("Register")

    inner class AppointmentBtn(view: View) : RecyclerView.ViewHolder(view) {
        var appointmentBtn: Button = view.findViewById(R.id.appointmentButton)

        init {
            appointmentBtn.setOnClickListener {
                val position = adapterPosition
                val clickedAppointment = appointmentList?.get(position)
                val access = referenceRegister
                    .orderByChild("phoneNumber")
                    .equalTo(LoginActivity.phoneNumber)
                build.dismiss()
                editText.text.clear()
                setAppointmentList(null)
                access.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (userSnapshot in snapshot.children) {
                            val userId = userSnapshot.key

                            val updateInfo = hashMapOf<String, Any>()
                            updateInfo["appointmentBerber"] = berberPhone.toString()
                            updateInfo["appointmentDatetime"] = "$date $clickedAppointment"
                            referenceRegister.child(userId!!).updateChildren(updateInfo)

                            break
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentBtn {
        val design =
            LayoutInflater.from(mContext).inflate(R.layout.appointment_button, parent, false)
        return AppointmentBtn(design)
    }

    override fun getItemCount(): Int {
        return appointmentList?.size ?: 0
    }


    fun markButtonDisable(appointmentBtn: Button) {
        appointmentBtn.isEnabled = false
        appointmentBtn.setTextColor(ContextCompat.getColor(mContext, R.color.red))
        appointmentBtn.setBackgroundColor(
            ContextCompat.getColor(
                mContext,
                androidx.appcompat.R.color.bright_foreground_disabled_material_light
            )
        )
    }

    override fun onBindViewHolder(holder: AppointmentBtn, position: Int) {
        val appointmentTime = getAppointmentTime(position)
        val formatter = DateTimeFormatter.ofPattern("d.M.yyyy HH.mm", Locale("tr", "TR"))
        val now = LocalDateTime.now()

        val localDateTime = LocalDateTime.parse("$date $appointmentTime", formatter)
        if (now > localDateTime) {
            markButtonDisable(holder.appointmentBtn)
        }else{
            for(dateTime in appointmentDates) {
                if(localDateTime.year == dateTime.year && localDateTime.monthValue == dateTime.monthValue && localDateTime.dayOfMonth == dateTime.dayOfMonth && localDateTime.hour == dateTime.hour && localDateTime.minute == dateTime.minute) {
                    markButtonDisable(holder.appointmentBtn)
                }
            }
        }
        holder.appointmentBtn.text = appointmentTime
    }

    private fun getAppointmentTime(position: Int): String {
        return appointmentList?.get(position) ?: ""
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setAppointmentList(appointments: List<String>?) {
        appointmentList = appointments
        notifyDataSetChanged()
    }
}

