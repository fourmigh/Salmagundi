package org.caojun.signman.room

import android.arch.persistence.room.TypeConverter
import android.graphics.drawable.Drawable
import android.text.TextUtils
import org.caojun.signman.utils.DrawableUtils
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by CaoJun on 2017/9/5.
 */
class DataConverter {
//    @TypeConverter
//    fun toDate(timestamp: Long): Date {
//        return Date(timestamp)
//    }
//
//    @TypeConverter
//    fun toTimestamp(date: Date): Long {
//        return date.time
//    }

    @TypeConverter
    fun toByteArray(drawable: Drawable): ByteArray {
        return DrawableUtils.toByteArray(drawable)
    }

    @TypeConverter
    fun toDrawable(data: ByteArray): Drawable {
        return DrawableUtils.toDrawable(data)
    }

    @TypeConverter
    fun toLongArray(time: ArrayList<Date>): String {
        var sb = StringBuffer()
        for (date in time) {
            sb.append(date).append(";")
        }
        return sb.toString()
    }

    @TypeConverter
    fun toArrayListDate(array: String): ArrayList<Date> {
        val list = ArrayList<Date>()
        val arrays = array.split(";")
        for (date in arrays) {
            if (TextUtils.isEmpty(date)) {
                continue
            }
            list.add(Date(date.toLong()))
        }
        return list
    }
}