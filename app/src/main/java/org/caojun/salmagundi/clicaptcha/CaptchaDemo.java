package org.caojun.salmagundi.clicaptcha;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import org.caojun.salmagundi.Constant;
import org.caojun.salmagundi.R;

/**
 * Created by CaoJun on 2017/6/28.
 */

@Route(path = Constant.ACTIVITY_CAPTCHA_DEMO)
public class CaptchaDemo extends Activity {

    private static final int RequestCode_Captcha = 1;

    private TextView tvInfo;
    private Button btnCaptcha;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_captcha_demo);

        tvInfo = (TextView) findViewById(R.id.tvInfo);
        btnCaptcha = (Button) findViewById(R.id.btnCaptcha);

        btnCaptcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCaptcha();
            }
        });
    }

    private void doCaptcha() {
        ARouter.getInstance().build(Constant.ACTIVITY_CAPTCHA)
                .withInt(CaptchaActivity.FailureTimes, 3)
                .navigation(this, RequestCode_Captcha);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode_Captcha) {
            if (resultCode == Activity.RESULT_OK) {
                byte responseCode = data.getByteExtra(CaptchaActivity.ResponseCode, CaptchaActivity.Result_Fail);
                switch (responseCode) {
                    case CaptchaActivity.Result_Success:
                        tvInfo.setText("Captcha Success");
                        break;
                    case CaptchaActivity.Result_Fail:
                        tvInfo.setText("Captcha Fail");
                        break;
                    case CaptchaActivity.Result_Cancel:
                        tvInfo.setText("Captcha Cancel");
                        break;
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
