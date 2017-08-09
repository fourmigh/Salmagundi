package org.caojun.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioGroup;

import org.caojun.rcn.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 自动换行的RadioGroup
 * Created by CaoJun on 2017/8/9.
 */

public class MultiRadioGroup extends RadioGroup implements View.OnClickListener {

    public final int LEFT = 1;
    public final int CENTER = 0;
    public final int RIGHT = 2;
    private int mX, mY;
    private List<CheckBox> viewList;
    private int childMarginHorizontal = 0;
    private int childMarginVertical = 0;
    private int childResId = 0;
    private int childCount = 0;
    private int childValuesId = 0;
    private boolean singleChoice = false;
    private int mLastCheckedPosition = -1;
    private int rowNumber = 0;
    private int gravity = LEFT;
    private OnCheckedChangedListener listener;
    private List<String> childValues;
    private boolean forceLayout;

    public MultiRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        viewList = new ArrayList<CheckBox>();
        childValues = new ArrayList<String>();
        TypedArray arr = context.obtainStyledAttributes(attrs,
                R.styleable.MultiRadioGroup);
        childMarginHorizontal = arr.getDimensionPixelSize(
                R.styleable.MultiRadioGroup_child_margin_horizontal, 15);
        childMarginVertical = arr.getDimensionPixelSize(
                R.styleable.MultiRadioGroup_child_margin_vertical, 5);
        childResId = arr.getResourceId(
                R.styleable.MultiRadioGroup_child_layout, 0);
        childCount = arr.getInt(R.styleable.MultiRadioGroup_child_count, 0);
        singleChoice = arr.getBoolean(
                R.styleable.MultiRadioGroup_single_choice, true);
        childValuesId = arr.getResourceId(
                R.styleable.MultiRadioGroup_child_values, 0);
        gravity = arr.getInt(R.styleable.MultiRadioGroup_gravity, LEFT);
//        if (childResId == 0) {
//            throw new RuntimeException(
//                    "The attr 'child_layout' must be specified!");
//        }
        if (childValuesId != 0) {
            String[] childValues_ = getResources()
                    .getStringArray(childValuesId);
            for (String str : childValues_) {
                childValues.add(str);
            }
        }
        if (childCount > 0) {
            boolean hasValues = childValues != null;
            for (int i = 0; i < childCount; i++) {
                CheckBox cb = getChild();
                viewList.add(cb);
                addView(cb);
                if (hasValues && i < childValues.size()) {
                    cb.setText(childValues.get(i));
                } else {
                    childValues.add(cb.getText().toString());
                }
                cb.setTag(i);
                cb.setOnClickListener(this);
            }
        }
        arr.recycle();
    }

    public MultiRadioGroup(Context context) {
        this(context, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        childCount = getChildCount();
        int flagX = 0, flagY = 0, sheight = 0;
        if (childCount > 0) {
            for (int i = 0; i < childCount; i++) {
                View v = getChildAt(i);
                measureChild(v, widthMeasureSpec, heightMeasureSpec);
                int w = v.getMeasuredWidth() + childMarginHorizontal * 2
                        + flagX + getPaddingLeft() + getPaddingRight();
                if (w > getMeasuredWidth()) {
                    flagY++;
                    flagX = 0;
                }
                sheight = v.getMeasuredHeight();
                flagX += v.getMeasuredWidth() + childMarginHorizontal * 2;
            }
            rowNumber = flagY;
        }
        int height = (flagY + 1) * (sheight + childMarginVertical)
                + childMarginVertical + getPaddingBottom() + getPaddingTop();
        setMeasuredDimension(getMeasuredWidth(), height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!changed && !forceLayout) {
            return;
        }
        childCount = getChildCount();
        int[] sX = new int[rowNumber + 1];
        if (childCount > 0) {
            if (gravity != LEFT) {
                for (int i = 0; i < childCount; i++) {
                    View v = getChildAt(i);
                    int w = v.getMeasuredWidth() + childMarginHorizontal * 2
                            + mX + getPaddingLeft() + getPaddingRight();
                    if (w > getWidth()) {
                        if (gravity == CENTER) {
                            sX[mY] = (getWidth() - mX) / 2;
                        } else { // right
                            sX[mY] = (getWidth() - mX);
                        }
                        mY++;
                        mX = 0;
                    }
                    mX += v.getMeasuredWidth() + childMarginHorizontal * 2;
                    if (i == childCount - 1) {
                        if (gravity == CENTER) {
                            sX[mY] = (getWidth() - mX) / 2;
                        } else { // right
                            sX[mY] = (getWidth() - mX);
                        }
                    }
                }
                mX = mY = 0;
            }
            for (int i = 0; i < childCount; i++) {
                View v = getChildAt(i);
                int w = v.getMeasuredWidth() + childMarginHorizontal * 2 + mX
                        + getPaddingLeft() + getPaddingRight();
                if (w > getWidth()) {
                    mY++;
                    mX = 0;
                }
                int startX = mX + childMarginHorizontal + getPaddingLeft()
                        + sX[mY];
                int startY = mY * v.getMeasuredHeight() + (mY + 1)
                        * childMarginVertical;
                v.layout(startX, startY, startX + v.getMeasuredWidth(), startY
                        + v.getMeasuredHeight());
                mX += v.getMeasuredWidth() + childMarginHorizontal * 2;
            }
        }
        mX = mY = 0;
        forceLayout = false;
    }

    @Override
    public void onClick(View v) {
        try {
            if (singleChoice) { // singleChoice
                int i = (Integer) v.getTag();
                boolean checked = ((CheckBox) v).isChecked();
                if (mLastCheckedPosition != -1 && mLastCheckedPosition != i) {
                    viewList.get(mLastCheckedPosition).setChecked(false);
                }
                if (checked) {
                    mLastCheckedPosition = i;
                } else {
                    mLastCheckedPosition = -1;
                }
                if (listener != null) {
                    listener.onItemChecked(this, i, checked);
                }
            } else { // multiChoice
                int i = (Integer) v.getTag();
                CheckBox cb = (CheckBox) v;
                if (null != listener) {
                    listener.onItemChecked(this, i, cb.isChecked());
                }
            }
        } catch (Exception e) {
        }
    }

    public void setOnCheckChangedListener(OnCheckedChangedListener l) {
        this.listener = l;
    }

    public boolean setItemChecked(int position) {
        if (position >= 0 && position < viewList.size()) {
            if (singleChoice) {
                if (position == mLastCheckedPosition) {
                    return true;
                }
                if (mLastCheckedPosition >= 0
                        && mLastCheckedPosition < viewList.size()) {
                    viewList.get(mLastCheckedPosition).setChecked(false);
                }
                mLastCheckedPosition = position;
            }
            viewList.get(position).setChecked(true);
            return true;
        }
        return false;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
        forceLayout = true;
        requestLayout();
    }

    public boolean isSingleChoice() {
        return singleChoice;
    }

    public void setChoiceMode(boolean isSingle) {
        this.singleChoice = isSingle;
        if (singleChoice) {
            if (getCheckedValues().size() > 1) {
                clearChecked();
            }
        }
    }

    public int[] getCheckedItems() {
        if (singleChoice && mLastCheckedPosition >= 0
                && mLastCheckedPosition < viewList.size()) {
            return new int[]{mLastCheckedPosition};
        }
        SparseIntArray arr = new SparseIntArray();
        for (int i = 0; i < viewList.size(); i++) {
            if (viewList.get(i).isChecked()) {
                arr.put(i, i);
            }
        }
        if (arr.size() != 0) {
            int[] r = new int[arr.size()];
            for (int i = 0; i < arr.size(); i++) {
                r[i] = arr.keyAt(i);
            }
            return r;
        }
        return null;
    }

    public List<String> getCheckedValues() {
        List<String> list = new ArrayList<String>();
        if (singleChoice && mLastCheckedPosition >= 0
                && mLastCheckedPosition < viewList.size()) {
            list.add(viewList.get(mLastCheckedPosition).getText().toString());
            return list;
        }
        for (int i = 0; i < viewList.size(); i++) {
            if (viewList.get(i).isChecked()) {
                list.add(viewList.get(i).getText().toString());
            }
        }
        return list;
    }

    public int append(String str) {
        CheckBox cb = getChild();
        cb.setText(str);
        cb.setTag(childCount);
        cb.setOnClickListener(this);
        viewList.add(cb);
        addView(cb);
        childValues.add(str);
        childCount++;
        forceLayout = true;
        postInvalidate();
        return childCount - 1;
    }

    public void addAll(List<String> list) {
        if (list != null && list.size() > 0) {
            for (String str : list) {
                append(str);
            }
        }
    }

    public boolean remove(int position) {
        if (position >= 0 && position < viewList.size()) {
            CheckBox cb = viewList.remove(position);
            removeView(cb);
            childValues.remove(cb.getText().toString());
            childCount--;
            forceLayout = true;
            if (position <= mLastCheckedPosition) { // before LastCheck
                if (mLastCheckedPosition == position) {
                    mLastCheckedPosition = -1;
                } else {
                    mLastCheckedPosition--;
                }
            }
            for (int i = position; i < viewList.size(); i++) {
                viewList.get(i).setTag(i);
            }
            postInvalidate();
            return true;
        }
        return false;
    }

    public boolean insert(int position, String str) {
        if (position < 0 || position > viewList.size()) {
            return false;
        }
        CheckBox cb = getChild();
        cb.setText(str);
        cb.setTag(position);
        cb.setOnClickListener(this);
        viewList.add(position, cb);
        addView(cb, position);
        childValues.add(position, str);
        childCount++;
        forceLayout = true;
        if (position <= mLastCheckedPosition) { // before LastCheck
            mLastCheckedPosition++;
        }
        for (int i = position; i < viewList.size(); i++) {
            viewList.get(i).setTag(i);
        }
        postInvalidate();
        return true;
    }

    private CheckBox getChild() {
        View v = LayoutInflater.from(getContext()).inflate(childResId, this,
                false);
        if (!(v instanceof CheckBox)) {
            throw new RuntimeException(
                    "The attr child_layout's root must be a CheckBox!");
        }
        CheckBox cb = (CheckBox) v;
        return cb;
    }

    public void clearChecked() {
        if (singleChoice) {
            if (mLastCheckedPosition >= 0
                    && mLastCheckedPosition < viewList.size()) {
                viewList.get(mLastCheckedPosition).setChecked(false);
                mLastCheckedPosition = -1;
                return;
            }
        }
        for (CheckBox cb : viewList) {
            if (cb.isChecked()) {
                cb.setChecked(false);
            }
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (null != viewList && viewList.size() > 0) {
            for (View v : viewList) {
                v.setEnabled(enabled);
            }
        }
    }

    public interface OnCheckedChangedListener {
        void onItemChecked(MultiRadioGroup group, int position,
                                  boolean checked);
    }
}
