package org.caojun.salmagundi.cameracolor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import org.caojun.salmagundi.BaseActivity;
import org.caojun.salmagundi.R;
import org.caojun.salmagundi.color.ColorUtils;

/**
 * 摄像头取色
 * Created by CaoJun on 2017/5/27.
 */

public class CameraColorActivity extends BaseActivity {

    private TextureView mTextureView;
    private ImageView ivColor;
    private TextView tvColor;

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
            tvColor.setText(hex);

            bitmap = ColorUtils.createImage(rgb, ivColor.getWidth(), ivColor.getHeight());
            ivColor.setImageBitmap(bitmap);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cameracolor);

        mTextureView = (TextureView) findViewById(R.id.textureView);
        ivColor = (ImageView) findViewById(R.id.ivColor);
        tvColor = (TextView) findViewById(R.id.tvColor);

        mTextureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraColorUtils.getInstance().takeColor();
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
