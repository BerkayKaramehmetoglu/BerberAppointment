package com.example.berberappointment.register

import android.text.BoringLayout
import com.example.berberappointment.berber.CreateBerber
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Register(
    var userName: String? = null,
    var lastName: String? = null,
    var phoneNumber: Long? = null,
    var password: String? = null,
    var isStaff: Boolean? = false,
    var isBerber: Boolean? = false,
    var appointmentDatetime: String? = null,
    var appointmentBerber: String? = null
) {
    companion object {
        fun fromCreateBerber(createBerber: CreateBerber): Register {
            return Register(
                userName = createBerber.berberName,
                lastName = createBerber.berberLName,
                phoneNumber = createBerber.berberPNumber,
                password = createBerber.berberPassword,
                isBerber = createBerber.isBerber,
            )
        }
    }
}