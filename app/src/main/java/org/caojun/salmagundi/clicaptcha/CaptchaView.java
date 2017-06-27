package org.caojun.salmagundi.clicaptcha;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.socks.library.KLog;

import java.util.Random;

/**
 * Created by CaoJun on 2017/6/26.
 */

public class CaptchaView extends View {

    public interface OnCodeClickListener {
        void onClick(String s);
    }

    private static final byte N = 4;
    private static final int SIZE = N * CaptchaImageButton.SIZE;
    //将背景分成WIDTH*HEIGHT个格子
    private static final byte WIDTH = N + 2;
    private static final byte HEIGHT = N + 1;
    private static final byte COUNT = WIDTH * HEIGHT;
    private byte[] idCount = new byte[COUNT];

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
        init(context, N, new OnCodeClickListener() {
            @Override
            public void onClick(String s) {
                KLog.d("OnCodeClickListener", s);
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(WIDTH * CaptchaImageButton.SIZE, HEIGHT * CaptchaImageButton.SIZE);
    }

    public void init(Context context, byte n, final OnCodeClickListener onCodeClickListener) {
        if (n <= 0 || onCodeClickListener == null) {
            return;
        }
        buttons = new CaptchaImageButton[n];
        for (byte i = 0;i < n;i ++) {
            buttons[i] = new CaptchaImageButton(context);
            final int index = i;
            buttons[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCodeClickListener.onClick(buttons[index].getCode());
                }
            });
            int id = getRandomId();
            KLog.d("getRandom", "id: " + id);
            while (idCount[id] > 0) {
                id = getRandomId();
            }
            idCount[id] = (byte)(i + 1);
        }
    }

    private int getRandomId() {
        int id = CaptchaUtils.getRandom(WIDTH + 1, COUNT - WIDTH - 2);
        while (id % WIDTH == 0 || id % WIDTH == WIDTH - 1) {
            id = CaptchaUtils.getRandom(WIDTH + 1, COUNT - WIDTH - 2);
        }
        return id;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        for (int i = 0;i < buttons.length;i ++) {
//            buttons[i].draw(canvas);
//            KLog.d("onDraw", i + " : " + buttons[i].getCode());
//            canvas.translate(buttons[i].getSize(), buttons[i].getSize());
//        }
        canvas.drawColor(0xfff9dec1);
        for (byte i = 0;i < COUNT;i ++) {
            if (idCount[i] == 0) {
                continue;
            }
            int id = idCount[i] - 1;
            int x0 = i % WIDTH;
            int y0 = i / WIDTH;
            int x = x0 * CaptchaImageButton.SIZE;
            int y = y0 * CaptchaImageButton.SIZE;
            canvas.translate(x , y);
            buttons[id].draw(canvas);
            canvas.translate(-x, -y);
            KLog.d("onDraw", id + " : " + buttons[id].getCode());
            KLog.d("onDraw", x0 + " : " + y0);
        }
    }
}
