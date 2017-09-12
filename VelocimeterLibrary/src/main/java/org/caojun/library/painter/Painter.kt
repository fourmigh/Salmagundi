package org.caojun.library.painter

import android.graphics.Canvas

/**
 * Created by CaoJun on 2017/9/11.
 */
interface Painter {
    fun draw(canvas: Canvas)

    fun onSizeChanged(width: Int, height: Int)
}