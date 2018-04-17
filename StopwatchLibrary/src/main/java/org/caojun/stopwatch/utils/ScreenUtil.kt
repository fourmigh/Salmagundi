package org.caojun.stopwatch.utils

import android.content.Context
import android.util.TypedValue

/**
 * Created by CaoJun on 2018-1-11.
 */
object ScreenUtil {
    fun spToPx(context: Context, sp: Int): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                sp.toFloat(), context.resources.displayMetrics)
    }

    fun dpToPx(context: Context, dp: Int): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp.toFloat(), context.resources.displayMetrics)
    }
}