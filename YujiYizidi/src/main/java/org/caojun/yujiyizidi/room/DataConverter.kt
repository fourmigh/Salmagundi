package org.caojun.yujiyizidi.room

import android.arch.persistence.room.TypeConverter
import android.graphics.drawable.Drawable
import org.caojun.utils.DrawableUtils
import java.util.Date

/**
 * Created by CaoJun on 2017/9/5.
 */
class DataConverter {

    @TypeConverter
    fun toByteArray(drawable: Drawable): ByteArray {
        return DrawableUtils.toByteArray(drawable)
    }

    @TypeConverter
    fun toDrawable(data: ByteArray): Drawable {
        return DrawableUtils.toDrawable(data)
    }

    @TypeConverter
    fun toLong(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun toDate(date: Long): Date {
        return Date(date)
    }
}