package org.caojun.salmagundi.lockpattern;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import org.caojun.salmagundi.Constant;
import org.caojun.salmagundi.R;
import org.caojun.salmagundi.lockpattern.utils.LockPatternUtils;
import org.caojun.salmagundi.lockpattern.widget.LockPatternIndicator;
import org.caojun.salmagundi.lockpattern.widget.LockPatternView;
import org.caojun.salmagundi.utils.DataStorageUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * 手势密码创建
 * Created by CaoJun on 2017/2/21.
 */

@Route(path = Constant.ACTIVITY_GESTURE_REGISTER)
public class CreateGestureActivity extends Activity {

    private static final long DELAY_TIME = 600l;
    private List<LockPatternView.Cell> mChosenPattern;
    private LockPatternIndicator lockPatternIndicator;
    private LockPatternView lockPatternView;
    private TextView tvMessage;
    private Button btnReset;

    @Autowired
    protected String hostGesture;

    private byte[] gesturePassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_gesture);
        ARouter.getInstance().inject(this);

        lockPatternIndicator = (LockPatternIndicator) this.findViewById(R.id.lockPatternIndicator);
        lockPatternView = (LockPatternView) this.findViewById(R.id.lockPatternView);
        tvMessage = (TextView) this.findViewById(R.id.tvMessage);
        btnReset = (Button) this.findViewById(R.id.btnReset);

        lockPatternView.setOnPatternListener(new LockPatternView.OnPatternListener() {

            @Override
            public void onPatternStart() {
                lockPatternView.removePostClearPatternRunnable();
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
            }

            @Override
            public void onPatternComplete(List<LockPatternView.Cell> pattern) {
                if (mChosenPattern == null && pattern.size() >= 4) {
                    mChosenPattern = new ArrayList<>(pattern);
                    updateStatus(Status.CORRECT, pattern);
                } else if (mChosenPattern == null && pattern.size() < 4) {
                    updateStatus(Status.LESSERROR, pattern);
                } else if (mChosenPattern != null) {
                    if (mChosenPattern.equals(pattern)) {
                        updateStatus(Status.CONFIRMCORRECT, pattern);
                    } else {
                        updateStatus(Status.CONFIRMERROR, pattern);
                    }
                }
            }
        });

        if (TextUtils.isEmpty(hostGesture)) {
            hostGesture = this.getClass().getName();
        }

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChosenPattern = null;
                lockPatternIndicator.setDefaultIndicator();
                updateStatus(Status.DEFAULT, null);
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                btnReset.setVisibility(View.GONE);
            }
        });

        btnReset.setVisibility(View.GONE);
    }

    /**
     * 更新状态
     *
     * @param status
     * @param pattern
     */
    private void updateStatus(Status status, List<LockPatternView.Cell> pattern) {
        tvMessage.setText(status.msgId);
        tvMessage.setTextColor(Color.parseColor(status.color));
        switch (status) {
            case DEFAULT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case CORRECT:
                updateLockPatternIndicator();
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                btnReset.setVisibility(View.VISIBLE);
                break;
            case LESSERROR:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case CONFIRMERROR:
                lockPatternView.setPattern(LockPatternView.DisplayMode.ERROR);
                lockPatternView.postClearPatternRunnable(DELAY_TIME);
                break;
            case CONFIRMCORRECT:
                saveChosenPattern(pattern);
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                setLockPatternSuccess();
                break;
        }
    }

    private void setLockPatternSuccess() {
        Intent intent = new Intent();
        intent.putExtra("gesturePassword", gesturePassword);
        setResult(Activity.RESULT_OK, intent);
//        setResult(Activity.RESULT_OK);
        finish();
    }

    private void updateLockPatternIndicator() {
        if (mChosenPattern == null) {
            return;
        }
        lockPatternIndicator.setIndicator(mChosenPattern);
    }

    private void saveChosenPattern(List<LockPatternView.Cell> cells) {
//        byte[] bytes = LockPatternUtils.patternToHash(cells);
//        DataStorageUtils.saveByteArray(this, GestureConstant.DataGesture, hostGesture, bytes);
        gesturePassword = LockPatternUtils.patternToHash(cells);
        DataStorageUtils.saveByteArray(getApplicationContext(), GestureConstant.DataGesture, hostGesture, gesturePassword);
    }

    private enum Status {
        //默认的状态，刚开始的时候（初始化状态）
        DEFAULT(R.string.create_gesture_default, "#a5a5a5"),
        //第一次记录成功
        CORRECT(R.string.create_gesture_correct, "#a5a5a5"),
        //连接的点数小于4（二次确认的时候就不再提示连接的点数小于4，而是提示确认错误）
        LESSERROR(R.string.create_gesture_less_error, "#f4333c"),
        //二次确认错误
        CONFIRMERROR(R.string.create_gesture_confirm_error, "#f4333c"),
        //二次确认正确
        CONFIRMCORRECT(R.string.create_gesture_confirm_correct, "#a5a5a5");

        private Status(int msgId, String color) {
            this.msgId = msgId;
            this.color = color;
        }

        private int msgId;
        private String color;
    }
}
