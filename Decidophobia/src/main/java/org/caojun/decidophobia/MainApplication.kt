package org.caojun.decidophobia

import android.app.Application
import com.socks.library.KLog
import org.caojun.decidophobia.BuildConfig.DEBUG

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