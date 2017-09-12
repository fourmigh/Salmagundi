package org.caojun.library.painter.digital

import android.content.Context
import android.graphics.BlurMaskFilter

/**
 * Created by CaoJun on 2017/9/12.
 */
class DigitalBlurImp: DigitalImp {
    constructor(context: Context, color: Int, marginTop: Int, textSize: Int, units: String) : super(context, color, marginTop, textSize, units) {
        digitPaint.maskFilter = BlurMaskFilter(6f, BlurMaskFilter.Blur.NORMAL)
    }
}