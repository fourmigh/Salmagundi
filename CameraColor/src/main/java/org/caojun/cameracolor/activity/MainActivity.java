package org.caojun.cameracolor.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import org.caojun.activity.BaseActivity;
import org.caojun.cameracolor.Constant;
import org.caojun.cameracolor.R;
import org.caojun.cameracolor.listener.OnColorStatusChange;
import org.caojun.cameracolor.utils.ColorUtils;
import org.caojun.cameracolor.widget.CameraView;

@Route(path = Constant.ACTIVITY_MAIN)
public class MainActivity extends BaseActivity {

//    private LinearLayout llRoot;
//    private TextureView mTextureView;
    private ImageView ivColor;
    private TextView tvRGB, tvHEX;
    private Button btnRGB2HSV, btnHSV2RGB;
    private String HEX = "000000";

    private CameraView cameraView;

//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            int rgb = 0;
//            byte[] bytes = msg.getData().getByteArray("bytes");
//            if (bytes != null) {
//
//                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                if (bitmap == null) {
//                    return;
//                }
////            Palette p = Palette.generate(bitmap);
////            if (p == null) {
////                return;
////            }
////            Swatch s1 = p.getVibrantSwatch(); //充满活力的色板
////            if (s1 == null) {
////                return;
////            }
////            int rgb = s1.getRgb();
//
//                rgb = bitmap.getPixel(bitmap.getWidth() / 2, bitmap.getHeight() / 2);
//
//            } else {
//                float red = msg.getData().getFloat("red", -1);
//                float green = msg.getData().getFloat("green", -1);
//                float blue = msg.getData().getFloat("blue", -1);
//                if (red != -1 && green != -1 && blue != -1) {
//                    rgb = ColorUtils.getIntFromColor((int)red, (int)green, (int)blue);
//                } else {
//                    return;
//                }
//            }
//
//            String hex = ColorUtils.toHexEncoding(rgb);
//            int red = Color.red(rgb);
//            int green = Color.green(rgb);
//            int blue = Color.blue(rgb);
//            HEX = hex.toUpperCase();
//
//            tvRGB.setText(getString(R.string.color_rgb, String.valueOf(red), String.valueOf(green), String.valueOf(blue)));
//            tvHEX.setText(getString(R.string.color_hex, HEX));
//
//            ivColor.setBackgroundColor(rgb);
//
////            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
////            ClipData clipData = ClipData.newPlainText(null, hex);
////            cm.setPrimaryClip(clipData);
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        llRoot = (LinearLayout) findViewById(R.id.llRoot);
//        mTextureView = (TextureView) findViewById(R.id.textureView);
        ivColor = (ImageView) findViewById(R.id.ivColor);
        tvRGB = (TextView) findViewById(R.id.tvRGB);
        tvHEX = (TextView) findViewById(R.id.tvHEX);
        btnRGB2HSV = (Button) findViewById(R.id.btnRGB2HSV);
        btnHSV2RGB = (Button) findViewById(R.id.btnHSV2RGB);

//        llRoot.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                CameraColorUtils.getInstance().takeColor();
//            }
//        });

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

//        CameraColorUtils.getInstance().init(this, mTextureView, handler);

        cameraView = findViewById(R.id.cameraView);
        cameraView.setOnColorStatusChange(new OnColorStatusChange() {
            @Override
            public void onColorChange(int color) {
                String hex = ColorUtils.toHexEncoding(color);
                int red = Color.red(color);
                int green = Color.green(color);
                int blue = Color.blue(color);
                HEX = hex.toUpperCase();

                tvRGB.setText(getString(R.string.color_rgb, String.valueOf(red), String.valueOf(green), String.valueOf(blue)));
                tvHEX.setText(getString(R.string.color_hex, HEX));

                ivColor.setBackgroundColor(color);
            }
        });

//        this.checkSelfPermission(Manifest.permission.CAMERA, new RequestPermissionListener() {
//
//            @Override
//            public void onFail() {
//                finish();
//            }
//
//            @Override
//            public void onSuccess() {
//                cameraView.create();
//                cameraView.initCamera();
//            }
//        });
    }

    @Override
    public void onResume() {
        super.onResume();
        cameraView.onResume();
    }

    @Override
    public void onPause() {
        cameraView.onPause();
        super.onPause();
    }
}
