package org.caojun.salmagundi.decibelman;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.socks.library.KLog;

import org.caojun.salmagundi.Constant;

/**
 * Created by CaoJun on 2017/9/11.
 */
@Route(path = Constant.ACTIVITY_DECIBELMAN)
public class DecibelmanActivity extends Activity {

    private Decibelman decibelman;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        decibelman = new Decibelman(new Decibelman.OnDecibelListener() {
            @Override
            public void onGetDecibel(double decibel) {
                KLog.d("DecibelmanActivity", "分贝值: " + decibel);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        decibelman.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        decibelman.stop();
    }
}
