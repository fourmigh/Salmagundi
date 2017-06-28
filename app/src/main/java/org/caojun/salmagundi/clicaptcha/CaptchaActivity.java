package org.caojun.salmagundi.clicaptcha;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import com.alibaba.android.arouter.facade.annotation.Route;
import org.caojun.salmagundi.Constant;
import org.caojun.salmagundi.R;

/**
 * Created by CaoJun on 2017/6/26.
 */

@Route(path = Constant.ACTIVITY_CAPTCHA)
public class CaptchaActivity extends Activity {

    private CaptchaView captchaView;
    private TextView tvInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_captcha);

        captchaView = (CaptchaView) findViewById(R.id.captchaView);
        tvInfo = (TextView) findViewById(R.id.tvInfo);

        refresh();

        captchaView.setOnCaptchaListener(new CaptchaView.OnCaptchaListener() {
            @Override
            public void onError(int count) {
            }

            @Override
            public void onSuccess() {
                tvInfo.setText("Success");
            }
        });

        tvInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captchaView.refresh();
                refresh();
            }
        });
    }

    private void refresh() {
        String[] code = captchaView.getCode();
        if (code == null) {
            tvInfo.setVisibility(View.GONE);
        } else {
            tvInfo.setText(Html.fromHtml(getString(R.string.captcha_check_info, code[0], code[1], code[2], code[3])));
            tvInfo.setVisibility(View.VISIBLE);
        }
    }
}
