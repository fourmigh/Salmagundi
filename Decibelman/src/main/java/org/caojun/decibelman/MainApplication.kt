package org.caojun.decibelman

import android.app.Application
import cn.bmob.v3.Bmob
import com.socks.library.KLog
import org.caojun.decibelman.BuildConfig.DEBUG
import org.caojun.decibelman.utils.ManifestUtils

/**
 * Created by CaoJun on 2017/9/30.
 */
class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        KLog.init(DEBUG)
        Bmob.initialize(this, ManifestUtils.getApplicationMetaData(this, "cn.bmob.v3.appid"))
    }
}