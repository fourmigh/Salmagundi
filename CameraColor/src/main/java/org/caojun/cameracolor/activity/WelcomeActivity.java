package org.caojun.cameracolor.activity;

import android.Manifest;
import android.os.Bundle;
import com.alibaba.android.arouter.launcher.ARouter;
import org.caojun.activity.BaseActivity;
import org.caojun.cameracolor.Constant;
import org.caojun.cameracolor.R;
import org.caojun.utils.ActivityUtils;

/**
 * Created by CaoJun on 2018-3-23.
 */

public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        this.checkSelfPermission(Manifest.permission.CAMERA, new ActivityUtils.RequestPermissionListener() {

            @Override
            public void onFail() {
                finish();
            }

            @Override
            public void onSuccess() {
                ARouter.getInstance().build(Constant.ACTIVITY_MAIN).navigation();
                finish();
            }
        });
    }
}
