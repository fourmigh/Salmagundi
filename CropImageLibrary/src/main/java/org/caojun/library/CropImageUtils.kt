package org.caojun.library

import android.app.Activity
import android.graphics.*
import android.view.Surface
import java.io.ByteArrayOutputStream
import java.io.File

/**
 * Created by CaoJun on 2018-3-14.
 */
object CropImageUtils {

    fun transform(scaler: Matrix?,
                  source: Bitmap,
                  targetWidth: Int,
                  targetHeight: Int,
                  scaleUp: Boolean): Bitmap {
        var scaler = scaler

        val deltaX = source.width - targetWidth
        val deltaY = source.height - targetHeight
        if (!scaleUp && (deltaX < 0 || deltaY < 0)) {
            /*
             * In this case the bitmap is smaller, at least in one dimension,
             * than the target.  Transform it by placing as much of the image
             * as possible into the target and leaving the top/bottom or
             * left/right (or both) black.
             */
            val b2 = Bitmap.createBitmap(targetWidth, targetHeight,
                    Bitmap.Config.ARGB_8888)
            val c = Canvas(b2)

            val deltaXHalf = Math.max(0, deltaX / 2)
            val deltaYHalf = Math.max(0, deltaY / 2)
            val src = Rect(
                    deltaXHalf,
                    deltaYHalf,
                    deltaXHalf + Math.min(targetWidth, source.width),
                    deltaYHalf + Math.min(targetHeight, source.height))
            val dstX = (targetWidth - src.width()) / 2
            val dstY = (targetHeight - src.height()) / 2
            val dst = Rect(
                    dstX,
                    dstY,
                    targetWidth - dstX,
                    targetHeight - dstY)
            c.drawBitmap(source, src, dst, null)
            return b2
        }
        val bitmapWidthF = source.width.toFloat()
        val bitmapHeightF = source.height.toFloat()

        val bitmapAspect = bitmapWidthF / bitmapHeightF
        val viewAspect = targetWidth.toFloat() / targetHeight

        if (bitmapAspect > viewAspect) {
            val scale = targetHeight / bitmapHeightF
            if (scale < .9f || scale > 1f) {
                scaler!!.setScale(scale, scale)
            } else {
                scaler = null
            }
        } else {
            val scale = targetWidth / bitmapWidthF
            if (scale < .9f || scale > 1f) {
                scaler!!.setScale(scale, scale)
            } else {
                scaler = null
            }
        }

        val b1: Bitmap
        if (scaler != null) {
            // this is used for minithumb and crop, so we want to mFilter here.
            b1 = Bitmap.createBitmap(source, 0, 0,
                    source.width, source.height, scaler, true)
        } else {
            b1 = source
        }

        val dx1 = Math.max(0, b1.width - targetWidth)
        val dy1 = Math.max(0, b1.height - targetHeight)

        val b2 = Bitmap.createBitmap(
                b1,
                dx1 / 2,
                dy1 / 2,
                targetWidth,
                targetHeight)

        if (b1 != source) {
            b1.recycle()
        }

        return b2
    }

    // Thong added for rotate
    fun rotateImage(src: Bitmap, degree: Float): Bitmap {
        // create new matrix
        val matrix = Matrix()
        // setup rotation degree
        matrix.postRotate(degree)
        return Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)
    }

    fun getOrientationInDegree(activity: Activity): Int {

        val rotation = activity.windowManager.defaultDisplay
                .rotation
        var degrees = 0

        when (rotation) {
            Surface.ROTATION_0 -> degrees = 0
            Surface.ROTATION_90 -> degrees = 90
            Surface.ROTATION_180 -> degrees = 180
            Surface.ROTATION_270 -> degrees = 270
        }

        return degrees
    }

    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    fun deleteFile(file: File) {

        if (file.exists()) {
            if (file.isFile) {
                file.delete()
            } else if (file.isDirectory) {
                val files = file.listFiles()
                for (i in files.indices) {
                    deleteFile(files[i])
                }
            }
            file.delete()
        }
    }

    fun bitmap2Bytes(bitmap: Bitmap?): ByteArray? {
        if (bitmap == null) {
            return null
        }
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        return baos.toByteArray()
    }

    @JvmStatic fun bytes2Bitmap(data: ByteArray?): Bitmap? {
        return if (data == null) {
            null
        } else BitmapFactory.decodeByteArray(data, 0, data.size)
    }
}