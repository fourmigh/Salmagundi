package org.caojun.salmagundi.arouter;

import android.content.Context;
import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.alibaba.android.arouter.launcher.ARouter;
import org.caojun.salmagundi.Constant;

/**
 * 登录拦截器
 * Created by CaoJun on 2017/6/12.
 */

@Interceptor(priority = 8, name = "登录拦截器")
public class LoginInterceptor implements IInterceptor {

    private static Postcard postcard;
    private static InterceptorCallback interceptorCallback;

    @Override
    public void process(Postcard postcard, InterceptorCallback interceptorCallback) {
        if ((postcard.getExtra() & Constant.EXTRAS_LOGIN) == 1) {
            //需要登录
            ARouter.getInstance().build(Constant.ACTIVITY_GESTURE_LOGIN).navigation();
            LoginInterceptor.postcard = postcard;
            LoginInterceptor.interceptorCallback = interceptorCallback;
            return;
        }
        interceptorCallback.onContinue(postcard);
    }

    @Override
    public void init(Context context) {

    }

    /**
     * 登录完成
     */
    public static void onLogined() {
        if (LoginInterceptor.postcard != null && LoginInterceptor.interceptorCallback != null) {
            LoginInterceptor.interceptorCallback.onContinue(LoginInterceptor.postcard);
            LoginInterceptor.postcard = null;
            LoginInterceptor.interceptorCallback = null;
        }
    }
}
