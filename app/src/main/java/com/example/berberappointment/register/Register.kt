package com.example.berberappointment.register

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Register(
    var userName: String? = null,
    var lastName: String? = null,
    var phoneNumber: Long? = null,
    var password: String? = null,
    var isStaff: Boolean? = false,
) {
}