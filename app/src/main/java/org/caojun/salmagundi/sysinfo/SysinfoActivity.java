package org.caojun.salmagundi.sysinfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.widget.TextView;

import org.caojun.salmagundi.BaseActivity;
import org.caojun.salmagundi.R;

/**
 * Created by CaoJun on 2017/6/9.
 */

public class SysinfoActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tvInfo = new TextView(this);
        tvInfo.setText(getSystemInfo());
        this.setContentView(tvInfo);
    }

    private String getSystemInfo() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        StringBuffer sb = new StringBuffer();
        sb.append("widthPixels: " + metrics.widthPixels);
        sb.append("\n" + "heightPixels: " + metrics.heightPixels);
        sb.append("\n" + "density: " + metrics.density);
        sb.append("\n" + "densityDpi: " + metrics.densityDpi);
        sb.append("\n" + "scaledDensity: " + metrics.scaledDensity);
        sb.append("\n" + "xdpi: " + metrics.xdpi);
        sb.append("\n" + "ydpi: " + metrics.ydpi);
        sb.append("\n" + "DPI: " + this.getString(R.string.dpi));
        return sb.toString();
    }
}
