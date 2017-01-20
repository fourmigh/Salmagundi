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

import com.socks.library.KLog;

import org.caojun.salmagundi.BaseActivity;
import org.caojun.salmagundi.R;

import rx.Observable;
import rx.Subscriber;

/**
 * 傻瓜式AI对话
 * Created by CaoJun on 2017/1/19.
 */

public class AIActivity extends BaseActivity {

    private String[] names;
    private String[] aiWords;
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
        aiWords = this.getResources().getStringArray(R.array.ai_words);

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

    private void showInfo(final String text, final boolean isEnter) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String info = tvInfo.getText().toString() + text.trim() + (isEnter?"\n":"");
                tvInfo.setText(info);
            }
        });

    }

    private void checkMyTurnInput(final Editable s) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
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
        });
    }

    private void doMyTurn() {
        String word = etInfo.getText().toString();
        showInfo(this.getString(R.string.ai_word, names[0], word), true);
        changeTurn();
        doAITurn(word);
    }

    private void doAITurn(String word) {
        showInfo(this.getString(R.string.ai_word, names[1], ""), false);

        for(int i = 0;i < aiWords.length;i ++) {
            final String[] words = aiWords[i].split("->");
            if(words[0].startsWith(word) || i == aiWords.length - 1) {
                new Thread() {
                    @Override
                    public void run() {
                        for(int i = 0;i < words[1].length();i ++) {
                            showInfo(String.valueOf(words[1].charAt(i)), i == words[1].length() - 1);
                            try {
                                sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        changeTurn();
                    }
                }.start();
                return;
            }
        }
    }

    private void changeTurn() {
        isAITurn = !isAITurn;
        checkMyTurnInput(etInfo.getText());
    }
}
