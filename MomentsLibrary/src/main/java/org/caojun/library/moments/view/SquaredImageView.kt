package org.caojun.library.moments.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

/**
 * Created by CaoJun on 2017-12-20.
 */
class SquaredImageView: ImageView {
    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredWidth)
    }
}