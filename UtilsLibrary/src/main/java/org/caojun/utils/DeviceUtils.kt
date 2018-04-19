package org.caojun.utils

import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager

/**
 * Created by CaoJun on 2017/9/26.
 */
object DeviceUtils {

    fun getImei(context: Context): String {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                tm.imei
            } else {
                tm.deviceId
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
            ""
        }

    }
}