package org.caojun.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import java.io.ByteArrayOutputStream

/**
 * Created by CaoJun on 2017/9/5.
 */
object DrawableUtils {
    fun toByteArray(drawable: Drawable?): ByteArray? {
        if (drawable == null) {
            return null
        }
        val bd = drawable as BitmapDrawable
        val bitmap = bd.bitmap
        val os = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
        return os.toByteArray()
    }

    fun toDrawable(data: ByteArray?): Drawable? {
        if (data == null) {
            return null
        }
        val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size, null)
        return BitmapDrawable(null, bitmap)
    }
}