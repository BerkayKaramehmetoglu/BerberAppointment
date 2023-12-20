package com.example.berberappointment.register

import android.widget.EditText
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Register(
    var userName: String? = null,
    var lastName: String? = null,
    var phoneNumber: Long? = null,
    var password: Int? = null,
    var rPassword: Int? = null
) {
}