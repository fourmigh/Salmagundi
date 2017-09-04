package org.caojun.signman.room

import android.arch.persistence.room.TypeConverter
import java.util.Date

/**
 * Created by CaoJun on 2017/9/4.
 */
object DateConverter {
    @TypeConverter
    fun toDate(timestamp: Long): Date {
        return Date(timestamp)
    }

    @TypeConverter
    fun toTimestamp(date: Date): Long {
        return date.time
    }
}