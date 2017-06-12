package org.caojun.salmagundi.sysinfo;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.widget.ScrollView;
import android.widget.TextView;

import org.caojun.salmagundi.BaseActivity;
import org.caojun.salmagundi.R;
import org.caojun.salmagundi.utils.TimeUtils;

/**
 * Created by CaoJun on 2017/6/9.
 */

public class SysinfoActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ScrollView scrollView = new ScrollView(this);
        final TextView tvInfo = new TextView(this);
        tvInfo.setText(getSystemInfo());
        scrollView.addView(tvInfo);
        this.setContentView(scrollView);
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
        sb.append("\n" + "Build.BOARD: " + Build.BOARD);
        sb.append("\n" + "Build.BOOTLOADER: " + Build.BOOTLOADER);
        sb.append("\n" + "Build.BRAND: " + Build.BRAND);
        sb.append("\n" + "Build.DEVICE: " + Build.DEVICE);
        sb.append("\n" + "Build.DISPLAY: " + Build.DISPLAY);
        sb.append("\n" + "Build.FINGERPRINT: " + Build.FINGERPRINT);
        sb.append("\n" + "Build.getRadioVersion(): " + Build.getRadioVersion());
        sb.append("\n" + "Build.HARDWARE: " + Build.HARDWARE);
        sb.append("\n" + "Build.HOST: " + Build.HOST);
        sb.append("\n" + "Build.ID: " + Build.ID);
        sb.append("\n" + "Build.MANUFACTURER: " + Build.MANUFACTURER);
        sb.append("\n" + "Build.MODEL: " + Build.MODEL);
        sb.append("\n" + "Build.PRODUCT: " + Build.PRODUCT);
        sb.append("\n" + "Build.SERIAL: " + Build.SERIAL);
        sb.append("\n" + "Build.TAGS: " + Build.TAGS);
        sb.append("\n" + "Build.TYPE: " + Build.TYPE);
        sb.append("\n" + "Build.UNKNOWN: " + Build.UNKNOWN);
        sb.append("\n" + "Build.USER: " + Build.USER);
        sb.append("\n" + "_Build.CPU_ABI: " + Build.CPU_ABI);
        sb.append("\n" + "_Build.CPU_ABI2: " + Build.CPU_ABI2);
        sb.append("\n" + "_Build.RADIO: " + Build.RADIO);
        String time = TimeUtils.getTime("yyyy/MM/dd HH:mm:ss", Build.TIME);
        sb.append("\n" + "Build.TIME: " + Build.TIME + "(" + time + ")");
        sb.append("\n" + "Build.VERSION.BASE_OS: " + Build.VERSION.BASE_OS);
        sb.append("\n" + "Build.VERSION.CODENAME: " + Build.VERSION.CODENAME);
        sb.append("\n" + "Build.VERSION.INCREMENTAL: " + Build.VERSION.INCREMENTAL);
        sb.append("\n" + "Build.VERSION.RELEASE: " + Build.VERSION.RELEASE);
        sb.append("\n" + "Build.VERSION.SECURITY_PATCH: " + Build.VERSION.SECURITY_PATCH);
        sb.append("\n" + "_Build.VERSION.SDK: " + Build.VERSION.SDK);
        sb.append("\n" + "Build.VERSION.PREVIEW_SDK_INT: " + Build.VERSION.PREVIEW_SDK_INT);
        sb.append("\n" + "Build.VERSION.SDK_INT: " + Build.VERSION.SDK_INT);
        sb.append("\n" + "Build.VERSION_CODES.BASE: " + Build.VERSION_CODES.BASE);
        sb.append("\n" + "Build.VERSION_CODES.BASE_1_1: " + Build.VERSION_CODES.BASE_1_1);
        sb.append("\n" + "Build.VERSION_CODES.CUPCAKE: " + Build.VERSION_CODES.CUPCAKE);
        sb.append("\n" + "Build.VERSION_CODES.CUR_DEVELOPMENT: " + Build.VERSION_CODES.CUR_DEVELOPMENT);
        sb.append("\n" + "Build.VERSION_CODES.DONUT: " + Build.VERSION_CODES.DONUT);
        sb.append("\n" + "Build.VERSION_CODES.ECLAIR: " + Build.VERSION_CODES.ECLAIR);
        sb.append("\n" + "Build.VERSION_CODES.ECLAIR_0_1: " + Build.VERSION_CODES.ECLAIR_0_1);
        sb.append("\n" + "Build.VERSION_CODES.ECLAIR_MR1: " + Build.VERSION_CODES.ECLAIR_MR1);
        sb.append("\n" + "Build.VERSION_CODES.FROYO: " + Build.VERSION_CODES.FROYO);
        sb.append("\n" + "Build.VERSION_CODES.GINGERBREAD: " + Build.VERSION_CODES.GINGERBREAD);
        sb.append("\n" + "Build.VERSION_CODES.GINGERBREAD_MR1: " + Build.VERSION_CODES.GINGERBREAD_MR1);
        sb.append("\n" + "Build.VERSION_CODES.HONEYCOMB: " + Build.VERSION_CODES.HONEYCOMB);
        sb.append("\n" + "Build.VERSION_CODES.HONEYCOMB_MR1: " + Build.VERSION_CODES.HONEYCOMB_MR1);
        sb.append("\n" + "Build.VERSION_CODES.HONEYCOMB_MR2: " + Build.VERSION_CODES.HONEYCOMB_MR2);
        sb.append("\n" + "Build.VERSION_CODES.ICE_CREAM_SANDWICH: " + Build.VERSION_CODES.ICE_CREAM_SANDWICH);
        sb.append("\n" + "Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1: " + Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1);
        sb.append("\n" + "Build.VERSION_CODES.JELLY_BEAN: " + Build.VERSION_CODES.JELLY_BEAN);
        sb.append("\n" + "Build.VERSION_CODES.JELLY_BEAN_MR1: " + Build.VERSION_CODES.JELLY_BEAN_MR1);
        sb.append("\n" + "Build.VERSION_CODES.JELLY_BEAN_MR2: " + Build.VERSION_CODES.JELLY_BEAN_MR2);
        sb.append("\n" + "Build.VERSION_CODES.KITKAT: " + Build.VERSION_CODES.KITKAT);
        sb.append("\n" + "Build.VERSION_CODES.KITKAT_WATCH: " + Build.VERSION_CODES.KITKAT_WATCH);
        sb.append("\n" + "Build.VERSION_CODES.LOLLIPOP: " + Build.VERSION_CODES.LOLLIPOP);
        sb.append("\n" + "Build.VERSION_CODES.LOLLIPOP_MR1: " + Build.VERSION_CODES.LOLLIPOP_MR1);
        sb.append("\n" + "Build.VERSION_CODES.M: " + Build.VERSION_CODES.M);
        String[] SUPPORTED_32_BIT_ABIS = Build.SUPPORTED_32_BIT_ABIS;
        for (int i = 0;i < SUPPORTED_32_BIT_ABIS.length;i ++) {
            sb.append("\n" + "Build.SUPPORTED_32_BIT_ABIS[" + i + "]: " + SUPPORTED_32_BIT_ABIS[i]);
        }
        String[] SUPPORTED_64_BIT_ABIS = Build.SUPPORTED_64_BIT_ABIS;
        for (int i = 0;i < SUPPORTED_64_BIT_ABIS.length;i ++) {
            sb.append("\n" + "Build.SUPPORTED_64_BIT_ABIS[" + i + "]: " + SUPPORTED_64_BIT_ABIS[i]);
        }
        String[] SUPPORTED_ABIS = Build.SUPPORTED_ABIS;
        for (int i = 0;i < SUPPORTED_ABIS.length;i ++) {
            sb.append("\n" + "Build.SUPPORTED_ABIS[" + i + "]: " + SUPPORTED_ABIS[i]);
        }
        return sb.toString();
    }
}
