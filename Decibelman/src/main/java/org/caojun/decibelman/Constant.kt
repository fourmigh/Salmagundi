package org.caojun.decibelman

import com.amap.api.location.AMapLocation
import org.caojun.decibelman.room.DecibelInfo

/**
 * Created by CaoJun on 2017/9/18.
 */
object Constant {
    val Sound_Permission = android.Manifest.permission.RECORD_AUDIO
    val RequestCode_Permissions = 1

    var average: Float = 0f
    var min: Float = Float.MAX_VALUE
    var max: Float = Float.MIN_VALUE
    var decibelInfo0: DecibelInfo? = null
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var amapLocation: AMapLocation? = null
}