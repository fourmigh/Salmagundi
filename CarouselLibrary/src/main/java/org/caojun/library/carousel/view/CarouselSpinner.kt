package org.caojun.library.carousel.view

import android.content.Context
import android.database.DataSetObserver
import android.graphics.Rect
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.SparseArray
import android.view.View
import android.widget.AbsSpinner
import android.widget.SpinnerAdapter
import java.util.*

/**
 * Created by CaoJun on 2017/10/25.
 */
abstract class CarouselSpinner: CarouselAdapter<SpinnerAdapter> {

    internal var mAdapter: SpinnerAdapter? = null

    internal var mHeightMeasureSpec: Int = 0
    internal var mWidthMeasureSpec: Int = 0
    internal var mSelectionLeftPadding = 0
    internal var mSelectionTopPadding = 0
    internal var mSelectionRightPadding = 0
    internal var mSelectionBottomPadding = 0
    internal val mSpinnerPadding = Rect()

    internal val mRecycler = RecycleBin()
    private var mDataSetObserver: DataSetObserver? = null

    constructor(context: Context): this(context, null)

    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int): super(context, attrs, defStyle) {
        initCarouselSpinner()
    }

    /**
     * Common code for different constructor flavors
     */
    private fun initCarouselSpinner() {
        isFocusable = true
        setWillNotDraw(false)
    }

    override fun getAdapter(): SpinnerAdapter? {
        return mAdapter
    }

    override fun setAdapter(adapter: SpinnerAdapter) {
        if (null != mAdapter) {
            mAdapter!!.unregisterDataSetObserver(mDataSetObserver)
            resetList()
        }

        mAdapter = adapter

        mOldSelectedPosition = INVALID_POSITION
        mOldSelectedRowId = INVALID_ROW_ID

        if (mAdapter != null) {
            mOldItemCount = mItemCount
            mItemCount = mAdapter!!.count
            checkFocus()

            mDataSetObserver = AdapterDataSetObserver()
            mAdapter?.registerDataSetObserver(mDataSetObserver)

            val position = if (mItemCount > 0) 0 else INVALID_POSITION

            setSelectedPositionInt(position)
            setNextSelectedPositionInt(position)

            if (mItemCount === 0) {
                // Nothing selected
                checkSelectionChanged()
            }

        } else {
            checkFocus()
            resetList()
            // Nothing selected
            checkSelectionChanged()
        }

        doRequestLayout()

    }

    override fun getSelectedView(): View? {
        return if (mItemCount > 0 && mSelectedPosition >= 0) {
            getChildAt(mSelectedPosition - mFirstPosition)
        } else {
            null
        }
    }

    /**
     * Jump directly to a specific item in the adapter data.
     */
    fun setSelection(position: Int, animate: Boolean) {
        // Animate only if requested position is already on screen somewhere
        val shouldAnimate = animate && mFirstPosition <= position && position <= mFirstPosition + childCount - 1
        setSelectionInt(position, shouldAnimate)
    }

    /**
     * Makes the item at the supplied position selected.
     *
     * @param position
     * Position to select
     * @param animate
     * Should the transition be animated
     */
    internal fun setSelectionInt(position: Int, animate: Boolean) {
        if (position != mOldSelectedPosition) {
            mBlockLayoutRequests = true
            val delta = position - mSelectedPosition
            setNextSelectedPositionInt(position)
            layout(delta, animate)
            mBlockLayoutRequests = false
        }
    }

    internal abstract fun layout(delta: Int, animate: Boolean)

    override fun setSelection(position: Int) {
        setSelectionInt(position, false)
    }

    /**
     * Clear out all children from the list
     */
    internal fun resetList() {
        mDataChanged = false
        mNeedSync = false

        removeAllViewsInLayout()
        mOldSelectedPosition = INVALID_POSITION
        mOldSelectedRowId = INVALID_ROW_ID

        setSelectedPositionInt(INVALID_POSITION)
        setNextSelectedPositionInt(INVALID_POSITION)
        invalidate()
    }

    /**
     * @see View.measure
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize: Int
        val heightSize: Int

        mSpinnerPadding.left = if (paddingLeft > mSelectionLeftPadding) {
            paddingLeft
        }
        else {
            mSelectionLeftPadding
        }
        mSpinnerPadding.top = if (paddingTop > mSelectionTopPadding) {
            paddingTop
        }
        else {
            mSelectionTopPadding
        }
        mSpinnerPadding.right = if (paddingRight > mSelectionRightPadding) {
            paddingRight
        }
        else {
            mSelectionRightPadding
        }
        mSpinnerPadding.bottom = if (paddingBottom > mSelectionBottomPadding) {
            paddingBottom
        }
        else {
            mSelectionBottomPadding
        }

        if (mDataChanged) {
            handleDataChanged()
        }

        var preferredHeight = 0
        var preferredWidth = 0
        var needsMeasuring = true

        val selectedPosition = getSelectedItemPosition()
        if (selectedPosition >= 0 && selectedPosition < mAdapter?.count?:0) {
            // Try looking in the recycler. (Maybe we were measured once
            // already)
            var view = mRecycler[selectedPosition]
            if (view == null) {
                // Make a new one
                view = mAdapter?.getView(selectedPosition, null, this)
            }

            if (view != null) {
                // Put in recycler for re-measuring and/or layout
                mRecycler.put(selectedPosition, view)
            }

            if (view != null) {
                if (view.layoutParams == null) {
                    mBlockLayoutRequests = true
                    view.layoutParams = generateDefaultLayoutParams()
                    mBlockLayoutRequests = false
                }
                measureChild(view, widthMeasureSpec, heightMeasureSpec)

                preferredHeight = getChildHeight(view) + mSpinnerPadding.top
                +mSpinnerPadding.bottom
                preferredWidth = getChildWidth(view) + mSpinnerPadding.left + mSpinnerPadding.right

                needsMeasuring = false
            }
        }

        if (needsMeasuring) {
            // No views -- just use padding
            preferredHeight = mSpinnerPadding.top + mSpinnerPadding.bottom
            if (widthMode == MeasureSpec.UNSPECIFIED) {
                preferredWidth = mSpinnerPadding.left + mSpinnerPadding.right
            }
        }

        preferredHeight = Math.max(preferredHeight, suggestedMinimumHeight)
        preferredWidth = Math.max(preferredWidth, suggestedMinimumWidth)

        heightSize = resolveSize(preferredHeight, heightMeasureSpec)
        widthSize = resolveSize(preferredWidth, widthMeasureSpec)

        setMeasuredDimension(widthSize, heightSize)
        mHeightMeasureSpec = heightMeasureSpec
        mWidthMeasureSpec = widthMeasureSpec
    }

    internal fun getChildHeight(child: View): Int {
        return child.measuredHeight
    }

    internal fun getChildWidth(child: View): Int {
        return child.measuredWidth
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        /*
		 * Carousel expects Carousel.LayoutParams.
		 */
        return LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT)

    }

    internal fun recycleAllViews() {
        val childCount = childCount
        val recycleBin = mRecycler
        val position = mFirstPosition

        // All views go in recycler
        for (i in 0 until childCount) {
            val v = getChildAt(i)
            val index = position + i
            recycleBin.put(index, v)
        }
    }

    private fun doRequestLayout() {
        if (!mBlockLayoutRequests) {
            super.requestLayout()
        }
    }

    fun judgePosition(x: Int, y: Int): Boolean {

        for (i in 0 until mAdapter!!.count) {

            val item = getChildAt(i) as CarouselItem<*>

            val mm = item.matrix
            val pts = FloatArray(3)

            pts[0] = item.left.toFloat()
            pts[1] = item.top.toFloat()
            pts[2] = 0f

            mm.mapPoints(pts)

            val mappedLeft = pts[0].toInt()
            val mappedTop = pts[1].toInt()

            pts[0] = item.right.toFloat()
            pts[1] = item.bottom.toFloat()
            pts[2] = 0f

            mm.mapPoints(pts)

            val mappedRight = pts[0].toInt()
            val mappedBottom = pts[1].toInt()

            if (mappedLeft < x && (mappedRight > x) and (mappedTop < y) && mappedBottom > y) {
                return true
            }
        }
        return false
    }

    /**
     * Maps a point to a position in the list.
     *
     * @param x
     * X in local coordinate
     * @param y
     * Y in local coordinate
     * @return The position of the item which contains the specified point, or
     * [.INVALID_POSITION] if the point does not intersect an
     * item.
     */
    fun pointToPosition(x: Int, y: Int): Int {

        val fitting = ArrayList<CarouselItem<Any>>()

        val count = mAdapter?.count?:0
        for (i in 0 until count) {

            val item = getChildAt(i) as CarouselItem<Any>

            val mm = item.matrix
            val pts = FloatArray(3)

            pts[0] = item.left.toFloat()
            pts[1] = item.top.toFloat()
            pts[2] = 0f

            mm.mapPoints(pts)

            val mappedLeft = pts[0].toInt()
            val mappedTop = pts[1].toInt()

            pts[0] = item.right.toFloat()
            pts[1] = item.bottom.toFloat()
            pts[2] = 0f

            mm.mapPoints(pts)

            val mappedRight = pts[0].toInt()
            val mappedBottom = pts[1].toInt()

            if (mappedLeft < x && (mappedRight > x) and (mappedTop < y) && mappedBottom > y) {
                fitting.add(item)
            }
        }

        Collections.sort(fitting)

        //PML
        //		if (fitting.size() != 0) return fitting.get(0).getIndex();
        //		else return mSelectedPosition;
        return if (fitting.size != 0) {
            fitting[fitting.size - 1].index
        }
        else {
            mSelectedPosition
        }
    }

    internal class SavedState : BaseSavedState {
        var selectedId: Long = 0
        var position: Int = 0

        /**
         * Constructor called from [AbsSpinner.onSaveInstanceState]
         */
        constructor(superState: Parcelable) : super(superState)

        /**
         * Constructor called from [.CREATOR]
         */
        private constructor(`in`: Parcel) : super(`in`) {
            selectedId = `in`.readLong()
            position = `in`.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeLong(selectedId)
            out.writeInt(position)
        }

        override fun toString(): String = "AbsSpinner.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " selectedId=" + selectedId + " position=" + position + "}"

        companion object {

            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(`in`: Parcel): SavedState = SavedState(`in`)

                override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)
            }
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val ss = SavedState(superState)
        ss.selectedId = getSelectedItemId()
        if (ss.selectedId >= 0) {
            ss.position = getSelectedItemPosition()
        } else {
            ss.position = INVALID_POSITION
        }
        return ss
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val ss = state as SavedState

        super.onRestoreInstanceState(ss.superState)

        if (ss.selectedId >= 0) {
            mDataChanged = true
            mNeedSync = true
            mSyncRowId = ss.selectedId
            mSyncPosition = ss.position
            mSyncMode = SYNC_SELECTED_POSITION
            doRequestLayout()
        }
    }

    internal inner class RecycleBin {
        private val mScrapHeap = SparseArray<View>()

        fun put(position: Int, v: View) {
            mScrapHeap.put(position, v)
        }

        operator fun get(position: Int): View? {
            val result = mScrapHeap.get(position)
            if (result != null) {
                mScrapHeap.delete(position)
            }
            return result
        }

        fun clear() {
            val scrapHeap = mScrapHeap
            val count = scrapHeap.size()
            (0 until count).forEach { i ->
                val view = scrapHeap.valueAt(i)
                if (view != null) {
                    removeDetachedView(view, true)
                }
            }
            scrapHeap.clear()
        }
    }
}