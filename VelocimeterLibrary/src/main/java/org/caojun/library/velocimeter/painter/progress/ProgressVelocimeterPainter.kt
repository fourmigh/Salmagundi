package org.caojun.library.velocimeter.painter.progress

import org.caojun.library.velocimeter.painter.Painter

/**
 * 数值
 * Created by CaoJun on 2017/9/11.
 */
interface ProgressVelocimeterPainter: Painter {
    fun setValue(value: Float)
}