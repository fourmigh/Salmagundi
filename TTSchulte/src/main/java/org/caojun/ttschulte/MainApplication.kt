package org.caojun.ttschulte

import android.app.Application
import cn.bmob.v3.Bmob
import com.socks.library.KLog
import org.caojun.ttschulte.BuildConfig.DEBUG
import org.caojun.utils.ManifestUtils

/**
 * Created by CaoJun on 2017/8/7.
 */
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        KLog.init(DEBUG)
        Bmob.initialize(this, ManifestUtils.getApplicationMetaData(this, "cn.bmob.v3.appid"))
    }
}