package org.caojun.salmagundi.demo;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;

import org.caojun.activity.BaseActivity;
import org.caojun.salmagundi.Constant;
import org.caojun.salmagundi.R;

/**
 * Created by CaoJun on 2018-3-15.
 */
@Route(path = Constant.ACTIVITY_DEMO)
public class DemoActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        final TextView textView = findViewById(R.id.textView);
        getIMEI(new RequestPermissionListener() {
            @Override
            public void onSuccess() {
                TelephonyManager tm = (TelephonyManager)DemoActivity.this.getSystemService(Context.TELEPHONY_SERVICE);
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        textView.setText(tm.getImei());
                    } else {
                        textView.setText(tm.getDeviceId());
                    }
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail() {
                Toast.makeText(DemoActivity.this, R.string.imei_permission, Toast.LENGTH_LONG).show();
            }
        });
    }
}
