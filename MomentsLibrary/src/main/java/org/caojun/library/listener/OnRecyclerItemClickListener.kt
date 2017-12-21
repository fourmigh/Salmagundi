package org.caojun.library.listener

import android.support.v4.view.GestureDetectorCompat
import android.support.v7.widget.RecyclerView
import android.view.GestureDetector
import android.view.MotionEvent

/**
 * Created by CaoJun on 2017-12-20.
 */
abstract class OnRecyclerItemClickListener: RecyclerView.OnItemTouchListener {

    private var mGestureDetector: GestureDetectorCompat? = null
    private var recyclerView: RecyclerView? = null

    constructor(recyclerView: RecyclerView?) {
        this.recyclerView = recyclerView
        mGestureDetector = GestureDetectorCompat(recyclerView?.context, ItemTouchHelperGestureListener())
    }

    override fun onTouchEvent(rv: RecyclerView?, e: MotionEvent?) {
    }

    override fun onInterceptTouchEvent(rv: RecyclerView?, e: MotionEvent?): Boolean {
        mGestureDetector?.onTouchEvent(e)
        return false
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
    }

    private inner class ItemTouchHelperGestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            val child = recyclerView?.findChildViewUnder(e.x, e.y)
            if (child != null) {
                val vh = recyclerView?.getChildViewHolder(child)
                if (vh != null) {
                    onItemClick(vh)
                }
            }
            return true
        }

        override fun onLongPress(e: MotionEvent) {
            val child = recyclerView?.findChildViewUnder(e.x, e.y)
            if (child != null) {
                val vh = recyclerView?.getChildViewHolder(child)
                if (vh != null) {
                    onItemLongClick(vh)
                }
            }
        }
    }

    abstract fun onItemClick(vh: RecyclerView.ViewHolder)

    abstract fun onItemLongClick(vh: RecyclerView.ViewHolder)
}