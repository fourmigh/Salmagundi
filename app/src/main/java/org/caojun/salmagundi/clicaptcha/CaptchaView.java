package org.caojun.salmagundi.clicaptcha;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.socks.library.KLog;

/**
 * Created by CaoJun on 2017/6/26.
 */

public class CaptchaView extends View {

    public interface OnCodeClickListener {
        void onClick(String s);
    }

    private int mWidth, mHeight;

    private CaptchaImageButton[] buttons;

    public CaptchaView(Context context) {
        this(context, null);
    }

    public CaptchaView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CaptchaView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mWidth = 400;
        mHeight = 400;
        init(context, 4, new OnCodeClickListener() {
            @Override
            public void onClick(String s) {
                KLog.d("OnCodeClickListener", s);
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    public void init(Context context, int n, final OnCodeClickListener onCodeClickListener) {
        if (n <= 0 || onCodeClickListener == null) {
            return;
        }
        buttons = new CaptchaImageButton[n];
        for (int i = 0;i < n;i ++) {
            buttons[i] = new CaptchaImageButton(context);
            final int index = i;
            buttons[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCodeClickListener.onClick(buttons[index].getCode());
                }
            });
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0;i < buttons.length;i ++) {
            buttons[i].draw(canvas);
        }
    }
}
