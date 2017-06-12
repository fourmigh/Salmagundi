package org.caojun.salmagundi.lockpattern;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;

import org.caojun.salmagundi.BaseActivity;
import org.caojun.salmagundi.R;
import org.caojun.salmagundi.lockpattern.utils.LockPatternUtils;
import org.caojun.salmagundi.lockpattern.widget.LockPatternView;
import org.caojun.salmagundi.utils.DataStorageUtils;

import java.util.List;

/**
 * 手势密码登录
 * Created by CaoJun on 2017/2/21.
 */

@Route(path = "/main/gesture/login")
public class GestureLoginActivity extends BaseActivity {

    private static final long DELAY_TIME = 600l;
    private byte[] gesturePassword;
    private String hostGesture;

    private LockPatternView lockPatternView;
    private TextView tvMessage;
    private Button btnForgetGesture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_login);

        lockPatternView = (LockPatternView) this.findViewById(R.id.lockPatternView);
        tvMessage = (TextView) this.findViewById(R.id.tvMessage);
        btnForgetGesture = (Button) this.findViewById(R.id.btnForgetGesture);

        btnForgetGesture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doForgetGesture();
            }
        });

        hostGesture = getIntent().getStringExtra(GestureConstant.HostGesture);
        if (TextUtils.isEmpty(hostGesture)) {
            hostGesture = this.getClass().getName();
        }

        lockPatternView.setOnPatternListener(patternListener);
        updateStatus(Status.DEFAULT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gesturePassword = DataStorageUtils.loadByteArray(this, GestureConstant.DataGesture, hostGesture, Byte.MIN_VALUE);
        if (needSetGesture()) {
            doForgetGesture();
        }
    }

    private LockPatternView.OnPatternListener patternListener = new LockPatternView.OnPatternListener() {

        @Override
        public void onPatternStart() {
            lockPatternView.removePostClearPatternRunnable();
        }

        @Override
        public void onPatternComplete(List<LockPatternView.Cell> pattern) {
            if(pattern != null){
                if(LockPatternUtils.checkPattern(pattern, gesturePassword)) {
                    updateStatus(Status.CORRECT);
                } else {
                    updateStatus(Status.ERROR);
                }
            }
        }
    };

    private void updateStatus(Status status) {
        tvMessage.setText(status.msgId);
        tvMessage.setTextColor(Color.parseColor(status.color));
        switch (status) {
            case DEFAULT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case ERROR:
                lockPatternView.setPattern(LockPatternView.DisplayMode.ERROR);
                lockPatternView.postClearPatternRunnable(DELAY_TIME);
                break;
            case CORRECT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                this.setResult(Activity.RESULT_OK);
                this.finish();
                break;
        }
    }

    private boolean needSetGesture() {
        if(gesturePassword == null) {
            return true;
        }
        for(int i = 0;i < gesturePassword.length;i ++) {
            if(gesturePassword[i] != Byte.MIN_VALUE) {
                return false;
            }
        }
        return true;
    }

    private void doForgetGesture() {
        Intent intent = new Intent(this, CreateGestureActivity.class);
        intent.putExtra(GestureConstant.HostGesture, hostGesture);
        this.startActivityForResult(intent, GestureConstant.RequestCode_GestureCreate);
    }

    private enum Status {
        //默认的状态
        DEFAULT(R.string.gesture_default, "#a5a5a5"),
        //密码输入错误
        ERROR(R.string.gesture_error, "#f4333c"),
        //密码输入正确
        CORRECT(R.string.gesture_correct, "#a5a5a5");

        private Status(int msgId, String color) {
            this.msgId = msgId;
            this.color = color;
        }
        private int msgId;
        private String color;
    }
}
