package org.caojun.salmagundi.today;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import org.caojun.salmagundi.Constant;
import org.caojun.salmagundi.R;

/**
 * Created by CaoJun on 2017/7/10.
 */

@Route(path = Constant.ACTIVITY_WEBVIEW)
public class WebviewActivity extends Activity {

    @Autowired
    protected String content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ARouter.getInstance().inject(this);

        final WebView webView = (WebView) findViewById(R.id.webView);
//        WebSettings settings = webView.getSettings();
//        settings.setLoadWithOverviewMode(true);
//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
    }
}
