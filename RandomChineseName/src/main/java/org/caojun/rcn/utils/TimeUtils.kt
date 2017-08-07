package org.caojun.rcn.utils

import android.text.TextUtils
import java.text.SimpleDateFormat
import java.util.TimeZone
import java.util.Date
import java.util.Calendar

/**
 * Created by CaoJun on 2017/8/7.
 */
private val LocalTimeZone = TimeZone.getDefault()

private fun getSimpleDateFormat(dateFormat: String): SimpleDateFormat {
    return getSimpleDateFormat(dateFormat, LocalTimeZone)
}

private fun getSimpleDateFormat(dateFormat: String, timeZone: TimeZone): SimpleDateFormat {
    val df = SimpleDateFormat(dateFormat)
    df.setTimeZone(timeZone)
    return df
}

fun getTime(dateFormat: String, timeZone: TimeZone, time: Long): String {
    if (TextUtils.isEmpty(dateFormat)) {
        return ""
    }
    val df = getSimpleDateFormat(dateFormat, timeZone)
    val date = Date(time)
    return df.format(date)
}

fun getTime(dateFormat: String, time: Long): String {
    return getTime(dateFormat, LocalTimeZone, time)
}

fun getTime(dateFormat: String): String? {
    return getTime(dateFormat, LocalTimeZone)
}

fun getTime(dateFormat: String, timeZone: TimeZone): String {
    return getTime(dateFormat, timeZone, getTime())
}

fun getTime(): Long {
    val date = Date()
    return date.time
}

fun getDays(timeEnd: Long, timeStart: Long): Long {
    val time = timeEnd - timeStart
    return time / (1000 * 60 * 60 * 24)
}

fun getCalendar(timeZone: TimeZone): Calendar {
    return Calendar.getInstance(timeZone)
}

fun getCalendar(): Calendar {
    return getCalendar(LocalTimeZone)
}