package org.caojun.salmagundi.iflytek;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;
import com.alibaba.android.arouter.facade.annotation.Route;
import org.caojun.salmagundi.Constant;
import org.caojun.salmagundi.R;

/**
 * 讯飞语音
 * Created by CaoJun on 2017/7/26.
 */

@Route(path = Constant.ACTIVITY_FLYTEK)
public class FlytekActivity extends Activity {

    private final int[] IDButtons = {R.id.btnEnglish, R.id.btnFrench, R.id.btnRussian, R.id.btnSpanish, R.id.btnHindi, R.id.btnVietnamese};
    private final int[] IDEditTexts = {R.id.etEnglish, R.id.etFrench, R.id.etRussian, R.id.etSpanish, R.id.etHindi, R.id.etVietnamese};
    private final String[] VoiceNames = {"aiscatherine", "mariane", "allabent", "gabriela", "abha", "xiaoyun"};
    private Button[] buttons;
    private EditText[] editTexts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_flytek);

        buttons = new Button[IDButtons.length];
        for (int i = 0;i < IDButtons.length;i ++) {
            buttons[i] = (Button) findViewById(IDButtons[i]);
        }
        editTexts = new EditText[IDEditTexts.length];
        for (int i = 0;i < IDEditTexts.length;i ++) {
            editTexts[i] = (EditText) findViewById(IDEditTexts[i]);
        }


    }
}
