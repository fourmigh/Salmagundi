package org.caojun.salmagundi.textart;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import org.caojun.salmagundi.BaseActivity;
import org.caojun.salmagundi.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 文字艺术
 * Created by CaoJun on 2017/1/23.
 */

public class TextArtActivity extends BaseActivity {

    private TextView tvInfo;
    private ScrollView scrollView;
    private String[] texts;

    private Timer timer = new Timer();
    private int index;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                showInfo(texts[msg.what]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private TimerTask task = new TimerTask() {
        public void run() {
            Message message = new Message();
            message.what = index;
            handler.sendMessage(message);
            index ++;
            if(index >= texts.length) {
                index = 0;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_textart);

        tvInfo = (TextView) this.findViewById(R.id.tvInfo);
        scrollView = (ScrollView) this.findViewById(R.id.scrollView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        texts = new String[26];
        for (int j = 0; j < texts.length; j++) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < j; i++) {
                sb.append("\n");
            }
            for (int i = 0; i < j; i++) {
                sb.append(" ");
            }
            texts[j] = sb.toString() + String.valueOf((char)('a' + j));
        }
        timer.schedule(task, 1000 * 5, 500);
    }

    private void showInfo(final String text) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvInfo.setText(text);
            }
        });
    }

    private void scroll() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (scrollView == null || tvInfo == null) {
                    return;
                }
                // 内层高度超过外层
                int offset = tvInfo.getMeasuredHeight() - scrollView.getMeasuredHeight();
                if (offset < 0) {
                    offset = 0;
                }
                scrollView.scrollTo(0, offset);
            }
        });
    }
}
