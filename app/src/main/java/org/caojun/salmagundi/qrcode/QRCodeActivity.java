package org.caojun.salmagundi.qrcode;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import org.caojun.salmagundi.R;
import org.caojun.salmagundi.utils.PackageUtils;

/**
 * 生成二维码
 * Created by CaoJun on 2016/10/26.
 */

public class QRCodeActivity extends AppCompatActivity {

    private EditText etQRCode;
    private ImageView ivQRCode;
    private Bitmap bmQRCode;
    private String strQRCode;
    private ImageButton ibGallery3d;
    private Drawable iconGallery3d;
    private String labelGallery3d;
    private static String LastSavedText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_qrcode);
        etQRCode = (EditText) this.findViewById(R.id.etQRCode);
        ivQRCode = (ImageView) this.findViewById(R.id.ivQRCode);
        ibGallery3d = (ImageButton) this.findViewById(R.id.ibGallery3d);
        ibGallery3d.setVisibility(View.GONE);
        ibGallery3d.setEnabled(false);

        ivQRCode.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v) {

                refreshQRCode();
            }
        });

        ibGallery3d.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                String message = getString(R.string.qrcode_save, labelGallery3d, getString(R.string.successed));
                if(TextUtils.isEmpty(MediaStore.Images.Media.insertImage(getContentResolver(), bmQRCode, getString(R.string.qrcode_title), strQRCode)))
                {
                    message = getString(R.string.qrcode_save, labelGallery3d, getString(R.string.failed));
                    ibGallery3d.setEnabled(true);
                }
                else
                {
                    LastSavedText = strQRCode;
                    ibGallery3d.setEnabled(false);
                }
                Toast.makeText(QRCodeActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });

        new Thread()
        {
            @Override
            public void run() {
                PackageInfo packageInfo = PackageUtils.getPackageInfo(QRCodeActivity.this, "com.android.gallery3d");
                if(packageInfo != null)
                {
                    PackageManager pm = QRCodeActivity.this.getPackageManager();
                    iconGallery3d = packageInfo.applicationInfo.loadIcon(pm);
                    labelGallery3d = packageInfo.applicationInfo.loadLabel(pm).toString();
                    handlerGallery3d.sendMessage(Message.obtain());
                }
            }
        }.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshQRCode();
    }

    /**
     * 刷新二维码
     */
    private void refreshQRCode()
    {
        ibGallery3d.setEnabled(false);
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
                handlerQRCode.sendMessage(Message.obtain());
            }
        }.start();
    }

    private Handler handlerGallery3d = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            try {
                ibGallery3d.setImageDrawable(iconGallery3d);
                ibGallery3d.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private Handler handlerQRCode = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            try {
                ivQRCode.setImageBitmap(bmQRCode);
                if(TextUtils.isEmpty(LastSavedText) || !LastSavedText.equals(strQRCode)) {
                    ibGallery3d.setEnabled(true);
                }
                else {
                    ibGallery3d.setEnabled(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
