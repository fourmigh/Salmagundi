package org.caojun.morseman.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Camera
import android.hardware.camera2.CameraManager
import android.os.Build
import org.caojun.activity.BaseActivity
import org.caojun.activity.BaseAppCompatActivity
import org.caojun.utils.ActivityUtils.RequestPermissionListener

/**
 * Created by CaoJun on 2018-3-22.
 */
object FlashlightUtils {

    var camera: Camera? = null
    var manager: CameraManager? = null

    fun init(activity: BaseAppCompatActivity, listener: RequestPermissionListener) {
        activity.checkSelfPermission(Manifest.permission.CAMERA, listener)
    }

    fun init(activity: BaseActivity, listener: RequestPermissionListener) {
        activity.checkSelfPermission(Manifest.permission.CAMERA, listener)
    }

    fun on(context: Context) {
        set(context, true)
    }

    fun off(context: Context) {
        set(context, false)
    }

    fun initCamera(): Boolean {
        if (camera == null) {
            try {
                camera = Camera.open()
            } catch (e: Exception) {
                return false
            }
        }
        return true
    }

    fun releaseCamera() {
        camera?.setPreviewCallback(null)
        camera?.stopPreview()
        camera?.release()
        camera = null
    }

    private fun set(context: Context, on: Boolean) {

        if (!context.applicationContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            return;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (!initCamera()) {
                return
            }

            try {
                val params = camera?.parameters
                if (on) {
                    params?.flashMode = Camera.Parameters.FLASH_MODE_TORCH
                } else {
                    params?.flashMode = Camera.Parameters.FLASH_MODE_OFF
                }
                camera?.parameters = params
            } catch (e: Exception) {
            }
        } else {

            if (manager == null) {
                try {
                    manager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
                } catch (e: Exception) {
                    return
                }
            }

            try {
                manager?.setTorchMode("0", on)
            } catch (e: Exception) {
            }
        }
    }
}