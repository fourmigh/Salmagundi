package org.caojun.salmagundi.clicaptcha;

import android.content.Context;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;

/**
 * Created by CaoJun on 2017/6/26.
 */

public class CaptchaImageButton extends AppCompatImageButton {
    public CaptchaImageButton(Context context) {
        this(context, null);
    }

    public CaptchaImageButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CaptchaImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(Context context) {

    }
}
