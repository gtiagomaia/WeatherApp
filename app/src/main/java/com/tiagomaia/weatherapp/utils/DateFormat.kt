package com.tiagomaia.weatherapp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

object DateFormat{
    private const val PATTERN_FORMAT = "dd.MM.yyyy HH:mm"
    private const val PATTERN_FORMAT_TIME = "HH:mm"

    fun getEpochTimeInHumanReadable(unixTime:Long):String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val instant = Instant.ofEpochSecond(unixTime)
            val formatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT).withZone(ZoneId.systemDefault())
            formatter.format(instant)
        } else {
            val date = Date(unixTime)
            val formatter = SimpleDateFormat(PATTERN_FORMAT, Locale.getDefault())
            formatter.format(date)
        }
    }

    fun getEpochTimeInHumanReadableOnlyTime(unixTime:Long):String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val instant = Instant.ofEpochSecond(unixTime)
            val formatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT_TIME).withZone(ZoneId.systemDefault())
            formatter.format(instant)
        } else {
            val date = Date(unixTime)
            val formatter = SimpleDateFormat(PATTERN_FORMAT_TIME, Locale.getDefault())
            formatter.format(date)
        }
    }
}