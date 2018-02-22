package org.caojun.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.GridView

/**
 * 可嵌套在ScrollView中的GridView
 * Created by CaoJun on 2018-2-22.
 */
class ScrollGridView: GridView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE shr 2, View.MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, expandSpec)
    }
}