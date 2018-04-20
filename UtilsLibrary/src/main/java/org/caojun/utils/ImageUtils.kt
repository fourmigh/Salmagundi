package org.caojun.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.R.attr.path
import android.content.Context
import android.graphics.Canvas
import java.io.*
import android.opengl.ETC1.getWidth
import android.opengl.ETC1.getHeight
import android.view.View


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

    fun toBitmap(path: String): Bitmap? {
        return try {
            BitmapFactory.decodeFile(path)
        } catch (e: Exception) {
            null
        }
    }

    fun toByteArray(path: String): ByteArray? {
        val bitmap = toBitmap(path)
        return toByteArray(bitmap)
    }

    fun toBitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    fun toFile(context: Context, bitmap: Bitmap): File {
        val path = FileUtils.getDiskCachePath(context)
        val file = File(path, "temp.jpg")
        if (file.exists()) {
            file.delete()
        }
        try {
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
            bitmap.recycle()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            return file
        }
    }
}