package org.caojun.library.calendar.util

import android.graphics.Paint

/**
 * Created by CaoJun on 2017/8/23.
 */
object UiUtils {
    fun calculateExpandMinHeight(textSize: Float, rowHeight: Int): Float {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.textSize = textSize
        val fontMetrics = paint.fontMetrics
        val fontHeight = fontMetrics.bottom - fontMetrics.top
        return rowHeight * 2 - fontHeight
    }
}