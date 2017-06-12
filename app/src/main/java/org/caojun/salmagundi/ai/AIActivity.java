package org.caojun.salmagundi.ai;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.util.Linkify;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.turing.androidsdk.HttpRequestListener;
import com.turing.androidsdk.TuringManager;
import org.caojun.salmagundi.BaseActivity;
import org.caojun.salmagundi.R;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 傻瓜式AI对话
 * Created by CaoJun on 2017/1/19.
 */

@Route(path = "/main/ai")
public class AIActivity extends BaseActivity {

    private String[] names;
    private boolean isAITurn = false;//是否轮到AI
    private Button btnSend;
    private EditText etInfo;
    private TextView tvInfo;
    private ScrollView scrollView;
    private TuringManager turingManager;
    private Spinner spTrueQuestion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_ai);

        tvInfo = (TextView) this.findViewById(R.id.tvInfo);
        etInfo = (EditText) this.findViewById(R.id.etInfo);
        btnSend = (Button) this.findViewById(R.id.btnSend);
        scrollView = (ScrollView) this.findViewById(R.id.scrollView);
        spTrueQuestion = (Spinner) this.findViewById(R.id.spTrueQuestion);

        names = this.getResources().getStringArray(R.array.ai_name);

        tvInfo.setAutoLinkMask(Linkify.ALL);
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
        scrollView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                scroll(scrollView, tvInfo);
            }
        });

        turingManager = new TuringManager(this, "1ce6ccd092ea41ac96a5c9db44250ecd", "b48a74b2dc265ea0");
        turingManager.setHttpRequestListener(new HttpRequestListener() {
            @Override
            public void onSuccess(String s) {
                parseTuring(s);
                changeTurn();
            }

            @Override
            public void onFail(int i, String s) {

            }
        });

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.true_questions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTrueQuestion.setAdapter(adapter);
        spTrueQuestion.setSelection(0, true);
        spTrueQuestion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                etInfo.setText(adapter.getItem(position).toString());
                btnSend.performClick();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void parseTuring(String s) {
        StringBuffer sb = new StringBuffer();
        try {
            JSONObject jsonObject = new JSONObject(s);
            String text = jsonObject.optString("text");
            sb.append(text);
            String url = jsonObject.optString("url");
            if(!TextUtils.isEmpty(url)) {
                sb.append("\n");
                sb.append(url);
                sb.append("\n");
            }
            JSONArray list = jsonObject.optJSONArray("list");
            if(list != null) {
                sb.append("\n");
                for(int i = 0;i < list.length();i ++) {
                    JSONObject jo = list.optJSONObject(i);
                    if(jo == null) {
                        continue;
                    }
                    String article = jo.optString("article");
                    String source = jo.optString("source");
                    String detailurl = jo.optString("detailurl");
                    String name = jo.optString("name");//菜名
                    String info = jo.optString("info");//配料

                    sb.append(i + 1);
                    if(!TextUtils.isEmpty(source) || !TextUtils.isEmpty(name)) {
                        sb.append("【");
                        if(!TextUtils.isEmpty(source)) {
                            sb.append(source);
                        }
                        if(!TextUtils.isEmpty(name)) {
                            sb.append(name);
                        }
                        sb.append("】");
                    }
                    if(!TextUtils.isEmpty(article) || !TextUtils.isEmpty(info)) {
                        if(!TextUtils.isEmpty(article)) {
                            sb.append(article);
                        }
                        if(!TextUtils.isEmpty(info)) {
                            sb.append(info);
                        }
                    }
                    if(!TextUtils.isEmpty(detailurl)) {
                        sb.append("\n");
                        sb.append(detailurl);
                        sb.append("\n");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        showInfo(sb.toString(), true);
    }

    private void showInfo(final String text, final boolean isEnter) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String info = tvInfo.getText().toString() + text + (isEnter?"\n":"");
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
        etInfo.setText(null);
        changeTurn();
        doAITurn(word);
    }

    private void doAITurn(String word) {
        showInfo(this.getString(R.string.ai_word, names[1], ""), false);
        turingManager.requestTuring(word);
    }

    private void changeTurn() {
        isAITurn = !isAITurn;
        checkMyTurnInput(etInfo.getText());
    }

    private void scroll(final ScrollView scroll, final View inner) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (scroll == null || inner == null) {
                    return;
                }
                // 内层高度超过外层
                int offset = inner.getMeasuredHeight() - scroll.getMeasuredHeight();
                if (offset < 0) {
                    offset = 0;
                }
                scroll.scrollTo(0, offset);
            }
        });
    }

}
