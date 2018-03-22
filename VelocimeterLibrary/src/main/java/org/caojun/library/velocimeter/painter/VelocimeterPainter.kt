package org.caojun.library.velocimeter.painter

import android.content.Context
import android.graphics.Paint
import android.graphics.RectF

/**
 * Created by CaoJun on 2017/9/12.
 */
open class VelocimeterPainter {

    constructor(context: Context) {
        this.context = context
    }

    protected var context: Context? = null
    protected var paint: Paint = Paint()
    protected var circle: RectF = RectF()
    protected var width: Int = 0
    protected var height: Int = 0
    protected var startAngle: Float = 160f
    protected var finishAngle: Float = 222f
    protected var strokeWidth: Int = 0
    protected var blurMargin: Int = 0
    protected var lineWidth: Int = 0
    protected var lineSpace: Int = 0
//    protected var color: Int = 0
    protected var colors = intArrayOf(0, 0)
    protected var margin: Int = 0
}