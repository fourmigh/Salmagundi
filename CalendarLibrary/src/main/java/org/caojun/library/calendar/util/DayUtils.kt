package org.caojun.library.calendar.util

import org.caojun.library.calendar.model.CalendarDay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Created by CaoJun on 2017/8/23.
 */
object DayUtils {
    val WEEKS_IN_YEAR = 12
    val DAY_IN_WEEK = 7

    fun calculateWeekCount(startDay: CalendarDay, endDay: CalendarDay): Int {
        val x = endDay.getTime() - startDay.getTime()
        val days = x.toInt() / (1000 * 60 * 60 * 24) + 1
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = startDay.getTime()
        val startDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        calendar.timeInMillis = endDay.getTime()
        val endDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        var week = days / DAY_IN_WEEK + 1
        if (endDayOfWeek < startDayOfWeek) {
            week++
        }
        return week
    }

    fun calculateMonthCount(startDay: CalendarDay, endDay: CalendarDay): Int {
        if (startDay.getYear() == endDay.getYear()) {
            return endDay.getMonth() - startDay.getMonth() + 1
        } else if (startDay.getYear() < endDay.getYear()) {
            return (endDay.getYear() - startDay.getYear() - 1) * 12 + (12 - startDay.getMonth()) + endDay.getMonth() + 1
        }
        return 0
    }

    fun calculateMonthPosition(startDay: CalendarDay, positionDay: CalendarDay): Int {
        if (startDay.getYear() == positionDay.getYear()) {
            return positionDay.getMonth() - startDay.getMonth()
        } else if (startDay.getYear() < positionDay.getYear()) {
            return (positionDay.getYear() - startDay.getYear() - 1) * 12 + (12 - startDay.getMonth()) + positionDay.getMonth()
        }
        return 0
    }

    fun calculateFirstShowDay(startDay: CalendarDay): CalendarDay {
        val day = startDay.getCalendar().get(Calendar.DAY_OF_WEEK)
        var calendar = Calendar.getInstance()
        calendar.timeInMillis = startDay.getTime()
        calendar.roll(Calendar.DAY_OF_YEAR, -day + 1)
        return CalendarDay(calendar)
    }

    fun calculateDayPosition(startDay: CalendarDay, day: CalendarDay): Int {
        val x = day.getTime() - startDay.getTime()
        return x.toInt() / (1000 * 60 * 60 * 24)
    }

    fun formatEnglishTime(times: Long): String {
        val df1 = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)
        return df1.format(Date(times))
    }

    fun getDaysInMonth(month: Int, year: Int): Int {
        when (month) {
            Calendar.JANUARY, Calendar.MARCH, Calendar.MAY, Calendar.JULY, Calendar.AUGUST, Calendar.OCTOBER, Calendar.DECEMBER -> return 31
            Calendar.APRIL, Calendar.JUNE, Calendar.SEPTEMBER, Calendar.NOVEMBER -> return 30
            Calendar.FEBRUARY -> return if (year % 4 == 0) 29 else 28
            else -> throw IllegalArgumentException("Invalid Month")
        }
    }
}