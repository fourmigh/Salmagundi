package org.caojun.library.calendar.model

import android.text.TextUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Created by CaoJun on 2017/8/23.
 */
class CalendarDay {
    private var calendar: Calendar? = null

    private var day: Int = 0
    private var month: Int = 0
    private var year: Int = 0

    constructor() {
        setTime(System.currentTimeMillis())
    }

    constructor(year: Int, month: Int, day: Int) {
        setDay(year, month - 1, day)
    }

    constructor(timeInMillis: Long) {
        setTime(timeInMillis)
    }

    constructor(calendar: Calendar) {
        if (this.calendar == null) {
            this.calendar = calendar
        }
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)
    }

    fun getCalendar(): Calendar {
        return calendar!!
    }

    private fun setTime(timeInMillis: Long) {
        if (calendar == null) {
            calendar = Calendar.getInstance()
        }
        calendar!!.timeInMillis = timeInMillis
        month = this.calendar!!.get(Calendar.MONTH)
        year = this.calendar!!.get(Calendar.YEAR)
        day = this.calendar!!.get(Calendar.DAY_OF_MONTH)
    }

    fun getTime(): Long {
        if (calendar == null) {
            calendar = Calendar.getInstance()
        }
        calendar!!.set(year, month, day)
        return calendar!!.timeInMillis
    }

    fun set(calendarDay: CalendarDay) {
        year = calendarDay.year
        month = calendarDay.month
        day = calendarDay.day
    }

    fun setDay(year: Int, month: Int, day: Int) {
        this.year = year
        this.month = month
        this.day = day
        if (calendar == null) {
            calendar = Calendar.getInstance()
        }
        calendar!!.set(year, month, day)
    }

    fun getDefaultMonth(): Int {
        return month
    }

    fun getYear(): Int {
        return year
    }

    fun getMonth(): Int {
        return month
    }

    fun getDay(): Int {
        return day
    }

    fun setStringDay(ymd: String) {
        if (TextUtils.isEmpty(ymd)) return
        val strings = ymd.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        year = Integer.valueOf(strings[0])!!
        month = Integer.valueOf(strings[1])!! - 1
        day = Integer.valueOf(strings[2])!!
        if (calendar == null) {
            calendar = Calendar.getInstance()
        }
        calendar!!.set(year, month, day)
    }

    fun getDayString(): String {
        val df1 = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        return df1.format(Date(getTime()))
    }
}