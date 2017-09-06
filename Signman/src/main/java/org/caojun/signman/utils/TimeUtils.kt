package org.caojun.signman.utils

import com.socks.library.KLog
import java.util.Date
import java.util.Calendar


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
}