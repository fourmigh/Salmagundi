package org.caojun.salmagundi.zxing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.client.android.CaptureActivity;

import org.caojun.salmagundi.BaseActivity;
import org.caojun.salmagundi.R;

/**
 * 扫码
 * Created by CaoJun on 2016/11/29.
 */

public class ZXingActivity extends BaseActivity {

    private static final int RequestCode_ZXing = 1;
    private LinearLayout llZXing;
    private RelativeLayout rlZXing;
    private TextView tvZXing;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_zxing);

        llZXing = (LinearLayout) this.findViewById(R.id.llZXing);
        rlZXing = (RelativeLayout) this.findViewById(R.id.rlZXing);
        tvZXing = (TextView) this.findViewById(R.id.tvZXing);
        final ImageView ivZXing = (ImageView) this.findViewById(R.id.ivZXing);
        ivZXing.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void startZXing()
    {
        Intent intent = new Intent(this, CaptureActivity.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RequestCode_ZXing && resultCode == Activity.RESULT_OK && data != null)
        {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
