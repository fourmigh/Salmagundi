package org.caojun.salmagundi.decibelman;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.socks.library.KLog;

import org.caojun.library.VelocimeterView;
import org.caojun.salmagundi.Constant;
import org.caojun.salmagundi.R;

/**
 * Created by CaoJun on 2017/9/11.
 */
@Route(path = Constant.ACTIVITY_DECIBELMAN)
public class DecibelmanActivity extends Activity {

    private Decibelman decibelman;
    private VelocimeterView velocimeterView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_decibelman);

        velocimeterView = findViewById(R.id.velocimeterView);

        decibelman = new Decibelman(new Decibelman.OnDecibelListener() {
            @Override
            public void onGetDecibel(double decibel) {
//                velocimeterView.setValue((float)decibel, false);
            }
        });
        velocimeterView.setValue(120f, true);
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
