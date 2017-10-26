package org.caojun.library.carousel.view

import android.annotation.SuppressLint
import android.content.Context
import android.database.DataSetObserver
import android.os.Handler
import android.os.Parcelable
import android.os.SystemClock
import android.util.AttributeSet
import android.util.SparseArray
import android.view.*
import android.view.accessibility.AccessibilityEvent
import android.widget.Adapter
import android.widget.ListView

/**
 * Created by CaoJun on 2017/10/25.
 */
abstract class CarouselAdapter<T: Adapter>: ViewGroup {

    /**
     * Represents an invalid position. All valid positions are in the range 0 to 1 less than the
     * number of items in the current adapter.
     */
    val INVALID_POSITION = -1

    /**
     * Represents an empty or invalid row id
     */
    val INVALID_ROW_ID = java.lang.Long.MIN_VALUE

    /**
     * The item view type returned by [Adapter.getItemViewType] when
     * the adapter does not want the item's view recycled.
     */
    val ITEM_VIEW_TYPE_IGNORE = -1

    /**
     * The item view type returned by [Adapter.getItemViewType] when
     * the item is a header or footer.
     */
    val ITEM_VIEW_TYPE_HEADER_OR_FOOTER = -2

    /**
     * The position of the first child displayed
     */
    @ViewDebug.ExportedProperty
    internal var mFirstPosition = 0

    /**
     * The offset in pixels from the top of the CarouselAdapter to the top
     * of the view to select during the next layout.
     */
    internal var mSpecificTop: Int = 0

    /**
     * Position from which to start looking for mSyncRowId
     */
    internal var mSyncPosition: Int = 0

    /**
     * Row id to look for when data has changed
     */
    internal var mSyncRowId = INVALID_ROW_ID

    /**
     * Height of the view when mSyncPosition and mSyncRowId where set
     */
    internal var mSyncHeight: Long = 0

    /**
     * True if we need to sync to mSyncRowId
     */
    internal var mNeedSync = false

    /**
     * Indicates whether to sync based on the selection or position. Possible
     * values are [.SYNC_SELECTED_POSITION] or
     * [.SYNC_FIRST_POSITION].
     */
    internal var mSyncMode: Int = 0

    /**
     * Our height after the last layout
     */
    private var mLayoutHeight: Int = 0

    /**
     * Sync based on the selected child
     */
    internal val SYNC_SELECTED_POSITION = 0

    /**
     * Sync based on the first child displayed
     */
    internal val SYNC_FIRST_POSITION = 1

    /**
     * Maximum amount of time to spend in [.findSyncPosition]
     */
    internal val SYNC_MAX_DURATION_MILLIS = 100

    /**
     * Indicates that this view is currently being laid out.
     */
    internal var mInLayout = false

    /**
     * The listener that receives notifications when an item is selected.
     */
    internal var mOnItemSelectedListener: OnItemSelectedListener? = null

    /**
     * The listener that receives notifications when an item is clicked.
     */
    internal var mOnItemClickListener: OnItemClickListener? = null

    /**
     * The listener that receives notifications when an item is long clicked.
     */
    internal var mOnItemLongClickListener: OnItemLongClickListener? = null

    /**
     * True if the data has changed since the last layout
     */
    internal var mDataChanged: Boolean = false

    /**
     * The position within the adapter's data set of the item to select
     * during the next layout.
     */
    @ViewDebug.ExportedProperty
    internal var mNextSelectedPosition = INVALID_POSITION

    /**
     * The item id of the item to select during the next layout.
     */
    internal var mNextSelectedRowId = INVALID_ROW_ID

    /**
     * The position within the adapter's data set of the currently selected item.
     */
    @ViewDebug.ExportedProperty
    internal var mSelectedPosition = INVALID_POSITION

    /**
     * The item id of the currently selected item.
     */
    internal var mSelectedRowId = INVALID_ROW_ID

    /**
     * View to show if there are no items to show.
     */
    private var mEmptyView: View? = null

    /**
     * The number of items in the current adapter.
     */
    @ViewDebug.ExportedProperty
    internal var mItemCount: Int = 0

    /**
     * The number of items in the adapter before a data changed event occured.
     */
    internal var mOldItemCount: Int = 0

    /**
     * The last selected position we used when notifying
     */
    internal var mOldSelectedPosition = INVALID_POSITION

    /**
     * The id of the last selected position we used when notifying
     */
    internal var mOldSelectedRowId = INVALID_ROW_ID

    /**
     * Indicates what focusable state is requested when calling setFocusable().
     * In addition to this, this view has other criteria for actually
     * determining the focusable state (such as whether its empty or the text
     * filter is shown).
     *
     * @see .setFocusable
     * @see .checkFocus
     */
    private var mDesiredFocusableState: Boolean = false
    private var mDesiredFocusableInTouchModeState: Boolean = false

    private var mSelectionNotifier: SelectionNotifier? = null
    /**
     * When set to true, calls to requestLayout() will not propagate up the parent hierarchy.
     * This is used to layout the children during a layout pass.
     */
    internal var mBlockLayoutRequests = false

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int): super(context, attrs, defStyle)


    /**
     * Interface definition for a callback to be invoked when an item in this
     * CarouselAdapter has been clicked.
     */
    interface OnItemClickListener {

        /**
         * Callback method to be invoked when an item in this CarouselAdapter has
         * been clicked.
         *
         *
         * Implementers can call getItemAtPosition(position) if they need
         * to access the data associated with the selected item.
         *
         * @param parent The CarouselAdapter where the click happened.
         * @param view The view within the CarouselAdapter that was clicked (this
         * will be a view provided by the adapter)
         * @param position The position of the view in the adapter.
         * @param id The row id of the item that was clicked.
         */
        fun onItemClick(parent: CarouselAdapter<*>, view: View, position: Int, id: Long)
    }

    /**
     * Register a callback to be invoked when an item in this CarouselAdapter has
     * been clicked.
     *
     * @param listener The callback that will be invoked.
     */
    fun setOnItemClickListener(listener: OnItemClickListener) {
        mOnItemClickListener = listener
    }

    /**
     * @return The callback to be invoked with an item in this CarouselAdapter has
     * been clicked, or null id no callback has been set.
     */
    fun getOnItemClickListener(): OnItemClickListener? = mOnItemClickListener

    /**
     * Call the OnItemClickListener, if it is defined.
     *
     * @param view The view within the CarouselAdapter that was clicked.
     * @param position The position of the view in the adapter.
     * @param id The row id of the item that was clicked.
     * @return True if there was an assigned OnItemClickListener that was
     * called, false otherwise is returned.
     */
    fun performItemClick(view: View?, position: Int, id: Long): Boolean {
        if (mOnItemClickListener != null && view != null) {
            playSoundEffect(SoundEffectConstants.CLICK)
            mOnItemClickListener!!.onItemClick(this, view, position, id)
            return true
        }

        return false
    }

    /**
     * Interface definition for a callback to be invoked when an item in this
     * view has been clicked and held.
     */
    interface OnItemLongClickListener {
        /**
         * Callback method to be invoked when an item in this view has been
         * clicked and held.
         *
         * Implementers can call getItemAtPosition(position) if they need to access
         * the data associated with the selected item.
         *
         * @param parent The AbsListView where the click happened
         * @param view The view within the AbsListView that was clicked
         * @param position The position of the view in the list
         * @param id The row id of the item that was clicked
         *
         * @return true if the callback consumed the long click, false otherwise
         */
        fun onItemLongClick(parent: CarouselAdapter<*>, view: View?, position: Int, id: Long): Boolean
    }


    /**
     * Register a callback to be invoked when an item in this CarouselAdapter has
     * been clicked and held
     *
     * @param listener The callback that will run
     */
    fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        if (!isLongClickable) {
            isLongClickable = true
        }
        mOnItemLongClickListener = listener
    }

    /**
     * @return The callback to be invoked with an item in this CarouselAdapter has
     * been clicked and held, or null id no callback as been set.
     */
    fun getOnItemLongClickListener(): OnItemLongClickListener? = mOnItemLongClickListener

    /**
     * Interface definition for a callback to be invoked when
     * an item in this view has been selected.
     */
    interface OnItemSelectedListener {
        /**
         * Callback method to be invoked when an item in this view has been
         * selected.
         *
         * Impelmenters can call getItemAtPosition(position) if they need to access the
         * data associated with the selected item.
         *
         * @param parent The CarouselAdapter where the selection happened
         * @param view The view within the CarouselAdapter that was clicked
         * @param position The position of the view in the adapter
         * @param id The row id of the item that is selected
         */
        fun onItemSelected(parent: CarouselAdapter<*>, view: View?, position: Int, id: Long)

        /**
         * Callback method to be invoked when the selection disappears from this
         * view. The selection can disappear for instance when touch is activated
         * or when the adapter becomes empty.
         *
         * @param parent The CarouselAdapter that now contains no selected item.
         */
        fun onNothingSelected(parent: CarouselAdapter<*>)
    }


    /**
     * Register a callback to be invoked when an item in this CarouselAdapter has
     * been selected.
     *
     * @param listener The callback that will run
     */
    fun setOnItemSelectedListener(listener: OnItemSelectedListener) {
        mOnItemSelectedListener = listener
    }

    fun getOnItemSelectedListener(): OnItemSelectedListener? = mOnItemSelectedListener

    /**
     * Extra menu information provided to the
     * [OnCreateContextMenuListener.onCreateContextMenu]
     * callback when a context menu is brought up for this CarouselAdapter.
     *
     */
    class AdapterContextMenuInfo(
            /**
             * The child view for which the context menu is being displayed. This
             * will be one of the children of this CarouselAdapter.
             */
            var targetView: View,
            /**
             * The position in the adapter for which the context menu is being
             * displayed.
             */
            var position: Int,
            /**
             * The row id of the item for which the context menu is being displayed.
             */
            var id: Long) : ContextMenu.ContextMenuInfo

    /**
     * Returns the adapter currently associated with this widget.
     *
     * @return The adapter used to provide this view's content.
     */
    abstract fun getAdapter(): T?

    /**
     * Sets the adapter that provides the data and the views to represent the data
     * in this widget.
     *
     * @param adapter The adapter to use to create this view's content.
     */
    abstract fun setAdapter(adapter: T)

    /**
     * This method is not supported and throws an UnsupportedOperationException when called.
     *
     * @param child Ignored.
     *
     * @throws UnsupportedOperationException Every time this method is invoked.
     */
    override fun addView(child: View) {
        throw UnsupportedOperationException("addView(View) is not supported in CarouselAdapter")
    }

    /**
     * This method is not supported and throws an UnsupportedOperationException when called.
     *
     * @param child Ignored.
     * @param index Ignored.
     *
     * @throws UnsupportedOperationException Every time this method is invoked.
     */
    override fun addView(child: View, index: Int) {
        throw UnsupportedOperationException("addView(View, int) is not supported in CarouselAdapter")
    }

    /**
     * This method is not supported and throws an UnsupportedOperationException when called.
     *
     * @param child Ignored.
     * @param params Ignored.
     *
     * @throws UnsupportedOperationException Every time this method is invoked.
     */
    override fun addView(child: View, params: ViewGroup.LayoutParams) {
        throw UnsupportedOperationException("addView(View, LayoutParams) " + "is not supported in CarouselAdapter")
    }

    /**
     * This method is not supported and throws an UnsupportedOperationException when called.
     *
     * @param child Ignored.
     * @param index Ignored.
     * @param params Ignored.
     *
     * @throws UnsupportedOperationException Every time this method is invoked.
     */
    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        throw UnsupportedOperationException("addView(View, int, LayoutParams) " + "is not supported in CarouselAdapter")
    }

    /**
     * This method is not supported and throws an UnsupportedOperationException when called.
     *
     * @param child Ignored.
     *
     * @throws UnsupportedOperationException Every time this method is invoked.
     */
    override fun removeView(child: View) {
        throw UnsupportedOperationException("removeView(View) is not supported in CarouselAdapter")
    }

    /**
     * This method is not supported and throws an UnsupportedOperationException when called.
     *
     * @param index Ignored.
     *
     * @throws UnsupportedOperationException Every time this method is invoked.
     */
    override fun removeViewAt(index: Int) {
        throw UnsupportedOperationException("removeViewAt(int) is not supported in CarouselAdapter")
    }

    /**
     * This method is not supported and throws an UnsupportedOperationException when called.
     *
     * @throws UnsupportedOperationException Every time this method is invoked.
     */
    override fun removeAllViews() {
        throw UnsupportedOperationException("removeAllViews() is not supported in CarouselAdapter")
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        mLayoutHeight = height
    }

    /**
     * Return the position of the currently selected item within the adapter's data set
     *
     * @return int Position (starting at 0), or [.INVALID_POSITION] if there is nothing selected.
     */
    @ViewDebug.CapturedViewProperty
    fun getSelectedItemPosition(): Int = mNextSelectedPosition

    /**
     * @return The id corresponding to the currently selected item, or [.INVALID_ROW_ID]
     * if nothing is selected.
     */
    @ViewDebug.CapturedViewProperty
    fun getSelectedItemId(): Long = mNextSelectedRowId

    /**
     * @return The view corresponding to the currently selected item, or null
     * if nothing is selected
     */
    abstract fun getSelectedView(): View?

    /**
     * @return The data corresponding to the currently selected item, or
     * null if there is nothing selected.
     */
    fun getSelectedItem(): Any? {
        val adapter = getAdapter()
        val selection = getSelectedItemPosition()
        return if (adapter != null && adapter.count > 0 && selection >= 0) {
            adapter.getItem(selection)
        } else {
            null
        }
    }

    /**
     * @return The number of items owned by the Adapter associated with this
     * CarouselAdapter. (This is the number of data items, which may be
     * larger than the number of visible view.)
     */
    @ViewDebug.CapturedViewProperty
    fun getCount(): Int {
        return mItemCount
    }

    /**
     * Get the position within the adapter's data set for the view, where view is a an adapter item
     * or a descendant of an adapter item.
     *
     * @param view an adapter item, or a descendant of an adapter item. This must be visible in this
     * CarouselAdapter at the time of the call.
     * @return the position within the adapter's data set of the view, or [.INVALID_POSITION]
     * if the view does not correspond to a list item (or it is not currently visible).
     */
    fun getPositionForView(view: View): Int {
        var listItem = view
        try {
            while (listItem.parent as View != this) {
                listItem = listItem.parent as View
            }
        } catch (e: ClassCastException) {
            // We made it up to the window without find this list view
            return INVALID_POSITION
        }

        // Search the children for the list item

        // Child not found!
//        for (i in 0 until childCount) {
//            if (getChildAt(i) == listItem) {
//                return mFirstPosition + i
//            }
//        }
        return (0 until childCount)
                .firstOrNull { getChildAt(it) == listItem }
                ?.let { mFirstPosition + it }
                ?: INVALID_POSITION
    }

    /**
     * Returns the position within the adapter's data set for the first item
     * displayed on screen.
     *
     * @return The position within the adapter's data set
     */
    fun getFirstVisiblePosition(): Int = mFirstPosition

    /**
     * Returns the position within the adapter's data set for the last item
     * displayed on screen.
     *
     * @return The position within the adapter's data set
     */
    fun getLastVisiblePosition(): Int = mFirstPosition + childCount - 1

    /**
     * Sets the currently selected item. To support accessibility subclasses that
     * override this method must invoke the overriden super method first.
     *
     * @param position Index (starting at 0) of the data item to be selected.
     */
    abstract fun setSelection(position: Int)

    /**
     * Sets the view to show if the adapter is empty
     */
    fun setEmptyView(emptyView: View) {
        mEmptyView = emptyView

        val adapter = getAdapter()
        val empty = adapter == null || adapter.isEmpty
        updateEmptyStatus(empty)
    }

    /**
     * When the current adapter is empty, the CarouselAdapter can display a special view
     * call the empty view. The empty view is used to provide feedback to the user
     * that no data is available in this CarouselAdapter.
     *
     * @return The view to show if the adapter is empty.
     */
    fun getEmptyView(): View? = mEmptyView

    /**
     * Indicates whether this view is in filter mode. Filter mode can for instance
     * be enabled by a user when typing on the keyboard.
     *
     * @return True if the view is in filter mode, false otherwise.
     */
    internal fun isInFilterMode(): Boolean = false

    override fun setFocusable(focusable: Boolean) {
        val adapter = getAdapter()
        val empty = adapter == null || adapter.count == 0

        mDesiredFocusableState = focusable
        if (!focusable) {
            mDesiredFocusableInTouchModeState = false
        }

        super.setFocusable(focusable && (!empty || isInFilterMode()))
    }

    override fun setFocusableInTouchMode(focusable: Boolean) {
        val adapter = getAdapter()
        val empty = adapter == null || adapter.count == 0

        mDesiredFocusableInTouchModeState = focusable
        if (focusable) {
            mDesiredFocusableState = true
        }

        super.setFocusableInTouchMode(focusable && (!empty || isInFilterMode()))
    }

    internal fun checkFocus() {
        val adapter = getAdapter()
        val empty = adapter == null || adapter.count == 0
        val focusable = !empty || isInFilterMode()
        // The order in which we set focusable in touch mode/focusable may matter
        // for the client, see View.setFocusableInTouchMode() comments for more
        // details
        super.setFocusableInTouchMode(focusable && mDesiredFocusableInTouchModeState)
        super.setFocusable(focusable && mDesiredFocusableState)
        if (mEmptyView != null) {
            updateEmptyStatus(adapter == null || adapter.isEmpty)
        }
    }

    /**
     * Update the status of the list based on the empty parameter.  If empty is true and
     * we have an empty view, display it.  In all the other cases, make sure that the listview
     * is VISIBLE and that the empty view is GONE (if it's not null).
     */
    @SuppressLint("WrongCall")
    private fun updateEmptyStatus(empty: Boolean) {
        var empty = empty
        if (isInFilterMode()) {
            empty = false
        }

        if (empty) {
            if (mEmptyView != null) {
                mEmptyView?.visibility = View.VISIBLE
                setVisibility(View.GONE)
            } else {
                // If the caller just removed our empty view, make sure the list view is visible
                setVisibility(View.VISIBLE)
            }

            // We are now GONE, so pending layouts will not be dispatched.
            // Force one here to make sure that the state of the list matches
            // the state of the adapter.
            if (mDataChanged) {
                this.onLayout(false, left, top, right, bottom)
            }
        } else {
            mEmptyView?.visibility = View.GONE
            setVisibility(View.VISIBLE)
        }
    }

    /**
     * Gets the data associated with the specified position in the list.
     *
     * @param position Which data to get
     * @return The data associated with the specified position in the list
     */
    fun getItemAtPosition(position: Int): Any? {
        val adapter = getAdapter()
        return if (adapter == null || position < 0) null else adapter.getItem(position)
    }

    fun getItemIdAtPosition(position: Int): Long {
        val adapter = getAdapter()
        return if (adapter == null || position < 0) INVALID_ROW_ID else adapter.getItemId(position)
    }

    override fun setOnClickListener(l: View.OnClickListener?) {
        throw RuntimeException("Don't call setOnClickListener for an CarouselAdapter. " + "You probably want setOnItemClickListener instead")
    }

    /**
     * Override to prevent freezing of any views created by the adapter.
     */
    override fun dispatchSaveInstanceState(container: SparseArray<Parcelable>) {
        dispatchFreezeSelfOnly(container)
    }

    /**
     * Override to prevent thawing of any views created by the adapter.
     */
    override fun dispatchRestoreInstanceState(container: SparseArray<Parcelable>) {
        dispatchThawSelfOnly(container)
    }

    internal inner class AdapterDataSetObserver : DataSetObserver() {

        private var mInstanceState: Parcelable? = null

        override fun onChanged() {
            mDataChanged = true
            mOldItemCount = mItemCount
            mItemCount = getAdapter()?.count?:0

            // Detect the case where a cursor that was previously invalidated has
            // been repopulated with new data.
            if (this@CarouselAdapter.getAdapter()!!.hasStableIds() && mInstanceState != null
                    && mOldItemCount == 0 && mItemCount > 0) {
                this@CarouselAdapter.onRestoreInstanceState(mInstanceState)
                mInstanceState = null
            } else {
                rememberSyncState()
            }
            checkFocus()
            requestLayout()
        }

        override fun onInvalidated() {
            mDataChanged = true

            if (this@CarouselAdapter.getAdapter()!!.hasStableIds()) {
                // Remember the current state for the case where our hosting activity is being
                // stopped and later restarted
                mInstanceState = this@CarouselAdapter.onSaveInstanceState()
            }

            // Data is invalid so we should reset our state
            mOldItemCount = mItemCount
            mItemCount = 0
            mSelectedPosition = INVALID_POSITION
            mSelectedRowId = INVALID_ROW_ID
            mNextSelectedPosition = INVALID_POSITION
            mNextSelectedRowId = INVALID_ROW_ID
            mNeedSync = false
            checkSelectionChanged()

            checkFocus()
            requestLayout()
        }

        fun clearSavedState() {
            mInstanceState = null
        }
    }

    private inner class SelectionNotifier : Handler(), Runnable {
        override fun run() {
            if (mDataChanged) {
                // Data has changed between when this SelectionNotifier
                // was posted and now. We need to wait until the CarouselAdapter
                // has been synched to the new data.
                post(this)
            } else {
                fireOnSelected()
            }
        }
    }

    internal open fun selectionChanged() {
        if (mOnItemSelectedListener != null) {
            if (mInLayout || mBlockLayoutRequests) {
                // If we are in a layout traversal, defer notification
                // by posting. This ensures that the view tree is
                // in a consistent state and is able to accomodate
                // new layout or invalidate requests.
                if (mSelectionNotifier == null) {
                    mSelectionNotifier = SelectionNotifier()
                }
                mSelectionNotifier!!.post(mSelectionNotifier)
            } else {
                fireOnSelected()
            }
        }

        // we fire selection events here not in View
        if (mSelectedPosition != ListView.INVALID_POSITION && isShown && !isInTouchMode) {
            sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_SELECTED)
        }
    }

    private fun fireOnSelected() {
        if (mOnItemSelectedListener == null)
            return

        val selection = this.getSelectedItemPosition()
        if (selection >= 0) {
            val v = getSelectedView()
            mOnItemSelectedListener!!.onItemSelected(this, v, selection,
                    getAdapter()!!.getItemId(selection))
        } else {
            mOnItemSelectedListener!!.onNothingSelected(this)
        }
    }

    override fun dispatchPopulateAccessibilityEvent(event: AccessibilityEvent): Boolean {
        var populated = false
        // This is an exceptional case which occurs when a window gets the
        // focus and sends a focus event via its focused child to announce
        // current focus/selection. CarouselAdapter fires selection but not focus
        // events so we change the event type here.
        if (event.eventType == AccessibilityEvent.TYPE_VIEW_FOCUSED) {
            event.eventType = AccessibilityEvent.TYPE_VIEW_SELECTED
        }

        // we send selection events only from CarouselAdapter to avoid
        // generation of such event for each child
        val selectedView = getSelectedView()
        if (selectedView != null) {
            populated = selectedView.dispatchPopulateAccessibilityEvent(event)
        }

        if (!populated) {
            if (selectedView != null) {
                event.isEnabled = selectedView.isEnabled
            }
            event.itemCount = getCount()
            event.currentItemIndex = getSelectedItemPosition()
        }

        return populated
    }

    override fun canAnimate(): Boolean {
        return super.canAnimate() && mItemCount > 0
    }

    internal fun handleDataChanged() {
        val count = mItemCount
        var found = false

        if (count > 0) {

            var newPos: Int

            // Find the row we are supposed to sync to
            if (mNeedSync) {
                // Update this first, since setNextSelectedPositionInt inspects
                // it
                mNeedSync = false

                // See if we can find a position in the new data with the same
                // id as the old selection
                newPos = findSyncPosition()
                if (newPos >= 0) {
                    // Verify that new selection is selectable
                    val selectablePos = lookForSelectablePosition(newPos, true)
                    if (selectablePos == newPos) {
                        // Same row id is selected
                        setNextSelectedPositionInt(newPos)
                        found = true
                    }
                }
            }
            if (!found) {
                // Try to use the same position if we can't find matching data
                newPos = getSelectedItemPosition()

                // Pin position to the available range
                if (newPos >= count) {
                    newPos = count - 1
                }
                if (newPos < 0) {
                    newPos = 0
                }

                // Make sure we select something selectable -- first look down
                var selectablePos = lookForSelectablePosition(newPos, true)
                if (selectablePos < 0) {
                    // Looking down didn't work -- try looking up
                    selectablePos = lookForSelectablePosition(newPos, false)
                }
                if (selectablePos >= 0) {
                    setNextSelectedPositionInt(selectablePos)
                    checkSelectionChanged()
                    found = true
                }
            }
        }
        if (!found) {
            // Nothing is selected
            mSelectedPosition = INVALID_POSITION
            mSelectedRowId = INVALID_ROW_ID
            mNextSelectedPosition = INVALID_POSITION
            mNextSelectedRowId = INVALID_ROW_ID
            mNeedSync = false
            checkSelectionChanged()
        }
    }

    internal fun checkSelectionChanged() {
        if (mSelectedPosition != mOldSelectedPosition || mSelectedRowId != mOldSelectedRowId) {
            selectionChanged()
            mOldSelectedPosition = mSelectedPosition
            mOldSelectedRowId = mSelectedRowId
        }
    }

    /**
     * Searches the adapter for a position matching mSyncRowId. The search starts at mSyncPosition
     * and then alternates between moving up and moving down until 1) we find the right position, or
     * 2) we run out of time, or 3) we have looked at every position
     *
     * @return Position of the row that matches mSyncRowId, or [.INVALID_POSITION] if it can't
     * be found
     */
    internal fun findSyncPosition(): Int {
        val count = mItemCount

        if (count == 0) {
            return INVALID_POSITION
        }

        val idToMatch = mSyncRowId
        var seed = mSyncPosition

        // If there isn't a selection don't hunt for it
        if (idToMatch == INVALID_ROW_ID) {
            return INVALID_POSITION
        }

        // Pin seed to reasonable values
        seed = Math.max(0, seed)
        seed = Math.min(count - 1, seed)

        val endTime = SystemClock.uptimeMillis() + SYNC_MAX_DURATION_MILLIS

        var rowId: Long

        // first position scanned so far
        var first = seed

        // last position scanned so far
        var last = seed

        // True if we should move down on the next iteration
        var next = false

        // True when we have looked at the first item in the data
        var hitFirst: Boolean

        // True when we have looked at the last item in the data
        var hitLast: Boolean

        // Get the item ID locally (instead of getItemIdAtPosition), so
        // we need the adapter
        val adapter = getAdapter() ?: return INVALID_POSITION

        while (SystemClock.uptimeMillis() <= endTime) {
            rowId = adapter.getItemId(seed)
            if (rowId == idToMatch) {
                // Found it!
                return seed
            }

            hitLast = last == count - 1
            hitFirst = first == 0

            if (hitLast && hitFirst) {
                // Looked at everything
                break
            }

            if (hitFirst || next && !hitLast) {
                // Either we hit the top, or we are trying to move down
                last++
                seed = last
                // Try going up next time
                next = false
            } else if (hitLast || !next && !hitFirst) {
                // Either we hit the bottom, or we are trying to move up
                first--
                seed = first
                // Try going down next time
                next = true
            }

        }

        return INVALID_POSITION
    }

    /**
     * Find a position that can be selected (i.e., is not a separator).
     *
     * @param position The starting position to look at.
     * @param lookDown Whether to look down for other positions.
     * @return The next selectable position starting at position and then searching either up or
     * down. Returns [.INVALID_POSITION] if nothing can be found.
     */
    internal fun lookForSelectablePosition(position: Int, lookDown: Boolean): Int = position

    /**
     * Utility to keep mSelectedPosition and mSelectedRowId in sync
     * @param position Our current position
     */
    internal open fun setSelectedPositionInt(position: Int) {
        mSelectedPosition = position
        mSelectedRowId = getItemIdAtPosition(position)
    }

    /**
     * Utility to keep mNextSelectedPosition and mNextSelectedRowId in sync
     * @param position Intended value for mSelectedPosition the next time we go
     * through layout
     */
    internal fun setNextSelectedPositionInt(position: Int) {
        mNextSelectedPosition = position
        mNextSelectedRowId = getItemIdAtPosition(position)
        // If we are trying to sync to the selection, update that too
        if (mNeedSync && mSyncMode == SYNC_SELECTED_POSITION && position >= 0) {
            mSyncPosition = position
            mSyncRowId = mNextSelectedRowId
        }
    }

    /**
     * Remember enough information to restore the screen state when the data has
     * changed.
     *
     */
    internal fun rememberSyncState() {
        if (childCount > 0) {
            mNeedSync = true
            mSyncHeight = mLayoutHeight.toLong()
            if (mSelectedPosition >= 0) {
                // Sync the selection state
                val v = getChildAt(mSelectedPosition - mFirstPosition)
                mSyncRowId = mNextSelectedRowId
                mSyncPosition = mNextSelectedPosition
                if (v != null) {
                    mSpecificTop = v.top
                }
                mSyncMode = SYNC_SELECTED_POSITION
            } else {
                // Sync the based on the offset of the first view
                val v = getChildAt(0)
                val adapter = getAdapter()
                if (mFirstPosition >= 0 && mFirstPosition < adapter!!.count) {
                    mSyncRowId = adapter.getItemId(mFirstPosition)
                } else {
                    mSyncRowId = View.NO_ID.toLong()
                }
                mSyncPosition = mFirstPosition
                if (v != null) {
                    mSpecificTop = v.top
                }
                mSyncMode = SYNC_FIRST_POSITION
            }
        }
    }
}