package org.caojun.signman.utils

import android.text.TextUtils
import com.socks.library.KLog
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by CaoJun on 2017/8/31.
 */
object TimeUtils {
    private val today: Calendar = Calendar.getInstance()

    fun isToday(date: Date): Boolean {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return isToday(calendar)
    }

    fun isToday(date: Calendar): Boolean {
        if (today.get(Calendar.YEAR) != date.get(Calendar.YEAR)) {
            KLog.d("isToday", "year: " + today.get(Calendar.YEAR) + " : " + date.get(Calendar.YEAR))
            return false
        }
        if (today.get(Calendar.MONTH) != date.get(Calendar.MONTH)) {
            KLog.d("isToday", "month: " + today.get(Calendar.MONTH) + " : " + date.get(Calendar.MONTH))
            return false
        }
        if (today.get(Calendar.DAY_OF_MONTH) != date.get(Calendar.DAY_OF_MONTH)) {
            KLog.d("isToday", "day: " + today.get(Calendar.DAY_OF_MONTH) + " : " + date.get(Calendar.DAY_OF_MONTH))
            return false
        }
        return true
    }

    private val LocalTimeZone = TimeZone.getDefault()

    private fun getSimpleDateFormat(dateFormat: String): SimpleDateFormat? {
        return getSimpleDateFormat(dateFormat, LocalTimeZone)
    }

    private fun getSimpleDateFormat(dateFormat: String, timeZone: TimeZone): SimpleDateFormat? {
        try {
            val df = SimpleDateFormat(dateFormat)
            df.timeZone = timeZone
            return df
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun getTime(dateFormat: String, timeZone: TimeZone, time: Long): String? {
        if (TextUtils.isEmpty(dateFormat)) {
            return null
        }
        val df = getSimpleDateFormat(dateFormat, timeZone) ?: return null
        val date = Date(time)
        return df.format(date)
    }

    fun getTime(dateFormat: String, time: Long): String? {
        return getTime(dateFormat, LocalTimeZone, time)
    }

    fun getTime(dateFormat: String): String? {
        return getTime(dateFormat, LocalTimeZone)
    }

    fun getTime(dateFormat: String, timeZone: TimeZone): String? {
        return getTime(dateFormat, timeZone, getTime())
    }

    fun getTime(): Long {
        val date = Date()
        return date.time
    }
}