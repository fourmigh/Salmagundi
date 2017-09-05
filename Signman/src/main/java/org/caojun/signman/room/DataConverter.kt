package org.caojun.signman.room

import android.arch.persistence.room.TypeConverter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import java.io.ByteArrayOutputStream
import java.util.Date

/**
 * Created by CaoJun on 2017/9/5.
 */
object DataConverter {
    @TypeConverter
    fun toDate(timestamp: Long): Date {
        return Date(timestamp)
    }

    @TypeConverter
    fun toTimestamp(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun toByteArray(drawable: Drawable): ByteArray {
        val bd = drawable as BitmapDrawable
        val bitmap = bd.bitmap
        val os = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
        return os.toByteArray()
    }

    @TypeConverter
    fun toDrawable(data: ByteArray): Drawable {
        val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size, null)
        return BitmapDrawable(null, bitmap)
    }
}