package org.caojun.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager

/**
 * Created by CaoJun on 2017/9/26.
 */
object DeviceUtils {

    @SuppressLint("HardwareIds")
    fun getImei(context: Context): String {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                return tm.imei
            } catch (e: SecurityException) {
            }
        }
        @Suppress("DEPRECATION")
        return tm.deviceId
    }
}