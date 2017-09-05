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
        val bitmap = drawable2Bitmap(drawable)
        return bitmap2Bytes(bitmap)
    }

    @TypeConverter
    fun toDrawable(data: ByteArray): Drawable {
        return byteArray2Drawable(data)
    }

    private fun bitmap2Drawable(bitmap: Bitmap): Drawable {
        return BitmapDrawable(null, bitmap)
    }

    private fun drawable2Bitmap(drawable: Drawable): Bitmap {
        val bitmap = Bitmap
                .createBitmap(
                        drawable.intrinsicWidth,
                        drawable.intrinsicHeight,
                        if (drawable.opacity != PixelFormat.OPAQUE)
                            Bitmap.Config.ARGB_8888
                        else
                            Bitmap.Config.RGB_565)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth,
                drawable.intrinsicHeight)
        drawable.draw(canvas)
        return bitmap
    }

    private fun byteArray2Bitmap(data: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(data, 0, data.size)
    }

    private fun bitmap2Bytes(bm: Bitmap): ByteArray {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos)
        return baos.toByteArray()
    }

    private fun byteArray2Drawable(b: ByteArray): Drawable {
        val bitmap = this.byteArray2Bitmap(b)
        return this.bitmap2Drawable(bitmap)
    }
}