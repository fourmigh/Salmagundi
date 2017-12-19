package org.caojun.utils

import android.text.TextUtils
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.TimeZone
import java.util.Date
import java.util.Calendar

/**
 * Created by CaoJun on 2017/9/27.
 */
object TimeUtils {

    private val LocalTimeZone = TimeZone.getDefault()

    private fun getSimpleDateFormat(dateFormat: String): SimpleDateFormat {
        return getSimpleDateFormat(dateFormat, LocalTimeZone)
    }

    private fun getSimpleDateFormat(dateFormat: String, timeZone: TimeZone): SimpleDateFormat {
        val df = SimpleDateFormat(dateFormat)
        df.timeZone = timeZone
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

    fun getTime(dateFormat: String): String {
        return getTime(dateFormat, LocalTimeZone)
    }

    fun getTime(dateFormat: String, timeZone: TimeZone): String {
        return getTime(dateFormat, timeZone, getTime())
    }

    fun getTime(): Long {
        val date = Date()
        return date.time
    }

    fun getDays(timeStart: Long, timeEnd: Long): Long {
        val time = timeEnd - timeStart
        return time / (1000 * 60 * 60 * 24)
    }

    fun getCalendar(timeZone: TimeZone): Calendar {
        return Calendar.getInstance(timeZone)
    }

    fun getCalendar(): Calendar {
        return getCalendar(LocalTimeZone)
    }

    /**
     * 在当前日期前/后n天
     */
    fun getDate(n: Int): Date {
        val c = Calendar.getInstance()
        c.add(Calendar.DATE, n)
        return c.time
    }

    /**
     * 当前日期是星期几
     * 0为星期日
     */
    fun getWeekdayInt(date: Date): Int {
        val cal = Calendar.getInstance()
        val isFirstSunday = cal.firstDayOfWeek == Calendar.SUNDAY
        cal.time = date
        val weekDay = cal.get(Calendar.DAY_OF_WEEK)
        if (isFirstSunday) {
            return weekDay - 1
        } else if (weekDay == 7) {
            return 0
        }
        return weekDay - 1
    }

    fun getDayOfWeek(date: Date): Int {
        val cal = Calendar.getInstance()
        cal.time = date
        return cal.get(Calendar.DAY_OF_WEEK)
    }

    fun getWeekdayString(date: Date): String {
        val weekDay = TimeUtils.getDayOfWeek(date)
        return DateFormatSymbols.getInstance().shortWeekdays[weekDay]
    }

    fun getTime(dateFormat: String, date: Date): String {
        if (TextUtils.isEmpty(dateFormat)) {
            return ""
        }
        val df = getSimpleDateFormat(dateFormat, LocalTimeZone)
        return df.format(date)
    }

    fun isYesterday(date: Date): Boolean {
        val calendar = Calendar.getInstance()
        calendar.time = date
        val yesterday = Calendar.getInstance()
        yesterday.add(Calendar.DATE, -1)

        return isOneDay(calendar, yesterday)
    }

    fun isTomorrow(date: Date): Boolean {
        val calendar = Calendar.getInstance()
        calendar.time = date
        val tomorrow = Calendar.getInstance()
        tomorrow.add(Calendar.DATE, 1)

        return isOneDay(calendar, tomorrow)
    }

    fun isToday(date: Date): Boolean {
        val calendar = Calendar.getInstance()
        calendar.time = date
        val today = Calendar.getInstance()

        return isOneDay(calendar, today)
    }

    fun isOneDay(d1: Date, d2: Date): Boolean {
        val c1 = Calendar.getInstance()
        c1.time = d1
        val c2 = Calendar.getInstance()
        c2.time = d2
        return isOneDay(c1, c2)
    }

    fun isOneDay(c1: Calendar, c2: Calendar): Boolean {
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) && c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)
    }
}