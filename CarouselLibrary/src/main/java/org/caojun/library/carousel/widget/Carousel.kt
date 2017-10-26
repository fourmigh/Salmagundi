package org.caojun.library.carousel.widget

import android.content.Context
import android.graphics.Camera
import android.graphics.Matrix
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.view.animation.Transformation
import org.caojun.library.carousel.R
import org.caojun.library.carousel.view.CarouselItem
import org.caojun.library.carousel.view.CarouselSpinner
import org.caojun.library.carousel.view.Rotator
import java.util.*

/**
 * Created by CaoJun on 2017/10/25.
 */
class Carousel: CarouselSpinner, GestureDetector.OnGestureListener {

    /**
     * Default min quantity of images
     */
    private val MIN_QUANTITY = 3

    /**
     * Default max quantity of images
     */
    private val MAX_QUANTITY = 15

    /**
     * Max theta
     */
    private val MAX_THETA = 95f//145正好水平

    /**
     * Duration in milliseconds from the start of a scroll during which we're
     * unsure whether the user is scrolling or flinging.
     */
    private val SCROLL_TO_FLING_UNCERTAINTY_TIMEOUT = 250

    /**
     * Duration in milliseconds during 180
     */
    //	private static final int SCROLL_180_TIMEOUT = 1800;

    // Private members
    /**
     * The Image max width or height
     */
//    private val IMAGE_MAX_WIDTH = 200

    /**
     * The info for adapter context menu
     */
    private var mContextMenuInfo: AdapterContextMenuInfo? = null

    /**
     * How long the transition animation should run when a child view changes
     * position, measured in milliseconds.
     */
    private var mAnimationDuration = 900

    /**
     * Camera to make 3D rotation
     */
    private val mCamera = Camera()

    /**
     * Sets mSuppressSelectionChanged = false. This is used to set it to false
     * in the future. It will also trigger a selection changed.
     */
    private val mDisableSuppressSelectionChangedRunnable = Runnable {
        mSuppressSelectionChanged = false
        selectionChanged()
    }

    /**
     * The position of the item that received the user's down touch.
     */
    private var mDownTouchPosition: Int = 0

    /**
     * The view of the item that received the user's down touch.
     */
    private var mDownTouchView: View? = null

    /**
     * Executes the delta rotations from a fling or scroll movement.
     */
    private val mFlingRunnable = FlingRotateRunnable()

    /**
     * Helper for detecting touch gestures.
     */
    private var mGestureDetector: GestureDetector? = null

    /**
     * Gravity for the widget
     */
    private var mGravity: Int = 0

    /**
     * If true, this onScroll is the first for this user's drag (remember, a
     * drag sends many onScrolls).
     */
    private var mIsFirstScroll: Boolean = false

    /**
     * Set max qantity of images
     */
    private var mMaxQuantity = MAX_QUANTITY

    /**
     * Set min quantity of images
     */
    private var mMinQuantity = MIN_QUANTITY

    /**
     * If true, we have received the "invoke" (center or enter buttons) key
     * down. This is checked before we action on the "invoke" key up, and is
     * subsequently cleared.
     */
    private var mReceivedInvokeKeyDown: Boolean = false

    /**
     * The currently selected item's child.
     */
    private var mSelectedChild: View? = null

    /**
     * Whether to continuously callback on the item selected listener during a
     * fling.
     */
    private var mShouldCallbackDuringFling = true

    /**
     * Whether to callback when an item that is not selected is clicked.
     */
    //	private boolean mShouldCallbackOnUnselectedItemClick = true;

    /**
     * When fling runnable runs, it resets this to false. Any method along the
     * path until the end of its run() can set this to true to abort any
     * remaining fling. For example, if we've reached either the leftmost or
     * rightmost item, we will set this to true.
     */
    private var mShouldStopFling: Boolean = false

    /**
     * If true, do not callback to item selected listener.
     */
    private var mSuppressSelectionChanged: Boolean = false

    /**
     * The axe angle
     */
    private val mTheta = (MAX_THETA * (Math.PI / 180.0)).toFloat()

    /**
     * If items should be reflected
     */
    private var mUseReflection: Boolean = false

    // Constructors

    constructor(context: Context): this(context, null)

    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int): super(context, attrs, defStyle) {

        // It's needed to make items with greater value of
        // z coordinate to be behind items with lesser z-coordinate
        isChildrenDrawingOrderEnabled = true

//        // Making user gestures available
//        mGestureDetector = GestureDetector(this.context, this)
//        mGestureDetector?.setIsLongpressEnabled(true)

        // It's needed to apply 3D transforms to items
        // before they are drawn
        setStaticTransformationsEnabled(true)

        // Retrieve settings
        val arr = getContext().obtainStyledAttributes(attrs, R.styleable.Carousel)
        mAnimationDuration = arr.getInteger(R.styleable.Carousel_android_animationDuration, 400)
        mUseReflection = arr.getBoolean(R.styleable.Carousel_UseReflection, false)
//        val selectedItem = arr.getInteger(R.styleable.Carousel_SelectedItem, 0)

        val min = arr.getInteger(R.styleable.Carousel_minQuantity, MIN_QUANTITY)
        val max = arr.getInteger(R.styleable.Carousel_maxQuantity, MAX_QUANTITY)

        var mTheta = arr.getFloat(R.styleable.Carousel_maxTheta, MAX_THETA)
        if (mTheta > MAX_THETA || mTheta < 0.0f) mTheta = MAX_THETA

        mMinQuantity = if (min < MIN_QUANTITY) MIN_QUANTITY else min
        mMaxQuantity = if (max > MAX_QUANTITY) MAX_QUANTITY else max

        if (arr.length() < mMinQuantity || arr.length() > mMaxQuantity) {
            throw IllegalArgumentException("Invalid set of items.")
        }

    }

    fun setGestureDetectorEnable(enabled: Boolean) {
        if (enabled) {
            mGestureDetector = GestureDetector(this.context, this)
            mGestureDetector?.setIsLongpressEnabled(true)
        }
    }

    // View overrides

    // These are for use with horizontal scrollbar

    /**
     * Compute the horizontal extent of the horizontal scrollbar's thumb within
     * the horizontal range. This value is used to compute the length of the
     * thumb within the scrollbar's track.
     */
    override fun computeHorizontalScrollExtent(): Int {
        // Only 1 item is considered to be selected
        return 1
    }

    /**
     * Compute the horizontal offset of the horizontal scrollbar's thumb within
     * the horizontal range. This value is used to compute the position of the
     * thumb within the scrollbar's track.
     */
    override fun computeHorizontalScrollOffset(): Int {
        // Current scroll position is the same as the selected position
        return mSelectedPosition
    }

    /**
     * Compute the horizontal range that the horizontal scrollbar represents.
     */
    override fun computeHorizontalScrollRange(): Int {
        // Scroll range is the same as the item count
        return mItemCount
    }

    /**
     * Implemented to handle touch screen motion events.
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {

        // Give everything to the gesture detector
        val retValue = mGestureDetector?.onTouchEvent(event)?:false
        val action = event.action
        if (action == MotionEvent.ACTION_UP) {
            // Helper method for lifted finger
            // int downPosition = pointToPosition((int) event.getX(), (int)
            // event.getY());
            // if (judgePosition((int)event.getX(),(int) event.getY()))
            onUp()

        } else if (action == MotionEvent.ACTION_CANCEL) {
            onCancel()
        }

        return retValue
    }

    /**
     * Extra information about the item for which the context menu should be
     * shown.
     */
    override fun getContextMenuInfo(): ContextMenu.ContextMenuInfo? {
        return mContextMenuInfo
    }

    /**
     * Bring up the context menu for this view.
     */
    override fun showContextMenu(): Boolean {

        if (isPressed && mSelectedPosition >= 0) {
            val index = mSelectedPosition - mFirstPosition
            val v = getChildAt(index)
            return dispatchLongPress(v, mSelectedPosition, mSelectedRowId)
        }

        return false
    }

    /**
     * Handles left, right, and clicking
     *
     * @see View.onKeyDown
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        when (keyCode) {

            KeyEvent.KEYCODE_DPAD_LEFT -> {
                // //if (movePrevious()) {
                playSoundEffect(SoundEffectConstants.NAVIGATION_LEFT)
                // //}
                return true
            }

            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                // ///if (moveNext()) {
                playSoundEffect(SoundEffectConstants.NAVIGATION_RIGHT)
                // //}
                return true
            }

            KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> mReceivedInvokeKeyDown = true
        }// fallthrough to default handling

        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> {

                if (mReceivedInvokeKeyDown) {
                    if (mItemCount > 0) {

                        dispatchPress(mSelectedChild)
                        postDelayed(Runnable { dispatchUnpress() }, ViewConfiguration.getPressedStateDuration().toLong())

                        val selectedIndex = mSelectedPosition - mFirstPosition
                        println("The key is:" + selectedIndex)
                        performItemClick(getChildAt(selectedIndex), mSelectedPosition,
                                mAdapter?.getItemId(mSelectedPosition)?:0)
                    }
                }

                // Clear the flag
                mReceivedInvokeKeyDown = false

                return true
            }
        }

        return super.onKeyUp(keyCode, event)
    }

    override fun onFocusChanged(gainFocus: Boolean, direction: Int, previouslyFocusedRect: Rect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect)

        /*
		 * The gallery shows focus by focusing the selected item. So, give focus
		 * to our selected item instead. We steal keys from our selected item
		 * elsewhere.
		 */
        if (gainFocus && mSelectedChild != null) {
            mSelectedChild!!.requestFocus(direction)
        }

    }

    // ViewGroup overrides

    override fun checkLayoutParams(p: ViewGroup.LayoutParams): Boolean {
        return p is LayoutParams
    }

    override fun generateLayoutParams(p: ViewGroup.LayoutParams): ViewGroup.LayoutParams {
        return LayoutParams(p)
    }

    override fun generateLayoutParams(attrs: AttributeSet): ViewGroup.LayoutParams {
        return LayoutParams(context, attrs)
    }

    override fun dispatchSetSelected(selected: Boolean) {
        /*
		 * We don't want to pass the selected state given from its parent to its
		 * children since this widget itself has a selected state to give to its
		 * children.
		 */
    }

    override fun dispatchSetPressed(pressed: Boolean) {

        // Show the pressed state on the selected child
        if (mSelectedChild != null) {
            mSelectedChild!!.isPressed = pressed
        }
    }

    override fun showContextMenuForChild(originalView: View): Boolean {

        val longPressPosition = getPositionForView(originalView)
        if (longPressPosition < 0) {
            return false
        }

        val longPressId = mAdapter?.getItemId(longPressPosition)?:0
        return dispatchLongPress(originalView, longPressPosition, longPressId)
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        // Gallery steals all key events
        return event.dispatch(this, null, null)
    }

    /**
     * Index of the child to draw for this iteration
     */
    override fun getChildDrawingOrder(childCount: Int, i: Int): Int {

        // Sort Carousel items by z coordinate in reverse order
        val sl = ArrayList<CarouselItem<Any>>()
        for (j in 0 until childCount) {
            val view = getAdapter()?.getView(j, null, null) as CarouselItem<Any>
            if (i == 0) {
                view.drawn = false
            }
            sl.add(getAdapter()?.getView(j, null, null) as CarouselItem<Any>)
        }

        Collections.sort(sl)

        // Get first undrawn item in array and get result index
        var idx = 0

        for (civ in sl) {
            if (!civ.drawn) {
                civ.drawn = true
                idx = civ.index
                break
            }
        }

        return idx

    }

    /**
     * Transform an item depending on it's coordinates
     */
    override fun getChildStaticTransformation(child: View, transformation: Transformation): Boolean {

        transformation.clear()
        transformation.transformationType = Transformation.TYPE_MATRIX

        // Center of the view
        //		float centerX = (float) getWidth() /3, centerY = (float) getHeight()/2 ;
        val centerX = width as Float / 2
        val centerY = height as Float / 2

        // Save camera
        mCamera.save()

        // Translate the item to it's coordinates
        val matrix = transformation.matrix

        mCamera.translate((child as CarouselItem<Any>).itemX, child.itemY, child.itemZ)

        // Align the item
        mCamera.getMatrix(matrix)

        matrix.preTranslate(-centerX, -centerY)
        matrix.postTranslate(centerX, centerY)

        val values = FloatArray(9)
        matrix.getValues(values)

        // Restore camera
        mCamera.restore()

//        val mm = Matrix()
//        mm.setValues(values)
//        (child as CarouselItem<Any>).matrix = mm
        child.matrix.setValues(values)

        // http://code.google.com/p/android/issues/detail?id=35178
        child.invalidate()
        return true
    }

    /**
     * The alpha below 255 by the angle
     *
     * @param angleOffset
     * @return
     */
    private fun getAlphaValue(angleOffset: Float): Int {
        var angleOffset = angleOffset
        //		int alpha = 0;
        angleOffset %= 360
        if (angleOffset > 180) {
            angleOffset = 360 - angleOffset
        }
        return (255 - angleOffset).toInt()
    }

    // CarouselAdapter overrides

    /**
     * Setting up images
     */
    override fun layout(delta: Int, animate: Boolean) {
        if (mDataChanged) {
            handleDataChanged()
        }

        // Handle an empty gallery by removing all views.
        if (getCount() === 0) {
            resetList()
            return
        }

        // Update to the new selected position.
        if (mNextSelectedPosition >= 0) {
            setSelectedPositionInt(mNextSelectedPosition)
        }

        // All views go in recycler while we are in layout
        recycleAllViews()

        // Clear out old views
        detachAllViewsFromParent()

        val count = getAdapter()?.count?:0
        // float angleUnit = 360.0f / count;
        //
        // float angleOffset = mSelectedPosition * angleUnit;
        // for (int i = 0;i < getAdapter().getCount();i++) {
        // float angle = angleUnit * i - angleOffset;
        // if (angle < 0.0f) angle = 360.0f + angle;
        // makeAndAddView(i, angle);
        // }

        val angleUnit = (Math.round(360.0f / count) * 1000000 / 1000000).toFloat()

        val angleOffset = mSelectedPosition * angleUnit

        for (i in 0 until count) {
            var angle = angleUnit * i - angleOffset
            if (angle < 0.0f) {
                angle += 360.0f
            }
            makeAndAddView(i, angle)
        }

        // Flush any cached views that did not get reused above
        mRecycler.clear()

        invalidate()

        setNextSelectedPositionInt(mSelectedPosition)

        checkSelectionChanged()

        // //////mDataChanged = false;
        mNeedSync = false

        updateSelectedItemMetadata()

    }

    /**
     * Setting up images after layout changed
     */
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)

        /*
		 * Remember that we are in layout to prevent more layout request from
		 * being generated.
		 */
        mInLayout = true
        layout(0, false)
        mInLayout = false
    }

    override fun selectionChanged() {
        if (!mSuppressSelectionChanged) {
            super.selectionChanged()
        }
    }

    override fun setSelectedPositionInt(position: Int) {
        super.setSelectedPositionInt(position)
        super.setNextSelectedPositionInt(position)

        // Updates any metadata we keep about the selected item.
        updateSelectedItemMetadata()
    }

    // Rotation class for the Carousel

    private inner class FlingRotateRunnable : Runnable {

        /**
         * Tracks the decay of a fling rotation
         */
        val mRotator: Rotator = Rotator(context)

        /**
         * Angle value reported by mRotator on the previous fling
         */
        private var mLastFlingAngle: Float = 0.toFloat()

        /**
         * Constructor
         */
//        init {
//            mRotator = Rotator(context)
//        }

        private fun startCommon() {
            // Remove any pending flings
            removeCallbacks(this)
        }

        //		public void startUsingVelocity(float initialVelocity) {
        //			if (initialVelocity == 0) return;
        //
        //			startCommon();
        //
        //			mLastFlingAngle = 0.0f;
        //
        //			mRotator.fling(initialVelocity);
        //
        //			post(this);
        //		}

        fun startUsingDistance(deltaAngle: Float) {
            if (deltaAngle == 0f) return

            startCommon()

            mLastFlingAngle = 0f
            synchronized(this) {
                mRotator?.startRotate(0.0f, -deltaAngle, mAnimationDuration)
            }
            post(this)
        }

        fun stop(scrollIntoSlots: Boolean) {
            removeCallbacks(this)
            endFling(scrollIntoSlots)
        }

        private fun endFling(scrollIntoSlots: Boolean) {
            /*
			 * Force the scroller's status to finished (without setting its
			 * position to the end)
			 */
            synchronized(this) {
                mRotator?.forceFinished(true)
            }

            if (scrollIntoSlots) scrollIntoSlots()
        }

        override fun run() {
            if (this@Carousel.childCount === 0) {
                endFling(true)
                return
            }

            mShouldStopFling = false

//            var rotator: Rotator? = null
            var angle = 0f
            var more = false
            synchronized(this) {
//                rotator = mRotator
                more = mRotator.computeAngleOffset()
                angle = mRotator.getCurrAngle()
            }

            // Flip sign to convert finger direction to list items direction
            // (e.g. finger moving down means list is moving towards the top)
            val delta = mLastFlingAngle - angle

            // ////// Shoud be reworked
            // System.out.println("The delta is:"+delta);
            trackMotionScroll(delta)

            if (more && !mShouldStopFling) {
                mLastFlingAngle = angle
                post(this)
            } else {
                mLastFlingAngle = 0.0f
                endFling(true)
            }

        }

    }


    // OnGestureListener implementation

    override fun onDown(e: MotionEvent): Boolean {
        // Kill any existing fling/scroll
        mFlingRunnable.stop(false)

        // /// Don't know yet what for it is
        // Get the item's view that was touched
        mDownTouchPosition = pointToPosition(e.x.toInt(), e.y.toInt())

        if (mDownTouchPosition >= 0) {
            mDownTouchView = getChildAt(mDownTouchPosition - mFirstPosition)
            mDownTouchView?.isPressed = true
        }

        // Reset the multiple-scroll tracking state
        mIsFirstScroll = true

        // Must return true to get matching events for this down event.
        return true
    }

    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean = true

    override fun onLongPress(e: MotionEvent) {

        if (mDownTouchPosition < 0) {
            return
        }

        performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
        val id = getItemIdAtPosition(mDownTouchPosition)
        dispatchLongPress(mDownTouchView, mDownTouchPosition, id)
    }

    override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {

        /*
		 * Now's a good time to tell our parent to stop intercepting our events!
		 * The user has moved more than the slop amount, since GestureDetector
		 * ensures this before calling this method. Also, if a parent is more
		 * interested in this touch's events than we are, it would have
		 * intercepted them by now (for example, we can assume when a Gallery is
		 * in the ListView, a vertical scroll would not end up in this method
		 * since a ListView would have intercepted it by now).
		 */

        parent.requestDisallowInterceptTouchEvent(true)

        // As the user scrolls, we want to callback selection changes so
        // related-
        // info on the screen is up-to-date with the gallery's selection
        if (!mShouldCallbackDuringFling) {
            if (mIsFirstScroll) {
                /*
				 * We're not notifying the client of selection changes during
				 * the fling, and this scroll could possibly be a fling. Don't
				 * do selection changes until we're sure it is not a fling.
				 */
                if (!mSuppressSelectionChanged) {
                    mSuppressSelectionChanged = true
                }
                postDelayed(mDisableSuppressSelectionChangedRunnable, SCROLL_TO_FLING_UNCERTAINTY_TIMEOUT.toLong())
            }
        } else {
            if (mSuppressSelectionChanged) {
                mSuppressSelectionChanged = false
            }
        }

        // Track the motion
        trackMotionScroll(/* -1 * */(distanceX.toInt() / 3).toFloat())

        mIsFirstScroll = false
        return true
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {

        if (judgePosition(e.x.toInt(), e.y.toInt())) {
            if (mDownTouchPosition == mSelectedPosition) {
                performItemClick(mDownTouchView, mDownTouchPosition, mAdapter?.getItemId(mDownTouchPosition)?:0)
            } else if (mDownTouchPosition != mSelectedPosition) {
                val count = getAdapter()?.count?:0
                val circleNum = (mSelectedPosition + count - mDownTouchPosition) % count
                var circleAngle = (circleNum * 360 / count).toFloat()
                if (circleAngle > 180.0f) {
                    circleAngle -= 360.0f
                }
                mFlingRunnable.startUsingDistance(circleAngle)
            }
            return true
        }
        return false

        // if (mDownTouchPosition >= 0) {
        //
        // // Pass the click so the client knows, if it wants to.
        // if (mShouldCallbackOnUnselectedItemClick || mDownTouchPosition ==
        // mSelectedPosition) {
        // performItemClick(mDownTouchView, mDownTouchPosition,
        // mAdapter.getItemId(mDownTouchPosition));
        // }
        //
        // return true;
        // }
        //
        // return false;

    }

    // /// Unused gestures
    override fun onShowPress(e: MotionEvent) {}

    private fun Calculate3DPosition(child: CarouselItem<Any>, diameter: Int, angleOffset: Float) {
        var angleOffset = angleOffset

        angleOffset *= (Math.PI / 180.0f).toFloat()
        //dalong--------------------------------------------------
        var x = -(diameter * 3 / 5 * Math.sin(angleOffset.toDouble())).toFloat() + diameter / 2 - child.width / 2
        if (getAdapter()?.count === 3) {
            x = -(diameter * 3 / 4 * Math.sin(angleOffset.toDouble())).toFloat() + diameter / 2 - child.width / 2
        }

        val z = diameter / 2 * (1.0f - Math.cos(angleOffset.toDouble()).toFloat())

        child.itemX = x
        child.itemZ = z
        child.itemY = -height / 8 + (z * Math.sin(mTheta.toDouble())).toFloat()

    }

    /**
     * Figure out vertical placement based on mGravity
     *
     * @param child
     * Child to place
     * @return Where the top of the child should be
     */
    private fun calculateTop(child: View, duringLayout: Boolean): Int {
        val myHeight = if (duringLayout) measuredHeight else height
        val childHeight = if (duringLayout) child.measuredHeight else child.height

        var childTop = 0

        when (mGravity) {
            Gravity.TOP -> childTop = mSpinnerPadding.top
            Gravity.CENTER_VERTICAL -> {
                val availableSpace = myHeight - mSpinnerPadding.bottom - mSpinnerPadding.top
                -childHeight
                childTop = mSpinnerPadding.top + availableSpace / 2
            }
            Gravity.BOTTOM -> childTop = myHeight - mSpinnerPadding.bottom - childHeight
        }
        return childTop / 2
    }

    private fun dispatchLongPress(view: View?, position: Int, id: Long): Boolean {
        var handled = false

        if (mOnItemLongClickListener != null) {
            handled = mOnItemLongClickListener!!.onItemLongClick(this, mDownTouchView, mDownTouchPosition, id)
        }

        if (!handled && view != null) {
            mContextMenuInfo = AdapterContextMenuInfo(view, position, id)
            handled = super.showContextMenuForChild(this)
        }

        if (handled) {
            performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
        }

        return handled
    }

    private fun dispatchPress(child: View?) {

        if (child != null) {
            child.isPressed = true
        }

        isPressed = true
    }

    private fun dispatchUnpress() {

        for (i in childCount - 1 downTo 0) {
            getChildAt(i).isPressed = false
        }

        isPressed = false
    }

    /**
     * @return The center of this Gallery.
     */
    private fun getCenterOfGallery(): Int = (width - this.paddingLeft - this.paddingRight) / 2 + this.paddingLeft

    /**
     * @return The center of the given view.
     */
    private fun getCenterOfView(view: View): Int = view.left + view.width / 2

    internal fun getLimitedMotionScrollAmount(motionToLeft: Boolean, deltaX: Float): Float {
        val extremeItemPosition = if (motionToLeft) this@Carousel.getCount() - 1 else 0
        val extremeChild = getChildAt(extremeItemPosition - this@Carousel.getFirstVisiblePosition()) ?: return deltaX

        val extremeChildCenter = getCenterOfView(extremeChild)
        val galleryCenter = getCenterOfGallery()

        if (motionToLeft) {
            if (extremeChildCenter <= galleryCenter) {

                // The extreme child is past his boundary point!
                return 0f
            }
        } else {
            if (extremeChildCenter >= galleryCenter) {

                // The extreme child is past his boundary point!
                return 0f
            }
        }
        val centerDifference = galleryCenter - extremeChildCenter

        return if (motionToLeft) {
            Math.max(centerDifference.toFloat(), deltaX)
        }
        else {
            Math.min(centerDifference.toFloat(), deltaX)
        }
    }

    internal fun getLimitedMotionScrollAmount(motionToLeft: Boolean, deltaX: Int): Int {
        val extremeItemPosition = if (motionToLeft) mItemCount - 1 else 0
        val extremeChild = getChildAt(extremeItemPosition - mFirstPosition) ?: return deltaX

        val extremeChildCenter = getCenterOfView(extremeChild)
        val galleryCenter = getCenterOfGallery()

        if (motionToLeft) {
            if (extremeChildCenter <= galleryCenter) {

                // The extreme child is past his boundary point!
                return 0
            }
        } else {
            if (extremeChildCenter >= galleryCenter) {

                // The extreme child is past his boundary point!
                return 0
            }
        }

        val centerDifference = galleryCenter - extremeChildCenter

        return if (motionToLeft) {
            Math.max(centerDifference, deltaX)
        }
        else {
            Math.min(centerDifference, deltaX)
        }
    }

    private fun makeAndAddView(position: Int, angleOffset: Float) {
        var child: CarouselItem<Any>? = null

        if (!mDataChanged) {
            child = mRecycler[position] as CarouselItem<Any>
            if (child != null) {

                // Position the view
                setUpChild(child, child.index, angleOffset)
            } else {
                // Nothing found in the recycler -- ask the adapter for a view
                child = mAdapter?.getView(position, null, this) as CarouselItem<Any>

                // Position the view
                setUpChild(child, child.index, angleOffset)
            }
            return
        }

        // Nothing found in the recycler -- ask the adapter for a view
        child = mAdapter?.getView(position, null, this) as CarouselItem<Any>

        // Position the view
        setUpChild(child, child.index, angleOffset)

    }

    internal fun onCancel() {
        onUp()
    }

    /**
     * Called when rotation is finished
     */
    private fun onFinishedMovement() {
        if (mSuppressSelectionChanged) {
            mSuppressSelectionChanged = false

            // We haven't been callbacking during the fling, so do it now
            super.selectionChanged()
        }
        checkSelectionChanged()
        invalidate()

    }

    fun onUp() {
        if (mFlingRunnable.mRotator?.isFinished()?:false) {
            scrollIntoSlots()
        }
        dispatchUnpress()
    }

    /**
     * Brings an item with nearest to 0 degrees angle to this angle and sets it
     * selected
     */
    private fun scrollIntoSlots() {
        // Nothing to do
        if (childCount === 0 || mSelectedChild == null) {
            return
        }

        // get nearest item to the 0 degrees angle
        // Sort itmes and get nearest angle
        var angle: Float
        val position: Int

        val count = getAdapter()?.count?:0
        val arr = (0 until count).map { getAdapter()?.getView(it, null, null) as CarouselItem<Any> }

        Collections.sort<CarouselItem<Any>>(arr) { c1, c2 ->
            var a1 = c1.currentAngle.toInt()
            if (a1 > 180) a1 = 360 - a1
            var a2 = c2.currentAngle.toInt()
            if (a2 > 180) a2 = 360 - a2
            a1 - a2
        }

        angle = arr[0].currentAngle
        // Make it minimum to rotate
        if (angle > 180.0f) angle = -(360.0f - angle)

        // Start rotation if needed
        if (angle != 0.0f) {
            mFlingRunnable.startUsingDistance(-angle)
        } else {
            // Set selected position
            position = arr[0].index
            setSelectedPositionInt(position)
            onFinishedMovement()
        }

    }

    fun scrollToChild(i: Int) {

        val view = getAdapter()?.getView(i, null, null) as CarouselItem<Any>
        var angle = view.currentAngle

        if (angle == 0f) {
            return
        }

        if (angle > 180f) {
            angle = 360f - angle
        }
        else {
            angle = -angle
        }

        mFlingRunnable.startUsingDistance(angle)

    }

    /**
     * Whether or not to callback on any [.getOnItemSelectedListener]
     * while the items are being flinged. If false, only the final selected item
     * will cause the callback. If true, all items between the first and the
     * final will cause callbacks.
     *
     * @param shouldCallback
     * Whether or not to callback on the listener while the items are
     * being flinged.
     */
    fun setCallbackDuringFling(shouldCallback: Boolean) {
        mShouldCallbackDuringFling = shouldCallback
    }

    /**
     * Whether or not to callback when an item that is not selected is clicked.
     * If false, the item will become selected (and re-centered). If true, the
     * {@link #getOnItemClickListener()} will get the callback.
     *
     * @param shouldCallback
     *            Whether or not to callback on the listener when a item that is
     *            not selected is clicked.
     * @hide
     */
    //	public void setCallbackOnUnselectedItemClick(boolean shouldCallback) {
    //		mShouldCallbackOnUnselectedItemClick = shouldCallback;
    //	}

    /**
     * Sets how long the transition animation should run when a child view
     * changes position. Only relevant if animation is turned on.
     *
     * @param animationDurationMillis
     * The duration of the transition, in milliseconds.
     *
     * @attr ref android.R.styleable#Gallery_animationDuration
     */
    fun setAnimationDuration(animationDurationMillis: Int) {
        mAnimationDuration = animationDurationMillis
    }

    fun setGravity(gravity: Int) {
        if (mGravity != gravity) {
            mGravity = gravity
            requestLayout()
        }
    }

    /**
     * Helper for makeAndAddView to set the position of a view and fill out its
     * layout paramters.
     *
     * @param child
     * The view to position
     * Offset from the selected position
     * X-coordintate indicating where this view should be placed.
     * This will either be the left or right edge of the view,
     * depending on the fromLeft paramter
     * Are we posiitoning views based on the left edge? (i.e.,
     * building from left to right)?
     */
    private fun setUpChild(child: CarouselItem<Any>?, index: Int, angleOffset: Float) {

        if (child == null) {
            return
        }
        // Ignore any layout parameters for child, use wrap content
        addViewInLayout(child, -1 /* index */, generateDefaultLayoutParams())

        child.isSelected = index == mSelectedPosition

        val h: Int
        val w: Int
        val d: Int

        if (mInLayout) {
            w = child.measuredWidth
            h = child.measuredHeight
            d = measuredWidth

        } else {
            w = child.measuredWidth
            h = child.measuredHeight
            d = width

        }

        child.currentAngle = angleOffset

        // Measure child
        child.measure(w, h)

        val childLeft = 0

        // Position vertically based on gravity setting
        val childTop = calculateTop(child, true)

//        childLeft = 0

        child.layout(childLeft, childTop, w, h)

        Calculate3DPosition(child, d, angleOffset)

    }

    /**
     * Tracks a motion scroll. In reality, this is used to do just about any
     * movement to items (touch scroll, arrow-key scroll, set an item as
     * selected).
     *
     * @param deltaAngle
     * Change in X from the previous event.
     */
    fun trackMotionScroll(deltaAngle: Float) {
        if (childCount == 0) {
            return
        }

        val count = getAdapter()?.count?:0
        for (i in 0 until count) {

            val child = getAdapter()?.getView(i, null, null) as CarouselItem<Any>

            var angle = child.currentAngle
            angle += deltaAngle

            while (angle > 360f) {
                angle -= 360f
            }

            while (angle < 0f) {
                angle += 360f
            }

            child.currentAngle = angle
            Calculate3DPosition(child, width, angle)

        }

        // Clear unused views
        mRecycler.clear()

        invalidate()
    }

    private fun updateSelectedItemMetadata() {

        val oldSelectedChild = mSelectedChild

        mSelectedChild = getChildAt(mSelectedPosition - mFirstPosition)
        val child = mSelectedChild ?: return

        child.isSelected = true
        child.isFocusable = true

        if (hasFocus()) {
            child.requestFocus()
        }

        // We unfocus the old child down here so the above hasFocus check
        // returns true
        if (oldSelectedChild != null) {

            // Make sure its drawable state doesn't contain 'selected'
            oldSelectedChild.isSelected = false

            // Make sure it is not focusable anymore, since otherwise arrow keys
            // can make this one be focused
            oldSelectedChild.isFocusable = false
        }

    }
}