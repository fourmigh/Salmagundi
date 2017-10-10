package org.caojun.morseman.utils

import android.content.Context
import android.hardware.Camera
import android.hardware.camera2.CameraManager
import android.os.Build

/**
 * Created by CaoJun on 2017/10/10.
 */
object FlashUtils {

    fun on(context: Context) {
        set(context, true)
    }

    fun off(context: Context) {
        set(context, false)
    }

    private fun set(context: Context, on: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val manager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            manager.setTorchMode("0", on);
        } else {
            try {
                val mCamera = Camera.open()
                val mParameters: Camera.Parameters
                mParameters = mCamera.parameters
                if (on) {
                    mParameters.flashMode = Camera.Parameters.FLASH_MODE_TORCH
                } else {
                    mParameters.flashMode = Camera.Parameters.FLASH_MODE_OFF
                }
                mCamera.parameters = mParameters
            } catch (e: Exception) {
            }
        }
    }
}