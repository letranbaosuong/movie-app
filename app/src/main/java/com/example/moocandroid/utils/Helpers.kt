package com.example.moocandroid.utils

import android.os.Build
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Helpers {
    companion object {
        fun dateFormatter(dateString: String?): String {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val firstApiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val date = LocalDate.parse(dateString, firstApiFormat)
                val formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy")
                date.format(formatter)
            } else {
                dateString.toString()
            }
        }
    }
}