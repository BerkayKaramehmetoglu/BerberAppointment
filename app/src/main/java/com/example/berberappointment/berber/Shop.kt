package com.example.berberappointment.berber

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Shop(
    var workingHours: Int,
    var shopDesc: String
) {
}