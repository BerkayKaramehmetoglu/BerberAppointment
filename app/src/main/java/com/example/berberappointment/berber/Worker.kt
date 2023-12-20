package com.example.berberappointment.berber

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Worker(
    var workerName: String,
    var workerLastN: String,
    var workerPhoneN: Long,
    var workDay: List<String>
) {
}