package org.caojun.library.utils

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.view.WindowManager

/**
 * Created by CaoJun on 2017-12-20.
 */
object ScreenUtils {
    fun getScreenSize(context: Context): Point {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val out = Point()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(out)
        } else {
            val width = display.width
            val height = display.height
            out.set(width, height)
        }
        return out
    }
}