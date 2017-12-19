package org.caojun.ttclass

import android.content.Context
import org.caojun.ttclass.room.Sign
import org.caojun.utils.TimeUtils
import java.util.*

/**
 * Created by CaoJun on 2017-12-19.
 */
object Utilities {
    fun getWeekday(context: Context, date: Date): String {
        if (TimeUtils.isYesterday(date)) {
            return context.getString(R.string.yesterday)
        }
        if (TimeUtils.isToday(date)) {
            return context.getString(R.string.today)
        }
        if (TimeUtils.isTomorrow(date)) {
            return context.getString(R.string.tomorrow)
        }
        return TimeUtils.getWeekdayString(date)
    }

    fun dateInSigns(date: Date, signs: List<Sign>): Boolean {
        return signs.indices.any { TimeUtils.isOneDay(date, signs[it].time) }
    }
}