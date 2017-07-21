package org.caojun.cameracolor;

import android.app.Application;
import com.alibaba.android.arouter.launcher.ARouter;
import com.socks.library.KLog;
import static org.caojun.cameracolor.BuildConfig.DEBUG;

/**
 * Application类
 * Created by CaoJun on 2016/10/27.
 */

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        KLog.init(DEBUG);
        if (DEBUG) {
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化
    }
}
