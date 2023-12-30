package com.example.berberappointment.berber

import com.google.firebase.database.IgnoreExtraProperties
import java.time.LocalTime

@IgnoreExtraProperties
data class CreateBerber(
    var berberName: String? = null,
    var berberLName: String? = null,
    var berberPNumber: Long? = null,
    var berberPassword: String? = null,
    var berberShopN: String? = null,
    var berberShopA: String? = null,
    var isBerber: Boolean? = true,
    var appointmentList: MutableList<LocalTime>? = null
) {
    companion object {
        fun appointment(appointmentList: MutableList<LocalTime>?,berberPhoneNumber: Long): CreateBerber {
            return CreateBerber(
                appointmentList = appointmentList,
                berberPNumber = berberPhoneNumber
            )
        }
    }
}