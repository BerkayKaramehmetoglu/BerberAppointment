package com.example.berberappointment.berber_shop

import com.google.firebase.database.IgnoreExtraProperties
import java.time.LocalTime

@IgnoreExtraProperties
data class AppointmentBerber(
    val appointmentList: List<String>? = null
) {
}