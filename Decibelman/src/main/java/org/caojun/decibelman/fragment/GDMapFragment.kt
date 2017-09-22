package org.caojun.decibelman.fragment

import android.app.Fragment
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.LocationSource
import com.amap.api.maps.model.MyLocationStyle
import kotlinx.android.synthetic.main.fragment_gdmap.*
import org.caojun.decibelman.R
import org.jetbrains.anko.toast

/**
 * Created by CaoJun on 2017/9/13.
 */
class GDMapFragment : Fragment(), LocationSource, AMapLocationListener {

    private var aMap: AMap? = null
    private var mLocationChangedListener: LocationSource.OnLocationChangedListener? = null
    private var mLocationClient: AMapLocationClient? = null
    private var mLocationOption: AMapLocationClientOption? = null
    private var savedInstanceState: Bundle? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_gdmap, null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.savedInstanceState = savedInstanceState
    }

    override fun onResume() {
        super.onResume()
        gdMapView.onCreate(savedInstanceState)
        initialize()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        deactivate()
    }

    override fun onLocationChanged(amapLocation: AMapLocation?) {
        mLocationChangedListener?.onLocationChanged(amapLocation)// 显示系统小蓝点
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

    /**
     * 激活定位
     */
    override fun activate(listener: LocationSource.OnLocationChangedListener?) {
        mLocationChangedListener = listener
        if (mLocationClient == null) {
            mLocationClient = AMapLocationClient(activity)
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

    private fun initialize() {
        if (aMap != null) {
            return
        }
        aMap = gdMapView.map
        aMap?.uiSettings?.isRotateGesturesEnabled = true//设置是否可旋转
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
        aMap?.uiSettings?.isMyLocationButtonEnabled = true// 设置默认定位按钮是否显示
        aMap?.uiSettings?.isTiltGesturesEnabled = true//设置是否可倾斜
    }
}