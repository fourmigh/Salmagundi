package org.caojun.ttclass.room

import android.arch.persistence.room.TypeConverter
import android.text.TextUtils
import java.util.*

/**
 * Created by CaoJun on 2017-12-12.
 */
class DataConverter {

    private val SEPARATOR = "<>"

    @TypeConverter
    fun schedule2String(schedule: Schedule?): String {
        if (schedule == null) {
            return ""
        }
        val sb = StringBuffer()
        sb.append(schedule.idClass.toString()).append(SEPARATOR)
        for (i in schedule.checked.indices) {
            sb.append(schedule.checked[i].toString()).append(SEPARATOR)
        }
        for (i in schedule.time.indices) {
            for (j in schedule.time[i].indices) {
                sb.append(schedule.time[i][j]).append(SEPARATOR)
            }
        }
        return sb.toString()
    }

    @TypeConverter
    fun string2Schedule(string: String): Schedule {
        val schedule = Schedule()
        if (TextUtils.isEmpty(string) || string.indexOf(SEPARATOR) < 0) {
            return schedule
        }
        var index = 0
        val strings = string.split(SEPARATOR)
        schedule.idClass = strings[index++].toLong()
        for (i in schedule.checked.indices) {
            schedule.checked[i] = strings[index++].toBoolean()
        }
        for (i in schedule.time.indices) {
            for (j in schedule.time[i].indices) {
                schedule.time[i][j] = strings[index++]
            }
        }
        return schedule
    }

    @TypeConverter
    fun date2Long(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun long2Date(date: Long): Date {
        return Date(date)
    }
}