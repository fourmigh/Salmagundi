package org.caojun.salmagundi.clicaptcha;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by CaoJun on 2017/6/26.
 */

public class CaptchaView extends AppCompatImageView {

    public interface OnCaptchaListener {
        void onError(int count);
        void onSuccess(int index);
    }

    public static final byte N = 4;
    //将背景分成WIDTH*HEIGHT个格子
    private static final byte WIDTH = N + 2;
    private static final byte HEIGHT = N + 1;
    private static final byte COUNT = WIDTH * HEIGHT;
    private byte[] idCount = new byte[COUNT];

    private CaptchaImage[] buttons;
    private OnCaptchaListener onCaptchaListener;
    private int index, count;

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

    public void setOnCaptchaListener(OnCaptchaListener onCaptchaListener) {
        this.onCaptchaListener = onCaptchaListener;
        index = 0;
        count = 0;
    }

    public void refresh() {
        index = 0;
        count = 0;
        for (byte i = 0;i < idCount.length;i ++) {
            idCount[i] = 0;
        }
        for (byte i = 0;i < N;i ++) {
            buttons[i].refresh();
            int id = getRandomId();
            while (idCount[id] > 0) {
                id = getRandomId();
            }
            idCount[id] = (byte)(i + 1);
        }
        this.invalidate();
    }

    private void initView(Context context) {
        buttons = new CaptchaImage[N];
        for (byte i = 0;i < N;i ++) {
            buttons[i] = new CaptchaImage(context);
            int id = getRandomId();
            while (idCount[id] > 0) {
                id = getRandomId();
            }
            idCount[id] = (byte)(i + 1);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(WIDTH * CaptchaImage.SIZE, HEIGHT * CaptchaImage.SIZE);
    }

    private int getRandomId() {
        int id = CaptchaUtils.getRandom(WIDTH + 1, COUNT - WIDTH - 2);
        while (id % WIDTH == 0 || id % WIDTH == WIDTH - 1 || hasNeighbor(id)) {
            id = CaptchaUtils.getRandom(WIDTH + 1, COUNT - WIDTH - 2);
        }
        return id;
    }

    private boolean hasNeighbor(int id) {
        int left = id - 1;
        if (left % WIDTH < WIDTH - 1) {
            //有左邻居
            if (idCount[left] > 0) {
                return true;
            }
        }
        int right = id  + 1;
        if (right % WIDTH > 0) {
            //有右邻居
            if (idCount[right] > 0) {
                return true;
            }
        }
        int up = id - WIDTH;
        if (up >= 0) {
            //有上邻居
            if (idCount[up] > 0) {
                return true;
            }
        }
        int down = id + WIDTH;
        if (down < COUNT) {
            //有下邻居
            if (idCount[down] > 0) {
                return true;
            }
        }
        return false;
    }

    public String[] getCode() {
        if (buttons == null) {
            return null;
        }
        String[] strings = new String[buttons.length];
        for (int i = 0;i < buttons.length;i ++) {
            strings[i] = buttons[i].getCode();
        }
        return strings;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (byte i = 0;i < COUNT;i ++) {
            if (idCount[i] == 0) {
                continue;
            }
            int id = idCount[i] - 1;
            int x0 = i % WIDTH;
            int y0 = i / WIDTH;
            int x = x0 * CaptchaImage.SIZE;
            int y = y0 * CaptchaImage.SIZE;
            canvas.translate(x , y);
            buttons[id].draw(canvas);
            canvas.translate(-x, -y);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int id = getButtonID(event.getX(), event.getY());
            if (id > 0 && onCaptchaListener != null) {
                if (id - 1 == index) {
                    index ++;
                    onCaptchaListener.onSuccess(index);
                    if (index == N) {
                        count = 0;
                        index = 0;
                    }
                } else {
                    index = 0;
                    count ++;
                    onCaptchaListener.onError(count);
                }
                return false;
            }
        }
        return true;
    }

    private int getButtonID(float x, float y) {
        int x0 = (int)(x / CaptchaImage.SIZE);
        int y0 = (int)(y / CaptchaImage.SIZE);
        int id = y0 * WIDTH + x0;
        return idCount[id];
    }
}
