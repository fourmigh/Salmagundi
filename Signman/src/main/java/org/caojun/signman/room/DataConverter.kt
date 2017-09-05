package org.caojun.signman.room

import android.arch.persistence.room.TypeConverter
import android.graphics.drawable.Drawable
import org.caojun.signman.utils.DrawableUtils

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
}