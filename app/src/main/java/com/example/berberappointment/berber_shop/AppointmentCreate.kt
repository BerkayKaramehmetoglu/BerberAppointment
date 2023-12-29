package com.example.berberappointment.berber_shop

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class AppointmentCreate(
    var startTime: String? = null,
    var endTime: String? = null,
    var cuttingTime: Int? = null,
) {
}