package org.caojun.salmagundi.qrcode;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;

import org.caojun.salmagundi.GalleryActivity;
import org.caojun.salmagundi.Constant;
import org.caojun.salmagundi.R;
import org.caojun.salmagundi.utils.FormatUtils;

/**
 * 生成二维码
 * Created by CaoJun on 2016/10/26.
 */

@Route(path = Constant.ACTIVITY_QRCODE)
public class QRCodeActivity extends GalleryActivity {

    private EditText etQRCode;
    private ImageView ivQRCode;
    private Bitmap bmQRCode;
    private String strQRCode;

    private static String LastSavedText;
    private LinearLayout llSize;
    private EditText etWidth, etHeight;

//    private int directColor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_qrcode);

        etQRCode = (EditText) this.findViewById(R.id.etQRCode);
        ivQRCode = (ImageView) this.findViewById(R.id.ivQRCode);

        llSize = (LinearLayout) this.findViewById(R.id.llSize);
        etWidth = (EditText) this.findViewById(R.id.etWidth);
        etHeight = (EditText) this.findViewById(R.id.etHeight);

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

        llSize.setVisibility(View.GONE);//TODO 隐藏功能
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshQRCode();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LastSavedText = null;
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
                int direct = FormatUtils.getRandom(0, QRCodeUtils.MaxDirectColor);
//                directColor = QRCodeUtils.MaxDirectColor;
                strQRCode = text;
                final DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                int width = dm.widthPixels * 4 / 5;
                int height = dm.heightPixels * 4 / 5;
                int wh = Math.min(width, height);

                String strWidth = etWidth.getText().toString();
                String strHeight = etHeight.getText().toString();
                if(!TextUtils.isEmpty(strWidth) && !TextUtils.isEmpty(strHeight))
                {
                    width = Integer.parseInt(strWidth);
                    height = Integer.parseInt(strHeight);
                    wh = Math.min(width, height);
                }
                Bundle data = new Bundle();
                data.putString("text", text);
                data.putInt("width", wh);
                data.putInt("height", wh);
                data.putInt("direct", direct);
                Message msg = new Message();
                msg.setData(data);
                handlerQRCode.sendMessage(msg);
            }
        }.start();
    }

    private Handler handlerQRCode = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            try {
                Bundle data = msg.getData();
                String text = data.getString("text");
                int width = data.getInt("width");
                int height = data.getInt("height");
                int direct = data.getInt("direct");
                bmQRCode = QRCodeUtils.createQRImage(QRCodeActivity.this, text, width, height, direct);

                ivQRCode.setImageBitmap(bmQRCode);

//                width = bmQRCode.getWidth();
//                height = bmQRCode.getHeight();
                etWidth.setText(String.valueOf(width));
                etHeight.setText(String.valueOf(height));

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
