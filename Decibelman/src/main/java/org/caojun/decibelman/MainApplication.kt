package org.caojun.decibelman

import android.app.Application
import cn.bmob.v3.Bmob
import cn.bmob.v3.BmobConfig
import org.caojun.decibelman.utils.ManifestUtils

/**
 * Created by CaoJun on 2017/9/30.
 */
class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
//        val config = BmobConfig.Builder(this)
//                //设置appkey
//                .setApplicationId(ManifestUtils.getApplicationMetaData(this, "cn.bmob.v3.appid"))
//                //请求超时时间（单位为秒）：默认15s
//                .setConnectTimeout(30)
//                //文件分片上传时每片的大小（单位字节），默认512*1024
//                .setUploadBlockSize(1024 * 1024)
//                //文件的过期时间(单位为秒)：默认1800s
//                .setFileExpiration(5500)
//                .build()
//        Bmob.initialize(config)
        Bmob.initialize(this, ManifestUtils.getApplicationMetaData(this, "cn.bmob.v3.appid"))
    }
}