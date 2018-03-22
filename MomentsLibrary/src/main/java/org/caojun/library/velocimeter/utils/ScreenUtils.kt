package org.caojun.library.velocimeter.utils

import android.content.Context
import android.graphics.Point
import android.view.WindowManager

/**
 * Created by CaoJun on 2017-12-20.
 */
object ScreenUtils {
    fun getScreenSize(context: Context): Point {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val out = Point()
        val display = wm.defaultDisplay
        display.getSize(out)
        return out
    }
}