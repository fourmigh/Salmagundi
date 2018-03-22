package org.caojun.library.velocimeter.utils

import android.content.Context

/**
 * Created by CaoJun on 2017/9/11.
 */
object DimensionUtils {
    fun getSizeInPixels(context: Context, dp: Float): Int {
        val metrics = context.resources.displayMetrics
        val pixels = metrics.density * dp
        return (pixels + 0.5f).toInt()
    }

    fun pixelsToSp(context: Context, sp: Float): Float {
        val scaledDensity = context.resources.displayMetrics.scaledDensity
        return sp * scaledDensity
    }
}