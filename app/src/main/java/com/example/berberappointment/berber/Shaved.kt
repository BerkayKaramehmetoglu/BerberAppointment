package com.example.berberappointment.berber

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Shaved(
    var shavedType: String,
    var shavedPrice: Int,
) {
}