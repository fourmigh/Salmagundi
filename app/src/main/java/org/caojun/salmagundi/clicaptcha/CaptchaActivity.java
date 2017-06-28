package org.caojun.salmagundi.clicaptcha;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.alibaba.android.arouter.facade.annotation.Route;
import org.caojun.salmagundi.Constant;
import org.caojun.salmagundi.R;

/**
 * Created by CaoJun on 2017/6/26.
 */

@Route(path = Constant.ACTIVITY_CAPTCHA)
public class CaptchaActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_captcha);
    }
}
