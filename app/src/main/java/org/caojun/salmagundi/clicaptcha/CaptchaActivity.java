package org.caojun.salmagundi.clicaptcha;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.socks.library.KLog;

import org.caojun.salmagundi.BaseActivity;
import org.caojun.salmagundi.Constant;

/**
 * Created by CaoJun on 2017/6/26.
 */

@Route(path = Constant.ACTIVITY_CAPTCHA)
public class CaptchaActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (int i = 0;i < 10;i ++) {
            KLog.d("Captcha", i + " : " + CaptchaUtils.getRandomChar());
        }
    }
}
