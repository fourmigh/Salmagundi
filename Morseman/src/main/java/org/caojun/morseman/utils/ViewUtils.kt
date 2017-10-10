package org.caojun.morseman.utils

import android.graphics.Color
import android.view.View

/**
 * Created by CaoJun on 2017/10/10.
 */
object ViewUtils {

    fun on(view: View) {
        set(view, true)
    }

    fun off(view: View) {
        set(view, false)
    }

    private fun set(view: View, on: Boolean) {
        if (on) {
            view.setBackgroundColor(Color.rgb(255, 255, 255))
        } else {
            view.setBackgroundColor(Color.rgb(0, 0, 0))
        }
    }
}