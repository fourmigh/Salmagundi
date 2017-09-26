package org.caojun.decibelman.activity

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.LocationSource
import com.amap.api.maps.model.*
import kotlinx.android.synthetic.main.fragment_gdmap.*
import org.caojun.decibelman.Constant
import org.caojun.decibelman.R
import org.caojun.decibelman.room.DecibelInfo
import org.caojun.decibelman.utils.DigitUtils
import org.caojun.decibelman.utils.TimeUtils
import org.jetbrains.anko.doAsync

/**
 * Created by CaoJun on 2017/9/25.
 */
class GDMapActivity: BaseActivity(), LocationSource, AMapLocationListener, AMap.OnMarkerClickListener {

    private var aMap: AMap? = null
    private var mLocationChangedListener: LocationSource.OnLocationChangedListener? = null
    private var mLocationClient: AMapLocationClient? = null
    private var mLocationOption: AMapLocationClientOption? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_gdmap)
        gdMapView?.onCreate(savedInstanceState)
        initialize()
        setOnDatabaseListener(object : OnDatabaseListener {
            override fun onSuccess() {
                addMarkersToMap()
            }

            override fun onError() {
            }
        })
        addMarkersToMap()
    }

    override fun onResume() {
        super.onResume()
        gdMapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        gdMapView?.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        gdMapView?.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        gdMapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        gdMapView?.onLowMemory()
    }

    private fun initialize() {
        if (aMap != null) {
            return
        }
        aMap = gdMapView.map
        aMap?.uiSettings?.isRotateGesturesEnabled = true//旋转手势
        aMap?.moveCamera(CameraUpdateFactory.zoomBy(6f))
        aMap?.setLocationSource(this)// 设置定位监听
        // 自定义系统定位蓝点
        val myLocationStyle = MyLocationStyle()
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0))
        // 自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(0f)
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0))
        // 将自定义的 myLocationStyle 对象添加到地图上
        aMap?.myLocationStyle = myLocationStyle
        aMap?.isMyLocationEnabled = true// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap?.uiSettings?.isMyLocationButtonEnabled = true// 定位按钮
        aMap?.uiSettings?.isTiltGesturesEnabled = true//倾斜
        aMap?.uiSettings?.isCompassEnabled = true//指南针
        aMap?.uiSettings?.isScaleControlsEnabled = true//比例尺
        aMap?.setOnMarkerClickListener(this)
    }

    /**
     * 激活定位
     */
    override fun activate(listener: LocationSource.OnLocationChangedListener?) {
        mLocationChangedListener = listener
        if (mLocationClient == null) {
            mLocationClient = AMapLocationClient(this)
            mLocationOption = AMapLocationClientOption()
            // 设置定位监听
            mLocationClient?.setLocationListener(this)
            // 设置为高精度定位模式
            mLocationOption?.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
            // 只是为了获取当前位置，所以设置为单次定位
            mLocationOption?.isOnceLocation = (true)
            // 设置定位参数
            mLocationClient?.setLocationOption(mLocationOption)
            mLocationClient?.startLocation()
        }
    }

    /**
     * 停止定位
     */
    override fun deactivate() {
        mLocationChangedListener = null
        mLocationClient?.stopLocation()
        mLocationClient?.onDestroy()
        mLocationClient = null
    }

    override fun onLocationChanged(amapLocation: AMapLocation) {
        Constant.latitude = amapLocation.latitude
        Constant.longitude = amapLocation.longitude
        mLocationChangedListener?.onLocationChanged(amapLocation)// 显示系统小蓝点
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        if (TextUtils.isEmpty(marker.title) && TextUtils.isEmpty(marker.snippet)) {
            alertSaveDecibelInfo()
        } else if (marker.isInfoWindowShown) {
            marker.hideInfoWindow()
        } else {
            marker.showInfoWindow()
        }
        return true
    }

    private fun addMarkerToMap(di: DecibelInfo) {

        val latLng = LatLng(di.latitude, di.longitude)
        val time = TimeUtils.getTime("yyyy/MM/dd HH:mm:ss", di.time);

        val markerOption = MarkerOptions().icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .position(latLng)
                .title(getString(R.string.mark_title, time.toString()))
                .snippet(getString(R.string.decibel_info, DigitUtils.getRound(di.decibel_max, 1), DigitUtils.getRound(di.decibel_average, 1), DigitUtils.getRound(di.decibel_min, 1)))
                .draggable(true)
        val marker = aMap?.addMarker(markerOption)
        marker?.showInfoWindow()
    }

    private fun addMarkersToMap() {
        doAsync {
            val list = getDecibelInfos()
            if (list != null && !list.isEmpty()) {
                aMap?.clear()
                for (i in 1..(list.size - 1)) {
                    addMarkerToMap(list[i])
                }
            }
        }
    }
}