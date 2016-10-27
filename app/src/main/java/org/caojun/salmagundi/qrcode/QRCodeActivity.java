package org.caojun.salmagundi.qrcode;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import org.caojun.salmagundi.R;

import java.io.InputStream;

/**
 * 生成二维码
 * Created by CaoJun on 2016/10/26.
 */

public class QRCodeActivity extends AppCompatActivity {

    private EditText etQRCode;
    private ImageView ivQRCode;
    private Bitmap bmQRCode;
    private String strQRCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_qrcode);
        etQRCode = (EditText) this.findViewById(R.id.etQRCode);
        ivQRCode = (ImageView) this.findViewById(R.id.ivQRCode);

        ivQRCode.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v) {

                doQRCode();
            }
        });

        doQRCode();
    }

    private void doQRCode()
    {

        new Thread()
        {
            @Override
            public void run() {
                String text = etQRCode.getText().toString();
                if(TextUtils.isEmpty(text))
                {
                    text = etQRCode.getHint().toString();
                    if(TextUtils.isEmpty(text))
                    {
                        return;
                    }
                }
                if(text.equals(strQRCode))
                {
                    return;
                }
                strQRCode = text;
                final DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                int width = dm.widthPixels * 4 / 5;
                int height = dm.heightPixels * 4 / 5;
                int wh = Math.min(width, height);
                bmQRCode = QRCodeUtils.createQRImage(text, wh, wh);
                handler.sendMessage(Message.obtain());
            }
        }.start();
    }

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            try {
                ivQRCode.setImageBitmap(bmQRCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
