package org.caojun.ttclass

import android.content.Context
import org.caojun.ttclass.room.IClass
import org.caojun.ttclass.room.Sign
import org.caojun.ttclass.room.TTCDatabase
import org.caojun.utils.TimeUtils
import org.jetbrains.anko.doAsync
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

    /**
     * 获取课程安排星期N
     */
    fun getScheduleWeekdays(context: Context, iClass: IClass): IntArray? {
        if (iClass.schedule == null) {
            return null
        }
        val weekdays = ArrayList<Int>()
        for (i in iClass.schedule!!.checked.indices) {
            if (!iClass.schedule!!.checked[i]) {
                continue
            }
            if (iClass.schedule!!.time[i][0] == context.getString(R.string.start_time)) {
                continue
            }
            if (iClass.schedule!!.time[i][1] == context.getString(R.string.end_time)) {
                continue
            }
            weekdays.add(i)
        }
        var intArray = IntArray(weekdays.size)
        for (i in weekdays.indices) {
            intArray[i] = weekdays[i]
        }
        return intArray
    }

    fun doSign(context: Context, iClass: IClass?, date: Date, listener: () -> Unit) {
        doAsync {
            val idClass = iClass?.id?:-1
            var list = TTCDatabase.getDatabase(context).getSign().query(idClass)
            val lastSize = list.size
            if (Utilities.dateInSigns(date, list)) {
                return@doAsync
            }
            val sign = Sign()
            sign.idClass = iClass!!.id
            sign.time = date
            TTCDatabase.getDatabase(context).getSign().insert(sign)
            list = TTCDatabase.getDatabase(context).getSign().query(idClass)
            if (list.size - lastSize == 1) {
                //新增一条签到记录
                iClass.reminder --
                if (iClass.reminder < 0) {
                    iClass.reminder = 0
                }
                TTCDatabase.getDatabase(context).getIClass().update(iClass)
            }
            listener()
        }
    }

    fun getSignButtonEnable(context: Context, iClass: IClass): Boolean {
        val scheduleWeekdays = Utilities.getScheduleWeekdays(context, iClass)
        return iClass.reminder > 0 && scheduleWeekdays != null && scheduleWeekdays.isNotEmpty()
    }
}