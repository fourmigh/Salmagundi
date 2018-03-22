package org.caojun.library.velocimeter.painter.needle

import android.content.Context
import android.graphics.BlurMaskFilter

/**
 * Created by CaoJun on 2017/9/12.
 */
class LineBlurPainter: NeedlePainterImp {
    constructor(context: Context, color: Int, max: Float) : super(context, color, max) {
        paint.maskFilter = BlurMaskFilter(25f, BlurMaskFilter.Blur.NORMAL)
    }
}