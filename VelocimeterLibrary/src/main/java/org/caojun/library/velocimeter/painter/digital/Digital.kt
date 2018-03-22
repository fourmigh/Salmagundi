package org.caojun.library.velocimeter.painter.digital

import org.caojun.library.velocimeter.painter.Painter

/**
 * 数字
 * Created by CaoJun on 2017/9/11.
 */
interface Digital: Painter {
    fun setValue(value: Float)
}