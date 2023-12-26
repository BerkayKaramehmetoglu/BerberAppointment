package com.example.berberappointment.berber

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class CreateBerber(
    var berberName: String? = null,
    var berberLName: String? = null,
    var berberPNumber: Long? = null,
    var berberPassword: String? = null,
    var berberShopN: String? = null,
    var berberShopA: String? = null,
    var isBerber: Boolean? = true
    ) {
}