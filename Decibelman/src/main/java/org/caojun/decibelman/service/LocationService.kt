package org.caojun.decibelman.service

import android.content.Context
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption

/**
 * Created by CaoJun on 2017/9/14.
 */
class LocationService {
    private var client: LocationClient? = null
    private var option: LocationClientOption? = null

    constructor(context: Context) {
        if (client == null) {
            client = LocationClient(context)
            client?.locOption = getDefaultLocationClientOption()
        }
    }

    fun registerListener(listener: BDAbstractLocationListener?): Boolean {
        if (listener != null) {
            client?.registerLocationListener(listener)
            return true
        }
        return false
    }

    fun unregisterListener(listener: BDAbstractLocationListener?) {
        if (listener != null) {
            client?.unRegisterLocationListener(listener)
        }
    }

    private fun getDefaultLocationClientOption(): LocationClientOption {
        if (option == null) {
            option = LocationClientOption()
            option?.locationMode = LocationClientOption.LocationMode.Hight_Accuracy//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
            option?.setCoorType("bd09ll")//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
            option?.setScanSpan(0)//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
            option?.setIsNeedAddress(true)//可选，设置是否需要地址信息，默认不需要
            option?.setIsNeedLocationDescribe(true)//可选，设置是否需要地址描述
            option?.setNeedDeviceDirect(false)//可选，设置是否需要设备方向结果
            option?.isLocationNotify = false//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
            option?.setIgnoreKillProcess(true)//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
            option?.setIsNeedLocationDescribe(true)//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
            option?.setIsNeedLocationPoiList(true)//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
            option?.SetIgnoreCacheException(false)//可选，默认false，设置是否收集CRASH信息，默认收集
            option?.setIsNeedAltitude(false)//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        }
        return option!!
    }

    fun start() {
        synchronized(this) {
            if (client?.isStarted == false) {
                client?.start()
            }
        }
    }

    fun stop() {
        synchronized(this) {
            if (client?.isStarted == true) {
                client?.stop()
            }
        }
    }
}