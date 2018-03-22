package org.caojun.cameracolor.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import org.caojun.cameracolor.jni.ImageUtilEngine;
import org.caojun.cameracolor.listener.OnColorStatusChange;
import org.caojun.cameracolor.utils.ColorUtils;

public class CameraView extends SurfaceView implements SurfaceHolder.Callback {

	private SurfaceHolder mSurfaceHolder = null;
	private Camera mCamera = null;
	private OnColorStatusChange colorChange;
    private ImageUtilEngine imageEngine;
	private int colorOld;

	public CameraView(Context context) {
		super(context);
		initHolder();
	}

	public CameraView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHolder();
	}

	public CameraView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initHolder();
	}

	/**
	 * 初始化
	 */
	private void initHolder() {
		if (mSurfaceHolder == null) {
			mSurfaceHolder = this.getHolder();
			mSurfaceHolder.addCallback(this);
			mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
	}

	/**
	 * 大小发生改变的时候调用
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
		if (holder.getSurface() == null) {
			return;
		}
		mSurfaceHolder = holder;
		initCamera();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		create();
	}

	public void create() {
		if (mCamera != null) {
			return;
		}
		try {
			mCamera = Camera.open();// 开启摄像头（2.3版本后支持多摄像头,需传入参数）
		} catch (Exception e) {
			e.printStackTrace();
		}
		imageEngine = new ImageUtilEngine();
		try {
			mCamera.setPreviewDisplay(mSurfaceHolder);// 设置预览
		} catch (Exception ex) {
			if (null != mCamera) {
				mCamera.release();
				mCamera = null;
			}
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (null != mCamera) {
			mCamera.setPreviewCallback(null); // ！！这个必须在前，不然退出出错
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}

		mSurfaceHolder = null;
	}

	/**
	 * 初始化Camera
	 */
	public void initCamera()// surfaceChanged中调用
	{
		if (null != mCamera) {
			mCamera.stopPreview();// stopCamera();
			try {
				/* Camera Service settings */
				Camera.Parameters parameters = mCamera.getParameters();
				parameters.setFlashMode("off"); // 无闪光灯
				// 设置预览图片大小
				parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);//视频自动对焦
				// 横竖屏镜头自动调整
				if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
					parameters.set("orientation", "portrait"); //
					// parameters.set("rotation", 90); // 镜头角度转90度（默认摄像头是横拍）
					mCamera.setDisplayOrientation(90); // 在2.2以上可以使用
				} else// 如果是横屏
				{
					parameters.set("orientation", "landscape"); //
					mCamera.setDisplayOrientation(0); // 在2.2以上可以使用
				}
				/* 视频流编码处理 */
				// 添加对视频流处理函数
				// 设定配置参数并开启预览
				mCamera.setParameters(parameters); // 将Camera.Parameters设定予Camera
				mCamera.setPreviewCallback(new PreviewCallback() {
					@Override
					public void onPreviewFrame(byte[] data, Camera camera) {
						int mWidth = camera.getParameters().getPreviewSize().width;
						int mHeight = camera.getParameters().getPreviewSize().height;
						int[] buf = imageEngine.decodeYUV420SP(data, mWidth, mHeight);
						Bitmap bitmap = Bitmap.createBitmap(buf, mWidth,
								mHeight, Config.RGB_565);
						int color = bitmap.getPixel(mWidth / 2, mHeight / 2);
						if (ColorUtils.compareSpan(colorOld, color) > 10) {
							if (colorChange != null) {
                                colorChange.onColorChange(color);
							}
						}
						colorOld = color;
					}
				});
				mCamera.startPreview(); // 打开预览画面
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void setOnColorStatusChange(OnColorStatusChange colorChange) {
		this.colorChange = colorChange;
	}

	private boolean isPaused = false;

	public void onPause() {
		isPaused = true;
	}

	public void onResume() {
		if (isPaused) {
			initHolder();
		}
	}
}
