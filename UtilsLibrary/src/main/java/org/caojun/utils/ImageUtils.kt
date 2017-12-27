package org.caojun.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import java.io.ByteArrayOutputStream

/**
 * Created by CaoJun on 2017/9/5.
 */
object ImageUtils {
    fun toByteArray(drawable: Drawable?): ByteArray? {
        if (drawable == null) {
            return null
        }
        val bd = drawable as BitmapDrawable
        val bitmap = bd.bitmap
        return toByteArray(bitmap)
    }

    fun toDrawable(data: ByteArray?): Drawable? {
        val bitmap = toBitmap(data) ?: return null
        return BitmapDrawable(null, bitmap)
    }

    fun toByteArray(bitmap: Bitmap?): ByteArray? {
        if (bitmap == null) {
            return null
        }
        val os = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
        return os.toByteArray()
    }

    fun toBitmap(data: ByteArray?): Bitmap? {
        if (data == null) {
            return null
        }
        return BitmapFactory.decodeByteArray(data, 0, data.size)
    }
}