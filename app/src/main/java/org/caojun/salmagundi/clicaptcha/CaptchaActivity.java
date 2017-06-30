package org.caojun.salmagundi.clicaptcha;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

import org.caojun.salmagundi.Constant;
import org.caojun.salmagundi.R;

/**
 * Created by CaoJun on 2017/6/26.
 */

@Route(path = Constant.ACTIVITY_CAPTCHA)
public class CaptchaActivity extends Activity {

    public static final String ResponseCode = "ResponseCode";
    public static final String FailureTimes = "failureTimes";//可失败的次数
    public static final byte Result_Success = 0;
    public static final byte Result_Fail = 1;
    public static final byte Result_Cancel = 2;

    @Autowired
    protected int failureTimes;

    private CaptchaView captchaView;
    private TextView tvInfo;
    private ImageView ivRefresh;

    private final int[] idInfo = {R.string.captcha_check_info_0, R.string.captcha_check_info_1, R.string.captcha_check_info_2, R.string.captcha_check_info_3, R.string.captcha_check_info_4};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_captcha);
        ARouter.getInstance().inject(this);

        this.setFinishOnTouchOutside(false);

        if (failureTimes == 0) {
            failureTimes = getIntent().getIntExtra(FailureTimes, 0);
        }

        captchaView = (CaptchaView) findViewById(R.id.captchaView);
        tvInfo = (TextView) findViewById(R.id.tvInfo);
        ivRefresh = (ImageView) findViewById(R.id.ivRefresh);

        refresh();

        captchaView.setOnCaptchaListener(new CaptchaView.OnCaptchaListener() {
            @Override
            public void onError(int count) {
                String[] code = captchaView.getCode();
                tvInfo.setText(Html.fromHtml(getString(idInfo[0], code[0], code[1], code[2], code[3])));
                if (failureTimes > 0 && count >= failureTimes) {
                    doFinish(Result_Fail);
                }
            }

            @Override
            public void onSuccess(int index) {
                String[] code = captchaView.getCode();
                tvInfo.setText(Html.fromHtml(getString(idInfo[index], code[0], code[1], code[2], code[3])));
                if (index >= CaptchaView.N) {
                    doFinish(Result_Success);
                }
            }
        });

        ivRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captchaView.refresh();
                refresh();
            }
        });
    }

    @Override
    public void onBackPressed() {
        doFinish(Result_Cancel);
    }

    private void doFinish(byte responseCode) {
        Intent intent = new Intent();
        intent.putExtra(ResponseCode, responseCode);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void refresh() {
        String[] code = captchaView.getCode();
        if (code == null) {
            tvInfo.setVisibility(View.GONE);
        } else {
            tvInfo.setText(Html.fromHtml(getString(idInfo[0], code[0], code[1], code[2], code[3])));
            tvInfo.setVisibility(View.VISIBLE);
        }
    }
}
