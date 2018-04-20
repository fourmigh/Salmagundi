package org.caojun.decibelman.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import cn.bmob.v3.BmobQuery
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.LocationSource
import com.amap.api.maps.model.*
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import kotlinx.android.synthetic.main.fragment_gdmap.*
import org.caojun.decibelman.Constant
import org.caojun.decibelman.R
import org.caojun.decibelman.room.DIBmob
import org.caojun.decibelman.room.DecibelInfo
import org.caojun.decibelman.utils.DigitUtils
import org.caojun.decibelman.utils.TimeUtils
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import rx.Subscriber

/**
 * Created by CaoJun on 2017/9/25.
 */
class GDMapActivity: BaseActivity(), LocationSource, AMapLocationListener, AMap.OnMarkerClickListener {

    private var aMap: AMap? = null
    private var mLocationChangedListener: LocationSource.OnLocationChangedListener? = null
    private var mLocationClient: AMapLocationClient? = null
    private var mLocationOption: AMapLocationClientOption? = null
    private val Request_Select_Picture = 1

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

        fab.setOnCreateContextMenuListener(this)
        fab.setOnClickListener({
            fab.showContextMenu()
        })
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
        Constant.amapLocation = amapLocation
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
            if (!list.isEmpty()) {
                aMap?.clear()
                for (i in 1..(list.size - 1)) {
                    addMarkerToMap(list[i])
                }
            }

            val bmobQuery = BmobQuery<DIBmob>()
            val isCache = bmobQuery.hasCachedResult(DIBmob::class.java)
            if (isCache) {
                bmobQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK)    // 先从缓存取数据，如果没有的话，再从网络取。
            } else {
                bmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE)    // 如果没有缓存的话，则先从网络中取
            }
            bmobQuery.findObjectsObservable(DIBmob::class.java!!)
                    .subscribe(object : Subscriber<List<DIBmob>>() {
                        override fun onCompleted() {}

                        override fun onError(e: Throwable) {}

                        override fun onNext(persons: List<DIBmob>) {
                            if (!persons.isEmpty()) {
                                val ID = Constant.decibelInfo0!!.database_time.toString() + Constant.decibelInfo0!!.imei + Constant.decibelInfo0!!.random_id + Constant.decibelInfo0!!.time.toString()
                                for (i in 0 until persons.size) {
                                    val di = DecibelInfo(persons[i])
                                    val id = di.database_time.toString() + di.imei + di.random_id + di.time.toString()
                                    if (!id.equals(ID)) {
                                        addMarkerToMap(di)
                                    }
                                }
                            }
                        }
                    })
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu?.add(1, 0, 0, R.string.menu_upload)
        menu?.add(1, 1, 1, R.string.menu_share)
        menu?.add(1, 2, 2, R.string.menu_report)
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId?:0) {
            0 -> {
                //上传
                alertSaveDecibelInfo()
            }
            1 -> {
                //分享
                shareInfo(Constant.amapLocation, getSavedDecibelInfo())
            }
            2 -> {
                //拍照举报
                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                        .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                        .isCamera(true)// 是否显示拍照按钮 true or false
                        .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                        .enableCrop(false)// 是否裁剪 true or false
                        .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                        .forResult(Request_Select_Picture)//结果回调onActivityResult code
            }
        }
        return super.onContextItemSelected(item)
    }

    private fun shareInfo(amapLocation: AMapLocation?, di: DecibelInfo) {
        if (amapLocation == null || TextUtils.isEmpty(amapLocation.address)) {
            return
        }
        val text = getString(R.string.share_info, amapLocation.address, DigitUtils.getRound(di.decibel_max, 1), DigitUtils.getRound(di.decibel_average, 1), DigitUtils.getRound(di.decibel_min, 1))
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, text)
        intent.type = "text/plain"
        startActivity(Intent.createChooser(intent, getString(R.string.menu_share)))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) when (requestCode) {
            Request_Select_Picture -> {
                val selectList = PictureSelector.obtainMultipleResult(data)
                val path = selectList[0].path
                doReport(Constant.amapLocation, getSavedDecibelInfo(), path)
                return
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun doReport(amapLocation: AMapLocation?, di: DecibelInfo, path: String) {
        if (amapLocation == null || TextUtils.isEmpty(amapLocation.address)) {
            return
        }
        val text = getString(R.string.share_info, amapLocation.address, DigitUtils.getRound(di.decibel_max, 1), DigitUtils.getRound(di.decibel_average, 1), DigitUtils.getRound(di.decibel_min, 1))
        startActivity<ReportActivity>(ReportActivity.Key_Picture_Path to path, ReportActivity.Key_Report_Content to text)
    }
}