package org.caojun.ttschulte.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

/**
 * Created by CaoJun on 2018-1-8.
 */
class SquareLinearLayout: LinearLayout {
    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val min = Math.min(widthMeasureSpec, heightMeasureSpec)
        super.onMeasure(min, min)
    }
}