package org.caojun.salmagundi.ai;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.caojun.salmagundi.BaseActivity;
import org.caojun.salmagundi.R;

/**
 * 傻瓜式AI对话
 * Created by CaoJun on 2017/1/19.
 */

public class AIActivity extends BaseActivity {

    private String[] names = new String[]{};
    private boolean isAITurn = false;//是否轮到AI
    private Button btnSend;
    private EditText etInfo;
    private TextView tvInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_ai);

        tvInfo = (TextView) this.findViewById(R.id.tvInfo);
        etInfo = (EditText) this.findViewById(R.id.etInfo);
        btnSend = (Button) this.findViewById(R.id.btnSend);

        names = this.getResources().getStringArray(R.array.ai_name);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doMyTurn();
            }
        });

        etInfo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkMyTurnInput(s);
            }
        });
    }

    private void showInfo(String text) {
        String info = tvInfo.getText().toString();
        tvInfo.setText(info + "\n" + text);
    }

    private void checkMyTurnInput(Editable s) {
        if(isAITurn) {
            etInfo.setEnabled(false);
            btnSend.setEnabled(false);
            return;
        }
        etInfo.setEnabled(true);
        if(s == null || TextUtils.isEmpty(s.toString())) {
            btnSend.setEnabled(false);
        }
        else {
            btnSend.setEnabled(true);
        }
    }

    private void doMyTurn() {
        String word = etInfo.getText().toString();
        showInfo(this.getString(R.string.ai_word, names[0], word));
        changeTurn();
        doAITurn(word);
    }

    private void doAITurn(String word) {
        showInfo(this.getString(R.string.ai_word, names[1], "你说啥？"));
        changeTurn();
    }

    private void changeTurn() {
        isAITurn = !isAITurn;
        checkMyTurnInput(etInfo.getText());
    }
}
