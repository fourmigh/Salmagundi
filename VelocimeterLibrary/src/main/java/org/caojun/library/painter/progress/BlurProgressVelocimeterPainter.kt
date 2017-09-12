package org.caojun.library.painter.progress

import android.content.Context
import android.graphics.BlurMaskFilter

/**
 * Created by CaoJun on 2017/9/12.
 */
class BlurProgressVelocimeterPainter: ProgressVelocimeterPainterImp {
    constructor(context: Context, color: Int, max: Float, margin: Int) : super(context, color, max, margin) {
        paint.maskFilter = BlurMaskFilter(45f, BlurMaskFilter.Blur.NORMAL)
    }
}