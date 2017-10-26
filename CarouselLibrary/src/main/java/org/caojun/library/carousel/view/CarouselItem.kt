package org.caojun.library.carousel.view

import android.content.Context
import android.graphics.Matrix
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout

/**
 * Created by CaoJun on 2017/10/25.
 */
abstract class CarouselItem<T>: FrameLayout, Comparable<CarouselItem<Any>> {

    var index: Int = 0

    var currentAngle: Float = 0f

    var itemX: Float = 0f

    var itemY: Float = 0f

    var itemZ: Float = 0f

    var drawn: Boolean = false

    constructor(context: Context, layoutId: Int): super(context) {

        val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        this.layoutParams = params

        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(layoutId, this, true)

        extractView(view)
    }

    override fun compareTo(other: CarouselItem<Any>): Int = (other.itemZ - this.itemZ).toInt()

    abstract fun extractView(view: View)

    abstract fun update(arg0: T)
}