package org.caojun.cameracolor.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.alibaba.android.arouter.launcher.ARouter;
import org.caojun.cameracolor.Constant;
import org.caojun.cameracolor.R;
import org.caojun.cameracolor.utils.CameraColorUtils;
import org.caojun.cameracolor.utils.ColorUtils;

public class MainActivity extends AppCompatActivity {

    private LinearLayout llRoot;
    private TextureView mTextureView;
    private ImageView ivColor;
    private TextView tvRGB, tvHEX;
    private Button btnRGB2HSV, btnHSV2RGB;
    private String HEX = "000000";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            byte[] bytes = msg.getData().getByteArray("bytes");
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            if (bitmap == null) {
                return;
            }
//            Palette p = Palette.generate(bitmap);
//            if (p == null) {
//                return;
//            }
//            Swatch s1 = p.getVibrantSwatch(); //充满活力的色板
//            if (s1 == null) {
//                return;
//            }
//            int rgb = s1.getRgb();

            int rgb = bitmap.getPixel(bitmap.getWidth() / 2, bitmap.getHeight() / 2);

            String hex = ColorUtils.toHexEncoding(rgb);
            int red = Color.red(rgb);
            int green = Color.green(rgb);
            int blue = Color.blue(rgb);
            HEX = hex.toUpperCase();

            tvRGB.setText(getString(R.string.color_rgb, String.valueOf(red), String.valueOf(green), String.valueOf(blue)));
            tvHEX.setText(getString(R.string.color_hex, HEX));

            ivColor.setBackgroundColor(rgb);

//            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//            ClipData clipData = ClipData.newPlainText(null, hex);
//            cm.setPrimaryClip(clipData);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        llRoot = (LinearLayout) findViewById(R.id.llRoot);
        mTextureView = (TextureView) findViewById(R.id.textureView);
        ivColor = (ImageView) findViewById(R.id.ivColor);
        tvRGB = (TextView) findViewById(R.id.tvRGB);
        tvHEX = (TextView) findViewById(R.id.tvHEX);
        btnRGB2HSV = (Button) findViewById(R.id.btnRGB2HSV);
        btnHSV2RGB = (Button) findViewById(R.id.btnHSV2RGB);

        llRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraColorUtils.getInstance().takeColor();
            }
        });

        btnRGB2HSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(HEX)) {
                    return;
                }
                ARouter.getInstance().build(Constant.ACTIVITY_RGB2HSV).withString("HEX", HEX).navigation();
            }
        });
        btnHSV2RGB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(HEX)) {
                    return;
                }
                ARouter.getInstance().build(Constant.ACTIVITY_HSV2RGB).withString("HEX", HEX).navigation();
            }
        });

        CameraColorUtils.getInstance().init(this, mTextureView, handler);
    }

    @Override
    public void onResume() {
        super.onResume();
        CameraColorUtils.getInstance().onResume();
    }

    @Override
    public void onPause() {
        CameraColorUtils.getInstance().onPause();
        super.onPause();
    }
}
