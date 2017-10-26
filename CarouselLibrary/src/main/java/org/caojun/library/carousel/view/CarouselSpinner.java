package org.caojun.library.carousel.view;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsSpinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.Collections;

public abstract class CarouselSpinner extends CarouselViewGroup<SpinnerAdapter> {

    public SpinnerAdapter mAdapter;

    int mHeightMeasureSpec;
    int mWidthMeasureSpec;
    boolean mBlockLayoutRequests;

    int mSelectionLeftPadding = 0;
    int mSelectionTopPadding = 0;
    int mSelectionRightPadding = 0;
    int mSelectionBottomPadding = 0;
    public final Rect mSpinnerPadding = new Rect();

    public final RecycleBin mRecycler = new RecycleBin();
    private DataSetObserver mDataSetObserver;

    public CarouselSpinner(Context context) {
        super(context);
        initCarouselSpinner();
    }

    public CarouselSpinner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CarouselSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initCarouselSpinner();
    }

    /**
     * Common code for different constructor flavors
     */
    private void initCarouselSpinner() {
        setFocusable(true);
        setWillNotDraw(false);
    }

    public SpinnerAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(SpinnerAdapter adapter) {
        if (null != mAdapter) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
            resetList();
        }

        mAdapter = adapter;

        mOldSelectedPosition = INVALID_POSITION;
        mOldSelectedRowId = INVALID_ROW_ID;

        if (mAdapter != null) {
            mOldItemCount = mItemCount;
            mItemCount = mAdapter.getCount();
            checkFocus();

            mDataSetObserver = new AdapterDataSetObserver();
            mAdapter.registerDataSetObserver(mDataSetObserver);

            int position = mItemCount > 0 ? 0 : INVALID_POSITION;

            setSelectedPositionInt(position);
            setNextSelectedPositionInt(position);

            if (mItemCount == 0) {
                // Nothing selected
                checkSelectionChanged();
            }

        } else {
            checkFocus();
            resetList();
            // Nothing selected
            checkSelectionChanged();
        }

        requestLayout();

    }

    public View getSelectedView() {
        if (mItemCount > 0 && mSelectedPosition >= 0) {
            return getChildAt(mSelectedPosition - mFirstPosition);
        } else {
            return null;
        }
    }

    /**
     * Jump directly to a specific item in the adapter data.
     */
    public void setSelection(int position, boolean animate) {
        // Animate only if requested position is already on screen somewhere
        boolean shouldAnimate = animate && mFirstPosition <= position
                && position <= mFirstPosition + getChildCount() - 1;
        setSelectionInt(position, shouldAnimate);
    }

    /**
     * Makes the item at the supplied position selected.
     *
     * @param position Position to select
     * @param animate  Should the transition be animated
     */
    void setSelectionInt(int position, boolean animate) {
        if (position != mOldSelectedPosition) {
            mBlockLayoutRequests = true;
            int delta = position - mSelectedPosition;
            setNextSelectedPositionInt(position);
            layout(delta, animate);
            mBlockLayoutRequests = false;
        }
    }

    public abstract void layout(int delta, boolean animate);

    public void setSelection(int position) {
        setSelectionInt(position, false);
    }

    /**
     * Clear out all children from the list
     */
    public void resetList() {
        mDataChanged = false;
        mNeedSync = false;

        removeAllViewsInLayout();
        mOldSelectedPosition = INVALID_POSITION;
        mOldSelectedRowId = INVALID_ROW_ID;

        setSelectedPositionInt(INVALID_POSITION);
        setNextSelectedPositionInt(INVALID_POSITION);
        invalidate();
    }

    /**
     * @see View#measure(int, int)
     * <p>
     * Figure out the dimensions of this Spinner. The width comes from the
     * widthMeasureSpec as Spinnners can't have their width set to
     * UNSPECIFIED. The height is based on the height of the selected item
     * plus padding.
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int widthSize;
        int heightSize;

        mSpinnerPadding.left = getPaddingLeft() > mSelectionLeftPadding ? getPaddingLeft()
                : mSelectionLeftPadding;
        mSpinnerPadding.top = getPaddingTop() > mSelectionTopPadding ? getPaddingTop()
                : mSelectionTopPadding;
        mSpinnerPadding.right = getPaddingRight() > mSelectionRightPadding ? getPaddingRight()
                : mSelectionRightPadding;
        mSpinnerPadding.bottom = getPaddingBottom() > mSelectionBottomPadding ? getPaddingBottom()
                : mSelectionBottomPadding;

        if (mDataChanged) {
            handleDataChanged();
        }

        int preferredHeight = 0;
        int preferredWidth = 0;
        boolean needsMeasuring = true;

        int selectedPosition = getSelectedItemPosition();
        if (selectedPosition >= 0 && mAdapter != null && selectedPosition < mAdapter.getCount()) {
            // Try looking in the recycler. (Maybe we were measured once
            // already)
            View view = mRecycler.get(selectedPosition);
            if (view == null) {
                // Make a new one
                view = mAdapter.getView(selectedPosition, null, this);
            }

            if (view != null) {
                // Put in recycler for re-measuring and/or layout
                mRecycler.put(selectedPosition, view);
            }

            if (view != null) {
                if (view.getLayoutParams() == null) {
                    mBlockLayoutRequests = true;
                    view.setLayoutParams(generateDefaultLayoutParams());
                    mBlockLayoutRequests = false;
                }
                measureChild(view, widthMeasureSpec, heightMeasureSpec);

                preferredHeight = getChildHeight(view) + mSpinnerPadding.top
                        + mSpinnerPadding.bottom;
                preferredWidth = getChildWidth(view) + mSpinnerPadding.left + mSpinnerPadding.right;

                needsMeasuring = false;
            }
        }

        if (needsMeasuring) {
            // No views -- just use padding
            preferredHeight = mSpinnerPadding.top + mSpinnerPadding.bottom;
            if (widthMode == View.MeasureSpec.UNSPECIFIED) {
                preferredWidth = mSpinnerPadding.left + mSpinnerPadding.right;
            }
        }

        preferredHeight = Math.max(preferredHeight, getSuggestedMinimumHeight());
        preferredWidth = Math.max(preferredWidth, getSuggestedMinimumWidth());

        heightSize = resolveSize(preferredHeight, heightMeasureSpec);
        widthSize = resolveSize(preferredWidth, widthMeasureSpec);

        setMeasuredDimension(widthSize, heightSize);
        mHeightMeasureSpec = heightMeasureSpec;
        mWidthMeasureSpec = widthMeasureSpec;
    }

    int getChildHeight(View child) {
        return child.getMeasuredHeight();
    }

    int getChildWidth(View child) {
        return child.getMeasuredWidth();
    }

    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        /*
		 * Carousel expects Carousel.LayoutParams.
		 */
        return new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    public void recycleAllViews() {
        final int childCount = getChildCount();
        final RecycleBin recycleBin = mRecycler;
        final int position = mFirstPosition;

        // All views go in recycler
        for (int i = 0; i < childCount; i++) {
            View v = getChildAt(i);
            int index = position + i;
            recycleBin.put(index, v);
        }
    }

    /**
     * Override to prevent spamming ourselves with layout requests as we place
     * views
     *
     * @see View#requestLayout()
     */
    @Override
    public void requestLayout() {
        if (!mBlockLayoutRequests) {
            super.requestLayout();
        }
    }

    @Override
    public int getCount() {
        return mItemCount;
    }

    public boolean judgePosition(int x, int y) {

        for (int i = 0; i < mAdapter.getCount(); i++) {

            CarouselItem item = (CarouselItem) getChildAt(i);

            Matrix mm = item.getCIMatrix();
            float[] pts = new float[3];

            pts[0] = item.getLeft();
            pts[1] = item.getTop();
            pts[2] = 0;

            mm.mapPoints(pts);

            int mappedLeft = (int) pts[0];
            int mappedTop = (int) pts[1];

            pts[0] = item.getRight();
            pts[1] = item.getBottom();
            pts[2] = 0;

            mm.mapPoints(pts);

            int mappedRight = (int) pts[0];
            int mappedBottom = (int) pts[1];

            if (mappedLeft < x && mappedRight > x & mappedTop < y && mappedBottom > y) {
                return true;
            }
        }
        return false;
    }

    /**
     * Maps a point to a position in the list.
     *
     * @param x X in local coordinate
     * @param y Y in local coordinate
     * @return The position of the item which contains the specified point, or
     * {@link #INVALID_POSITION} if the point does not intersect an
     * item.
     */
    public int pointToPosition(int x, int y) {

        ArrayList<CarouselItem> fitting = new ArrayList<CarouselItem>();

        for (int i = 0; i < mAdapter.getCount(); i++) {

            CarouselItem item = (CarouselItem) getChildAt(i);

            Matrix mm = item.getCIMatrix();
            float[] pts = new float[3];

            pts[0] = item.getLeft();
            pts[1] = item.getTop();
            pts[2] = 0;

            mm.mapPoints(pts);

            int mappedLeft = (int) pts[0];
            int mappedTop = (int) pts[1];

            pts[0] = item.getRight();
            pts[1] = item.getBottom();
            pts[2] = 0;

            mm.mapPoints(pts);

            int mappedRight = (int) pts[0];
            int mappedBottom = (int) pts[1];

            if (mappedLeft < x && mappedRight > x & mappedTop < y && mappedBottom > y) fitting
                    .add(item);

        }

        Collections.sort(fitting);

//PML
//		if (fitting.size() != 0) return fitting.get(0).getIndex();
//		else return mSelectedPosition;
        if (fitting.size() != 0) return fitting.get(fitting.size() - 1).getIndex();
        else return mSelectedPosition;
    }

    static class SavedState extends View.BaseSavedState {
        long selectedId;
        int position;

        /**
         * Constructor called from {@link AbsSpinner#onSaveInstanceState()}
         */
        SavedState(Parcelable superState) {
            super(superState);
        }

        /**
         * Constructor called from {@link #CREATOR}
         */
        private SavedState(Parcel in) {
            super(in);
            selectedId = in.readLong();
            position = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeLong(selectedId);
            out.writeInt(position);
        }

        @Override
        public String toString() {
            return "AbsSpinner.SavedState{" + Integer.toHexString(System.identityHashCode(this))
                    + " selectedId=" + selectedId + " position=" + position + "}";
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.selectedId = getSelectedItemId();
        if (ss.selectedId >= 0) {
            ss.position = getSelectedItemPosition();
        } else {
            ss.position = INVALID_POSITION;
        }
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;

        super.onRestoreInstanceState(ss.getSuperState());

        if (ss.selectedId >= 0) {
            mDataChanged = true;
            mNeedSync = true;
            mSyncRowId = ss.selectedId;
            mSyncPosition = ss.position;
            mSyncMode = SYNC_SELECTED_POSITION;
            requestLayout();
        }
    }

    public class RecycleBin {
        private final SparseArray<View> mScrapHeap = new SparseArray<View>();

        public void put(int position, View v) {
            mScrapHeap.put(position, v);
        }

        public View get(int position) {
            // System.out.print("Looking for " + position);
            View result = mScrapHeap.get(position);
            if (result != null) {
                // System.out.println(" HIT");
                mScrapHeap.delete(position);
            } else {
                // System.out.println(" MISS");
            }
            return result;
        }

        public void clear() {
            final SparseArray<View> scrapHeap = mScrapHeap;
            final int count = scrapHeap.size();
            for (int i = 0; i < count; i++) {
                final View view = scrapHeap.valueAt(i);
                if (view != null) {
                    removeDetachedView(view, true);
                }
            }
            scrapHeap.clear();
        }
    }
}
