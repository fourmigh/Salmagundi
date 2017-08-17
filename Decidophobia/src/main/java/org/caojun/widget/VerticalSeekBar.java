package org.caojun.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;

/**
 * Created by CaoJun on 2017/8/14.
 */

public class VerticalSeekBar extends AppCompatSeekBar {

    private static final byte FingerHeight = 80;//手指尺寸
    private boolean mIsDragging;
    private boolean isDown = true;

    public VerticalSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        for (int i =0; i < attrs.getAttributeCount(); i++) {
            if (attrs.getAttributeName(i).equals("textDirection")) {
                //有设置该属性，即认为是向上
                isDown = false;
                break;
            }
        }
    }

    public VerticalSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalSeekBar(Context context) {
        this(context, null, 0);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, w, oldh, oldw);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec,
                                          int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        if (isDown) {
            canvas.rotate(90);
            canvas.translate(0, -getWidth());
        } else {
            canvas.rotate(-90);
            canvas.translate(-getHeight(), 0);
        }

        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setPressed(true);

                invalidate();
                onStartTrackingTouch();
                trackTouchEvent(event);
                attemptClaimDrag();

                onSizeChanged(getWidth(), getHeight(), 0, 0);
                break;

            case MotionEvent.ACTION_MOVE:
                trackTouchEvent(event);
                onSizeChanged(getWidth(), getHeight(), 0, 0);
                break;

            case MotionEvent.ACTION_UP:
                if (mIsDragging) {
                    trackTouchEvent(event);
                    onStopTrackingTouch();
                    setPressed(false);

                } else {
                    // Touch up when we never crossed the touch slop threshold
                    // should
                    // be interpreted as a tap-seek to that location.
                    onStartTrackingTouch();
                    trackTouchEvent(event);
                    onStopTrackingTouch();

                }
                onSizeChanged(getWidth(), getHeight(), 0, 0);
                // ProgressBar doesn't know to repaint the thumb drawable
                // in its inactive state when the touch stops (because the
                // value has not apparently changed)
                invalidate();
                break;
        }
        return true;

    }

    private void trackTouchEvent(MotionEvent event) {
        final int height = getHeight();
        final int top = getPaddingTop();
        final int bottom = getPaddingBottom();
        final int available = height - top - bottom;

        int y = (int) event.getY();
        if (isDown) {
            y += FingerHeight;
        } else {
            y -= FingerHeight;
        }

        float scale;
        float progress = 0;

        // 下面是最小值
        if (y > height - bottom) {
            if (isDown) {
                scale = 1.0f;
            } else {
                scale = 0.0f;
            }
        } else if (y < top) {
            if (isDown) {
                scale = 0.0f;
            } else {
                scale = 1.0f;
            }
        } else {
            if (isDown) {
                scale = (float) y / available;
            } else {
                scale = (float) (available - y + top) / (float) available;
            }
        }

        final int max = getMax();
        progress += scale * max;

        setProgress((int) progress);

    }

    /**
     * This is called when the user has started touching this widget.
     */
    void onStartTrackingTouch() {
        mIsDragging = true;
    }

    /**
     * This is called when the user either releases his touch or the touch is
     * canceled.
     */
    void onStopTrackingTouch() {
        mIsDragging = false;
    }

    private void attemptClaimDrag() {
        ViewParent p = getParent();
        if (p != null) {
            p.requestDisallowInterceptTouchEvent(true);
        }
    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);
        onSizeChanged(getWidth(), getHeight(), 0, 0);
    }
}
