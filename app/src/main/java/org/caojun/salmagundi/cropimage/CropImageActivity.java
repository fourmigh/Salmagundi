package org.caojun.salmagundi.cropimage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;

import org.caojun.salmagundi.Constant;
import org.caojun.salmagundi.R;

import java.util.List;

/**
 * Created by CaoJun on 2018-3-12.
 */
@Route(path = Constant.ACTIVITY_CROPIMAGE)
public class CropImageActivity extends Activity {

//    private static final int Request_User_Header = 1;
//    private static final int  Request_Image_Crop = 2;
    private static final int Request_Select_Picture = 3;
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
                doSelectPicture();
            }
        });
    }

    private void doSelectPicture() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofAll())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
//                .theme()//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .maxSelectNum(1)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .previewVideo(true)// 是否可预览视频 true or false
                .enablePreviewAudio(false) // 是否可播放音频 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                .enableCrop(true)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                .glideOverride(300, 300)// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(true)// 是否显示gif图片 true or false
//                .compressSavePath(getPath())//压缩图片保存地址
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                .circleDimmedLayer(true)// 是否圆形裁剪 true or false
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .openClickSound(true)// 是否开启点击声音 true or false
//                .selectionMedia()// 是否传入已选图片 List<LocalMedia> list
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .cropCompressQuality(100)// 裁剪压缩质量 默认90 int
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .cropWH(300, 300)// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
                .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                .videoQuality(1)// 视频录制质量 0 or 1 int
                .videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int
                .videoMinSecond(10)// 显示多少秒以内的视频or音频也可适用 int
                .recordVideoSecond(60)//视频秒数录制 默认60s int
                .forResult(Request_Select_Picture);//结果回调onActivityResult code
    }

    private void doUserHeader() {
//        MultiImageSelector.create()
//                .showCamera(true)
//                .single()
//                .start(this, Request_User_Header);
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofAll())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
//                .theme()//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
//                .maxSelectNum(1)// 最大图片选择数量 int
//                .minSelectNum(1)// 最小选择数量 int
//                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
//                .previewImage(true)// 是否可预览图片 true or false
//                .previewVideo(true)// 是否可预览视频 true or false
//                .enablePreviewAudio(false) // 是否可播放音频 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
//                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
//                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                .enableCrop(true)// 是否裁剪 true or false
//                .compress(true)// 是否压缩 true or false
//                .glideOverride(300, 300)// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
//                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
//                .isGif(true)// 是否显示gif图片 true or false
//                .compressSavePath(getPath())//压缩图片保存地址
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
//                .circleDimmedLayer(true)// 是否圆形裁剪 true or false
                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
//                .openClickSound(true)// 是否开启点击声音 true or false
//                .selectionMedia()// 是否传入已选图片 List<LocalMedia> list
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
//                .cropCompressQuality(100)// 裁剪压缩质量 默认90 int
//                .minimumCompressSize(100)// 小于100kb的图片不压缩
//                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .cropWH(300, 300)// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
                .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
//                .videoQuality(1)// 视频录制质量 0 or 1 int
//                .videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int
//                .videoMinSecond(10)// 显示多少秒以内的视频or音频也可适用 int
//                .recordVideoSecond(60)//视频秒数录制 默认60s int
                .forResult(Request_Select_Picture);//结果回调onActivityResult code
    }

//    private void doCropImage(String path) {
////        val uri = Uri.fromFile(File(path))
////
////        val intent = Intent("com.android.camera.action.CROP")
////        intent.setDataAndType(uri, "image/*")
////        // crop为true是设置在开启的intent中设置显示的view可以剪裁
////        intent.putExtra("crop", "true")
////
////        intent.putExtra("scale", true)
////
////        // aspectX aspectY 是宽高的比例
////        intent.putExtra("aspectX", 1)
////        intent.putExtra("aspectY", 1)
////
////        // outputX,outputY 是剪裁图片的宽高
////        intent.putExtra("outputX", 300)
////        intent.putExtra("outputY", 300)
////        intent.putExtra("return-data", true)
////        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
//
//        Intent intent = new Intent(this, CropImage.class);
//        intent.putExtra(CropImage.IMAGE_PATH, path);
//        intent.putExtra(CropImage.SCALE, true);
//        intent.putExtra(CropImage.RETURN_DATA, true);
//        intent.putExtra(CropImage.OUTPUT_X, 300);
//        intent.putExtra(CropImage.OUTPUT_Y, 300);
//        startActivityForResult(intent, Request_Image_Crop);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            switch (requestCode) {
                case Request_Select_Picture:
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    if (selectList.get(0).isCompressed()) {
                        Glide.with(this).load(selectList.get(0).getCompressPath()).into(ivCurrent);
                    } else if (selectList.get(0).isCut()){
                        Glide.with(this).load(selectList.get(0).getCutPath()).into(ivCurrent);
                    } else {
                        Glide.with(this).load(selectList.get(0).getPath()).into(ivCurrent);
                    }
                    //包括裁剪和压缩后的缓存，要在上传成功后调用，注意：需要系统sd卡权限
                    PictureFileUtils.deleteCacheDirFile(this);
                    return;
//                case Request_User_Header:
//                    List<String> images = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
//                    doCropImage(images.get(0));
//                    return;
//                case Request_Image_Crop:
////                    String path = data.getStringExtra(CropImage.IMAGE_PATH);
////                    if (TextUtils.isEmpty(path)) {
////                        return;
////                    }
////                    Bitmap bitmap = BitmapFactory.decodeFile(path);
////                    ivCurrent.setImageBitmap(bitmap);
////                    CropImageUtils.deleteFile(new File(path));
//                    byte[] bytes = data.getExtras().getByteArray(CropImage.RETURN_DATA_AS_BITMAP);
//                    if (bytes != null) {
//                        Bitmap bitmap = CropImageUtils.bytes2Bitmap(bytes);
////                        ivCurrent.setImageBitmap(bitmap);
//                        Glide.with(this).load(bitmap).into(ivCurrent);
//                    }
//                    return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
