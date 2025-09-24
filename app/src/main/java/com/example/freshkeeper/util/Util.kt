package com.example.freshkeeper.util

import androidx.compose.ui.graphics.Color
import com.example.freshkeeper.model.db.Grocery
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

class Util {

    fun expiryColorValidator(item: Grocery): Color {
        val daysLeft = expiryDaysLeft(item) ?: return Color.Gray // no expiry

        return when {
            daysLeft <= 0 -> Color(0xFFD32F2F) // Expired = Red
            daysLeft <= 3 -> Color(0xFFFFA000) // Soon to expire = Orange
            else -> Color(0xFF388E3C)          // Safe = Green
        }
    }

    fun expiryDaysLeft(item: Grocery): Int? {
        val expiryMillis = item.expiryDate ?: return null

        // Convert expiry millis to LocalDate in UTC
        val expiryDate = Instant.ofEpochMilli(expiryMillis)
            .atZone(ZoneOffset.UTC)
            .toLocalDate()

        val today = LocalDate.now(ZoneOffset.UTC)

        // Days between today and expiryDate
        return ChronoUnit.DAYS.between(today, expiryDate).toInt()
    }
}