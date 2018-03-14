package org.caojun.salmagundi.cropimage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;

import org.caojun.library.CropImage;
import org.caojun.library.CropImageUtils;
import org.caojun.library.MultiImageSelector;
import org.caojun.salmagundi.Constant;
import org.caojun.salmagundi.R;
import java.util.List;

/**
 * Created by CaoJun on 2018-3-12.
 */
@Route(path = Constant.ACTIVITY_CROPIMAGE)
public class CropImageActivity extends Activity {

    private static final int Request_User_Header = 1;
    private static final int  Request_Image_Crop = 2;
    private ImageView ivCurrent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cropimage);

        final ImageView imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivCurrent = imageView;
                doUserHeader();
            }
        });

        final ImageView circleImageView = findViewById(R.id.circleImageView);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivCurrent = circleImageView;
                doUserHeader();
            }
        });
    }

    private void doUserHeader() {
        MultiImageSelector.create()
                .showCamera(true)
                .single()
                .start(this, Request_User_Header);
    }

    private void doCropImage(String path) {
//        val uri = Uri.fromFile(File(path))
//
//        val intent = Intent("com.android.camera.action.CROP")
//        intent.setDataAndType(uri, "image/*")
//        // crop为true是设置在开启的intent中设置显示的view可以剪裁
//        intent.putExtra("crop", "true")
//
//        intent.putExtra("scale", true)
//
//        // aspectX aspectY 是宽高的比例
//        intent.putExtra("aspectX", 1)
//        intent.putExtra("aspectY", 1)
//
//        // outputX,outputY 是剪裁图片的宽高
//        intent.putExtra("outputX", 300)
//        intent.putExtra("outputY", 300)
//        intent.putExtra("return-data", true)
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())

        Intent intent = new Intent(this, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, path);
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.RETURN_DATA, true);
        intent.putExtra(CropImage.OUTPUT_X, 300);
        intent.putExtra(CropImage.OUTPUT_Y, 300);
//        intent.putExtra(CropImage.ASPECT_X, 1);
//        intent.putExtra(CropImage.ASPECT_Y, 1);
        startActivityForResult(intent, Request_Image_Crop);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            switch (requestCode) {
                case Request_User_Header:
                    List<String> images = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                    doCropImage(images.get(0));
                    return;
                case Request_Image_Crop:
//                    String path = data.getStringExtra(CropImage.IMAGE_PATH);
//                    if (TextUtils.isEmpty(path)) {
//                        return;
//                    }
//                    Bitmap bitmap = BitmapFactory.decodeFile(path);
//                    ivCurrent.setImageBitmap(bitmap);
//                    CropImageUtils.deleteFile(new File(path));
                    byte[] bytes = data.getExtras().getByteArray(CropImage.RETURN_DATA_AS_BITMAP);
                    if (bytes != null) {
                        Bitmap bitmap = CropImageUtils.bytes2Bitmap(bytes);
//                        ivCurrent.setImageBitmap(bitmap);
                        Glide.with(this).load(bitmap).into(ivCurrent);
                    }
                    return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
