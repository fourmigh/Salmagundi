package org.caojun.yujiyizidi.room

import android.arch.persistence.room.TypeConverter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import org.caojun.utils.DrawableUtils
import java.io.ByteArrayOutputStream
import java.util.Date

/**
 * Created by CaoJun on 2017/9/5.
 */
class DataConverter {

    @TypeConverter
    fun toByteArray(bitmap: Bitmap?): ByteArray? {
        if (bitmap == null) {
            return null
        }
        val os = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
        return os.toByteArray()
    }

    @TypeConverter
    fun toBitmap(data: ByteArray?): Bitmap? {
        if (data == null) {
            return null
        }
        return BitmapFactory.decodeByteArray(data, 0, data.size)
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