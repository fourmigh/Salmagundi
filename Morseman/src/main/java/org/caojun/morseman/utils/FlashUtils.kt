package org.caojun.morseman.utils

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Camera
import org.caojun.morseman.MainActivity

/**
 * Created by CaoJun on 2017/10/10.
 */
object FlashUtils {

    var camera: Camera? = null

    fun on(context: Context) {
        set(context, true)
    }

    fun off(context: Context) {
        set(context, false)
    }

    private fun set(context: Context, on: Boolean) {

        if (!context.applicationContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            return;
        }

        if (camera == null) {
            try {
                camera = Camera.open()
            } catch (e: RuntimeException) {
                return
            }
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
    }

//    fun release(context: Context) {
//        off(context)
//        if (camera != null) {
//            camera?.release()
//            camera = null
//        }
//    }
}