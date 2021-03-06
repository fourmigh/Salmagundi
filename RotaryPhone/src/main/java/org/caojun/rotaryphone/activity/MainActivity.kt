package org.caojun.rotaryphone.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Vibrator
import kotlinx.android.synthetic.main.activity_main.*
import org.caojun.rotaryphone.R
import org.caojun.rotaryphone.widget.RotaryView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import org.caojun.activity.BaseAppCompatActivity
import android.widget.RelativeLayout
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import com.bumptech.glide.Glide
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.socks.library.KLog
import org.caojun.utils.DataStorageUtils
import org.caojun.utils.ImageUtils
import org.jetbrains.anko.*
import org.caojun.utils.DigitUtils

class MainActivity : BaseAppCompatActivity() {

    private val Request_Select_Picture = 1
    private val Request_Select_Background = 2
    private val SEPARATOR = "，"
    private val ImageData = "ImageData"

    companion object {
        val BackgroundData = "BackgroundData"
    }

    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val vibrator = this.getSystemService(VIBRATOR_SERVICE) as Vibrator

        rotaryView.setOnRotaryListener(object : RotaryView.OnRotaryListener {
            override fun onDial(number: String) {
                val text = autoCompleteTextView.text.toString() + number
                autoCompleteTextView.setText(text)
                autoCompleteTextView.setSelection(autoCompleteTextView.text.length)
                doAsync {
                    vibrator.cancel()
                }

                if (text.length >= 3) {
                    startCountDown()
                }
            }

            override fun onReady() {
                val text = autoCompleteTextView.text.toString()
                if (text.length >= 3) {
                    startCountDown()
                }
            }

            override fun onRotating() {
                stopCountDown()
            }

            override fun onDialing() {
                stopCountDown()
            }

            override fun onStopDialing() {
                doAsync {
                    val mSharedPreferences = getSharedPreferences(SettingsActivity.PREFER_NAME, Context.MODE_PRIVATE)
                    val isVibrator = mSharedPreferences.getBoolean("switch_preference_vibrator", true)
                    if (isVibrator) {
                        vibrator.vibrate(longArrayOf(1, 1), 0)
                    }
                }
                stopCountDown()
            }

            override fun onBackgroundClicked() {
                PictureSelector.create(this@MainActivity)
                        .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                        .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                        .isCamera(true)// 是否显示拍照按钮 true or false
                        .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                        .enableCrop(true)// 是否裁剪 true or false
                        .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                        .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                        .circleDimmedLayer(true)// 是否圆形裁剪 true or false
                        .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                        .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                        .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                        .cropWH(300, 300)// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
                        .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
                        .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                        .forResult(Request_Select_Background)//结果回调onActivityResult code
            }
        })

//        checkSelfPermission(Manifest.permission.READ_CONTACTS, object : RequestPermissionListener {
//            override fun onSuccess() {
//                val list = getPhoneContacts()
//                val adapter = ArrayAdapter<String>(this@MainActivity, android.R.layout.simple_dropdown_item_1line, list)
//                autoCompleteTextView.setAdapter(adapter)
//            }
//
//            override fun onFail() {
//            }
//        })

        circleImageView.setOnClickListener({
            PictureSelector.create(this)
                    .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                    .isCamera(true)// 是否显示拍照按钮 true or false
                    .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                    .enableCrop(true)// 是否裁剪 true or false
                    .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                    .circleDimmedLayer(true)// 是否圆形裁剪 true or false
                    .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                    .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                    .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                    .cropWH(300, 300)// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
                    .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
                    .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                    .forResult(Request_Select_Picture)//结果回调onActivityResult code
        })
        circleImageView.setOnLongClickListener {
            autoCompleteTextView.text = null
            stopCountDown()
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) when (requestCode) {
            Request_Select_Background -> {
                val selectList = PictureSelector.obtainMultipleResult(data)
                val path = if (selectList[0].isCut) {
                    selectList[0].cutPath
                } else if (selectList[0].isCompressed) {
                    selectList[0].compressPath
                } else {
                    selectList[0].path
                }
                rotaryView.setMaskImage(path)
                DataStorageUtils.saveBitmap(this@MainActivity, BackgroundData, ImageUtils.toBitmap(path))
                return
            }
            Request_Select_Picture -> {
                val selectList = PictureSelector.obtainMultipleResult(data)
                // 例如 LocalMedia 里面返回三种path
                // 1.media.getPath(); 为原图path
                // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                val path = if (selectList[0].isCut) {
                    selectList[0].cutPath
                } else if (selectList[0].isCompressed) {
                    selectList[0].compressPath
                } else {
                    selectList[0].path
                }
//                    Glide.with(this).load(path).into(circleImageView)
//                    DataStorageUtils.saveString(this@MainActivity, ImageData, path)
                val image = ImageUtils.toBitmap(path)
                if (image != null) {
                    Glide.with(this).load(image).into(circleImageView)
                }
                DataStorageUtils.saveBitmap(this@MainActivity, ImageData, image)
                //包括裁剪和压缩后的缓存，要在上传成功后调用，注意：需要系统sd卡权限
//                    PictureFileUtils.deleteCacheDirFile(this)
                return
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onResume() {
        super.onResume()
//        doWaitDial()
        var isOnGlobalLayout = true
        rotaryView.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {

                if (isOnGlobalLayout) {
                    isOnGlobalLayout = false
                } else {
                    return
                }

                val params = circleImageView.layoutParams as RelativeLayout.LayoutParams
                params.width = Math.min(rotaryView.width, rotaryView.height) / 2 - 30
                params.height = params.width
                circleImageView.layoutParams = params

                val image = DataStorageUtils.loadBitmap(this@MainActivity, ImageData, null)
                if (image != null) {
                    Glide.with(this@MainActivity).load(image).into(circleImageView)
                }
            }
        })
    }

//    private fun getPhoneContacts(): List<String> {
//        /**联系人显示名称**/
//        val PHONES_DISPLAY_NAME_INDEX = 0
//        /**电话号码**/
//        val PHONES_NUMBER_INDEX = 1
//        val PHONES_PROJECTION = arrayOf(Phone.DISPLAY_NAME, Phone.NUMBER)
//
//        val resolver = this.contentResolver
//
//        // 获取手机联系人
//        val phoneCursor = resolver.query(Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null)
//
//        val list = ArrayList<String>()
//        if (phoneCursor != null) {
//            while (phoneCursor.moveToNext()) {
//
//                //得到手机号码
//                val phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX)
//                //当手机号码为空的或者为空字段 跳过当前循环
//                if (TextUtils.isEmpty(phoneNumber))
//                    continue
//
//                //得到联系人名称
//                val contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX)
//
//                list.add(phoneNumber + SEPARATOR + contactName)
//            }
//
//            phoneCursor.close()
//        }
//        return list
//    }

    private fun startCountDown() {
        tvCountdown.visibility = View.GONE
        val mSharedPreferences = getSharedPreferences(SettingsActivity.PREFER_NAME, Context.MODE_PRIVATE)
        val time = mSharedPreferences.getInt("sb_preference_time", 3).toLong() * 1000
        countDownTimer = object : CountDownTimer(time, 1) {
            override fun onTick(millisUntilFinished: Long) {
                KLog.d("onTick", "onTick: $millisUntilFinished")
                tvCountdown.text = getString(R.string.countdown_call, DigitUtils.getFixedPoint(millisUntilFinished.toInt(), 4))
                tvCountdown.visibility = View.VISIBLE
            }

            override fun onFinish() {
                tvCountdown.visibility = View.GONE
                val number = autoCompleteTextView.text.toString()
                val isAlert = mSharedPreferences.getBoolean("switch_preference_alert", true)
                if (isAlert) {
                    alert(getString(R.string.call_alert, number)) {
                        yesButton {
                            autoCompleteTextView.text = null
                            call(number)
                        }
                        noButton {
                            startCountDown()
                        }
                    }.show()
                } else {
                    autoCompleteTextView.text = null
                    call(number)
                }
            }
        }.start()
    }

    private fun stopCountDown() {
        countDownTimer?.cancel()
        tvCountdown.visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.menu_settings) {
            startActivity<SettingsActivity>()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        stopCountDown()
    }
}
