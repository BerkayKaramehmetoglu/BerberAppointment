package com.example.berberappointment.utils

import java.security.MessageDigest
import java.util.Base64

class Utils {
    companion object {
        fun sha256(input: String): String {
            val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
            return Base64.getEncoder().encodeToString(bytes)
        }
    }
}