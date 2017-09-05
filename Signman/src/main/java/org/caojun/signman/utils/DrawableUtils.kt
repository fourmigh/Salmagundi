package org.caojun.signman.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.provider.MediaStore.Images.Media.getBitmap
import java.io.ByteArrayOutputStream


/**
 * Created by CaoJun on 2017/9/4.
 */
object DrawableUtils {

    fun getByteArray(drawable: Drawable): ByteArray {
        val bd = drawable as BitmapDrawable
        val bitmap = bd.bitmap
        val os = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
        return os.toByteArray()
    }

    fun getDrawable(data: ByteArray): Drawable {
        val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size, null)
        val bitmapDrawable = BitmapDrawable(bitmap)
        val drawable = bitmapDrawable
        return drawable
    }
}