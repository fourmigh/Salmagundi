package org.caojun.salmagundi.string;

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
 * Created by CaoJun on 2017/8/18.
 */

@Route(path = Constant.ACTIVITY_STRINGSORT_DEMO)
public class StringSortDemo extends Activity {

    private static final int RequestCode_Sort = 1;
    private TextView tvInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_captcha_demo);



        tvInfo = (TextView) findViewById(R.id.tvInfo);
        Button btnCaptcha = (Button) findViewById(R.id.btnCaptcha);

        btnCaptcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCaptcha();
            }
        });
    }

    private void doCaptcha() {
        int[] ids = {1211,232,3432,412,54,673,7932,832};
        String[] strings = {"ab12cd", "abcd", "查良镛", "曹珺", "abc曹珺", "123曹珺", "曹abc珺", "查良查"};

        ARouter.getInstance().build(Constant.ACTIVITY_STRINGSORT)
                .withObject("ids", ids)
                .withObject("strings", strings)
                .navigation(this, RequestCode_Sort);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode_Sort && resultCode == Activity.RESULT_OK && data != null) {
            tvInfo.setText("id: " + data.getIntExtra("id", Integer.MIN_VALUE) + " , " + "text: " + data.getStringExtra("text"));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
