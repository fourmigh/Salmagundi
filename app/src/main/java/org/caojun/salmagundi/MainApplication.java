package org.caojun.salmagundi;

import android.app.Application;

import com.socks.library.KLog;

/**
 * Application类
 * Created by CaoJun on 2016/10/27.
 */

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        KLog.init(true);
    }
}
