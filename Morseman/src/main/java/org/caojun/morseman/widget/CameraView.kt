package org.caojun.morseman.widget

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.hardware.Camera
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import org.caojun.morseman.jni.ImageUtilEngine
import org.caojun.morseman.listener.OnColorStatusChange
import org.caojun.morseman.utils.FlashUtils

/**
 * Created by CaoJun on 2017/10/13.
 */
class CameraView: SurfaceView, SurfaceHolder.Callback {

    private var mSurfaceHolder: SurfaceHolder? = null
//    private var mCamera: Camera? = null
    private var colorChange: OnColorStatusChange? = null
    private var imageEngine: ImageUtilEngine? = null
    private var isPaused = false

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initHolder()
    }

    private fun initHolder() {
        if (mSurfaceHolder == null) {
            mSurfaceHolder = this.holder
            mSurfaceHolder?.addCallback(this)
            mSurfaceHolder?.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        }
    }

    private fun initCamera()
    {
        FlashUtils.camera?.stopPreview()
        try {
            /* Camera Service settings */
            val parameters = FlashUtils.camera?.parameters
            parameters?.flashMode = "off" // 无闪光灯
            // 设置预览图片大小
            parameters?.focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO//视频自动对焦
            // 横竖屏镜头自动调整
            if (this.resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE) {
                parameters?.set("orientation", "portrait")
                // parameters.set("rotation", 90); // 镜头角度转90度（默认摄像头是横拍）
                FlashUtils.camera?.setDisplayOrientation(90) // 在2.2以上可以使用
            } else
            // 如果是横屏
            {
                parameters?.set("orientation", "landscape")
                FlashUtils.camera?.setDisplayOrientation(0) // 在2.2以上可以使用
            }
            /* 视频流编码处理 */
            // 添加对视频流处理函数
            // 设定配置参数并开启预览
            FlashUtils.camera?.parameters = parameters // 将Camera.Parameters设定予Camera
            FlashUtils.camera?.setPreviewCallback(Camera.PreviewCallback { data, camera ->
                if (isPaused) {
                    return@PreviewCallback
                }
                val mWidth = camera.parameters.previewSize.width
                val mHeight = camera.parameters.previewSize.height
                val buf = imageEngine?.decodeYUV420SP(data, mWidth, mHeight)
                val bitmap = Bitmap.createBitmap(buf, mWidth,
                        mHeight, Bitmap.Config.RGB_565)
                val color = bitmap.getPixel(mWidth / 2, mHeight / 2)
                colorChange?.onColorChange(color)
            })
            FlashUtils.camera?.startPreview() // 打开预览画面
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        if (holder?.surface == null) {
            return
        }
        mSurfaceHolder = holder
        initCamera()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        FlashUtils.camera?.setPreviewCallback(null) // ！！这个必须在前，不然退出出错
        FlashUtils.camera?.stopPreview()
        FlashUtils.camera?.release()
        FlashUtils.camera = null

        mSurfaceHolder = null
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
//        if (mCamera != null) {
//            return
//        }
//        mCamera = Camera.open()// 开启摄像头（2.3版本后支持多摄像头,需传入参数）
        if (FlashUtils.camera == null) {
            FlashUtils.camera = Camera.open()
        }
        imageEngine = ImageUtilEngine()
        try {
            FlashUtils.camera?.setPreviewDisplay(mSurfaceHolder)// 设置预览
        } catch (ex: Exception) {
            FlashUtils.camera?.release()
            FlashUtils.camera = null
        }
    }

    fun setOnColorStatusChange(colorChange: OnColorStatusChange) {
        this.colorChange = colorChange
    }

    fun onPause() {
        isPaused = true
    }

    fun onResume() {
        if (isPaused) {
            initHolder()
        }
        isPaused = false
    }
}