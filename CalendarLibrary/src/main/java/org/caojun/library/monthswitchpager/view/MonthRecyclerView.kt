package org.caojun.library.monthswitchpager.view

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View

/**
 * Created by CaoJun on 2017/8/23.
 */
class MonthRecyclerView: RecyclerView {
    private var LIST_LEFT_OFFSET = -1
    private var mManager: LinearLayoutManager? = null
    private var mMonthSwitchTextView: MonthSwitchTextView? = null

    constructor(context: Context): this(context, null)

    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int): super(context, attrs, defStyle) {
        initData()
    }

    private fun initData() {
        mManager = LinearLayoutManager(context)
        mManager!!.orientation = LinearLayoutManager.HORIZONTAL
        layoutManager = mManager

        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                adjustPosition(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val firstVisiblePosition = mManager!!.findFirstVisibleItemPosition()
                mMonthSwitchTextView!!.setPosition(firstVisiblePosition)
            }
        })
    }

    private fun adjustPosition(recyclerView: RecyclerView, newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            var i = 0
            var child: View? = recyclerView.getChildAt(i)
            while (child != null && child.right <= 0) {
                child = recyclerView.getChildAt(++i)
            }
            if (child == null) {
                // The view is no longer visible, just return
                return
            }
            val left = child.left
            val right = child.right
            val midpoint = recyclerView.width / 2
            if (left < LIST_LEFT_OFFSET) {
                if (right > midpoint) {
                    recyclerView.smoothScrollBy(left, 0)
                } else {
                    recyclerView.smoothScrollBy(right, 0)
                }
            }
        }
    }

    fun setMonthSwitchTextView(textView: MonthSwitchTextView) {
        mMonthSwitchTextView = textView
    }
}