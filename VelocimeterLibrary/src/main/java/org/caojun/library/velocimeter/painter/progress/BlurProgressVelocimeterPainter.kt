package org.caojun.library.velocimeter.painter.progress

import android.content.Context
import android.graphics.BlurMaskFilter

/**
 * Created by CaoJun on 2017/9/12.
 */
class BlurProgressVelocimeterPainter: ProgressVelocimeterPainterImp {
    constructor(context: Context, colors: IntArray, max: Float, margin: Int) : super(context, colors, max, margin) {
        paint.maskFilter = BlurMaskFilter(45f, BlurMaskFilter.Blur.NORMAL)
    }
}