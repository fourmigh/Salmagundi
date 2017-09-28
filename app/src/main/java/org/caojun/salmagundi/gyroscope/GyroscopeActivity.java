package org.caojun.salmagundi.gyroscope;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;

import org.caojun.salmagundi.Constant;
import org.caojun.salmagundi.R;

/**
 * Created by CaoJun on 2017/9/28.
 */
@Route(path = Constant.ACTIVITY_GYROSCOPE)
public class GyroscopeActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, GyroscopeKotlinActivity.class));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }
    }
}
