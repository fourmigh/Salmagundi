package org.caojun.decibelman.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.TELEPHONY_SERVICE
import android.telephony.TelephonyManager

/**
 * Created by CaoJun on 2017/9/25.
 */
object DeviceUtils {
    @Suppress("DEPRECATION")
    @SuppressLint("HardwareIds", "MissingPermission")
    fun getIMEI(context: Context): String {
        val tm = context.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        return tm.deviceId
    }
}