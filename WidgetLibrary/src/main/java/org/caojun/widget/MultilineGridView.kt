package org.caojun.widget

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.widget.*
import kotlinx.android.synthetic.main.view_multiline_gridview.view.*

/**
 * 多行横向GridView
 * Created by CaoJun on 2018-2-11.
 */
class MultilineGridView : HorizontalScrollView {

    companion object {
        private var Lines = 2
        private var Columns = 4
        fun <E> resort(list: ArrayList<E>): ArrayList<E> {
            val size = list.size
//            var numPages = size / (Lines * Columns)
//            if (size % (Lines * Columns) > 0) {
//                numPages ++//总页数
//            }
            var numParts = size / Columns
            if (size % Columns > 0) {
                numParts ++//总行数
            }

            val result = ArrayList<E>()
            var indexPart = 0
            var indexLine = 0
            for (i in 0 until numParts) {
                for (j in 0 until Columns) {
                    val position = indexPart * Columns + j
                    if (position < size) {
                        result.add(list[position])
                    }
                }

                indexPart += Lines
                if (indexPart >= numParts) {
                    indexLine ++
                    indexPart = indexLine
                }
            }

            return result
        }
    }

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize(attrs)
    }

    private fun initialize(attrs: AttributeSet?) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_multiline_gridview, this)
        var ta = context.obtainStyledAttributes(attrs, R.styleable.MultilineGridView)
        Lines = ta.getInteger(R.styleable.MultilineGridView_numLines, Lines)
        Columns = ta.getInteger(R.styleable.MultilineGridView_numColumns, Columns)

        ta = context.obtainStyledAttributes(attrs, intArrayOf(android.R.attr.horizontalSpacing))
        gridView.horizontalSpacing = ta.getDimensionPixelSize(0, 1)

        ta = context.obtainStyledAttributes(attrs, intArrayOf(android.R.attr.verticalSpacing))
        gridView.verticalSpacing = ta.getDimensionPixelSize(0, 1)
    }

    fun setOnItemClickListener(listener: AdapterView.OnItemClickListener) {
        gridView.onItemClickListener = listener
    }

    fun setAdapter(adapter: ListAdapter) {
        val dm = DisplayMetrics()
        val activity = context as Activity
        activity.windowManager.defaultDisplay.getMetrics(dm)
        val count = adapter.count
        val columns = if (count % Lines == 0) count / Lines else count / Lines + 1
        gridView.adapter = adapter
        val params = LinearLayout.LayoutParams(columns * dm.widthPixels / Columns,
                LinearLayout.LayoutParams.MATCH_PARENT)
        gridView.layoutParams = params
        gridView.columnWidth = dm.widthPixels / Columns
        gridView.stretchMode = GridView.NO_STRETCH
        if (count <= Columns) {
            gridView.numColumns = count
        } else {
            gridView.numColumns = columns
        }
    }
}