package org.caojun.rcn

import android.app.Application
import com.socks.library.KLog
import org.caojun.rcn.BuildConfig.DEBUG

/**
 * Created by CaoJun on 2017/8/7.
 */
class MainApplication : Application {
    constructor() : super()

    override fun onCreate() {
        super.onCreate()
        KLog.init(DEBUG)
    }
}