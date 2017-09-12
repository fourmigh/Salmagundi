package org.caojun.library.painter.needle

import org.caojun.library.painter.Painter

/**
 * 指针
 * Created by CaoJun on 2017/9/11.
 */
interface NeedlePainter: Painter {
    fun setValue(value: Float)
}