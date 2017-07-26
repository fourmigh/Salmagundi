package org.caojun.salmagundi.textart;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.ScrollView;
import android.widget.TextView;
import com.alibaba.android.arouter.facade.annotation.Route;
import org.caojun.salmagundi.Constant;
import org.caojun.salmagundi.R;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 文字艺术
 * Created by CaoJun on 2017/1/23.
 */

@Route(path = Constant.ACTIVITY_TEXTART)
public class TextArtActivity extends Activity {

    private TextView tvInfo;
    private ScrollView scrollView;

    private String TEXT;
    private StringBuffer stringBuffer;
    private Timer timer = new Timer();
    private int index;
    private boolean isDirect = true;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                showInfo((String) msg.obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private TimerTask task = new TimerTask() {
        public void run() {

            if(isDirect) {
                stringBuffer.append(TEXT.charAt(index));
                index++;
            }
            else {
                stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                index --;
            }

            Message message = new Message();
            message.obj = stringBuffer.toString();
            handler.sendMessage(message);

            if (index >= TEXT.length() || index < 0) {
                isDirect = !isDirect;
                if(isDirect) {
                    index = 0;
                }
                else {
                    index = TEXT.length() - 1;
                }
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
//        texts = new String[26];
//        for (int j = 0; j < texts.length; j++) {
//            StringBuffer sb = new StringBuffer();
//            for (int i = 0; i < j; i++) {
//                sb.append("\n");
//            }
//            for (int i = 0; i < j; i++) {
//                sb.append(" ");
//            }
//            texts[j] = sb.toString() + String.valueOf((char)('a' + j));
//        }
        stringBuffer = new StringBuffer();
        TEXT = "//                       .::::.\n" +
                "//                     .::::::::.\n" +
                "//                    :::::::::::\n" +
                "//                 ..:::::::::::'\n" +
                "//              '::::::::::::'\n" +
                "//                .::::::::::\n" +
                "//           '::::::::::::::..\n" +
                "//                ..::::::::::::.\n" +
                "//              ``::::::::::::::::\n" +
                "//               ::::``:::::::::'        .:::.\n" +
                "//              ::::'   ':::::'       .::::::::.\n" +
                "//            .::::'      ::::     .:::::::'::::.\n" +
                "//           .:::'       :::::  .:::::::::' ':::::.\n" +
                "//          .::'        :::::.:::::::::'      ':::::.\n" +
                "//         .::'         ::::::::::::::'         ``::::.\n" +
                "//     ...:::           ::::::::::::'              ``::.\n" +
                "//    ```` ':.          ':::::::::'                  ::::..\n" +
                "//                       '.:::::'                    ':'````..";
        timer.schedule(task, 1000, 100);
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
