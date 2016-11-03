package org.caojun.salmagundi;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import org.caojun.salmagundi.utils.PackageUtils;

/**
 * Activity基类
 * Created by CaoJun on 2016/10/31.
 */

public class BaseActivity extends AppCompatActivity {

    protected ImageButton ibGallery3d;
    private Drawable iconGallery3d;
    protected String labelGallery3d;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ibGallery3d = (ImageButton) this.findViewById(R.id.ibGallery3d);
        if(ibGallery3d != null) {
            ibGallery3d.setVisibility(View.GONE);
            ibGallery3d.setEnabled(false);

            new Thread() {
                @Override
                public void run() {
                    PackageInfo packageInfo = PackageUtils.getPackageInfo(BaseActivity.this, "com.android.gallery3d");
                    if (packageInfo != null) {
                        PackageManager pm = BaseActivity.this.getPackageManager();
                        iconGallery3d = packageInfo.applicationInfo.loadIcon(pm);
                        labelGallery3d = packageInfo.applicationInfo.loadLabel(pm).toString();
                        handlerGallery3d.sendMessage(Message.obtain());
                    }
                }
            }.start();
        }
    }

    private Handler handlerGallery3d = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            try {
                ibGallery3d.setImageDrawable(iconGallery3d);
                ibGallery3d.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
