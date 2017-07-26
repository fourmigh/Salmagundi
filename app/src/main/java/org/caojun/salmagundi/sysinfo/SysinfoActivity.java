package org.caojun.salmagundi.sysinfo;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.TextView;
import com.alibaba.android.arouter.facade.annotation.Route;
import org.caojun.salmagundi.Constant;
import org.caojun.salmagundi.R;
import org.caojun.salmagundi.utils.TimeUtils;

/**
 * Created by CaoJun on 2017/6/9.
 */

@Route(path = Constant.ACTIVITY_SYSINFO)
public class SysinfoActivity extends Activity {
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
        StringBuffer sb = new StringBuffer();

        DisplayMetrics metrics = getResources().getDisplayMetrics();
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
        sb.append("\n" + "Build.VERSION.CODENAME: " + Build.VERSION.CODENAME);
        sb.append("\n" + "Build.VERSION.INCREMENTAL: " + Build.VERSION.INCREMENTAL);
        sb.append("\n" + "Build.VERSION.RELEASE: " + Build.VERSION.RELEASE);
        sb.append("\n" + "_Build.VERSION.SDK: " + Build.VERSION.SDK);
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

        TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        sb.append("\n" + "TelephonyManager.getDeviceId(): " + tm.getDeviceId());
        sb.append("\n" + "TelephonyManager.getDeviceSoftwareVersion(): " + tm.getDeviceSoftwareVersion());
        sb.append("\n" + "TelephonyManager.getGroupIdLevel1(): " + tm.getGroupIdLevel1());
        sb.append("\n" + "TelephonyManager.getLine1Number(): " + tm.getLine1Number());
        sb.append("\n" + "TelephonyManager.getMmsUAProfUrl(): " + tm.getMmsUAProfUrl());
        sb.append("\n" + "TelephonyManager.getMmsUserAgent(): " + tm.getMmsUserAgent());
        sb.append("\n" + "TelephonyManager.getNetworkCountryIso(): " + tm.getNetworkCountryIso());
        sb.append("\n" + "TelephonyManager.getNetworkOperator(): " + tm.getNetworkOperator());
        sb.append("\n" + "TelephonyManager.getNetworkOperatorName(): " + tm.getNetworkOperatorName());
        sb.append("\n" + "TelephonyManager.getSimCountryIso(): " + tm.getSimCountryIso());
        sb.append("\n" + "TelephonyManager.getSimOperator(): " + tm.getSimOperator());
        sb.append("\n" + "TelephonyManager.getSimOperatorName(): " + tm.getSimOperatorName());
        sb.append("\n" + "TelephonyManager.getSimSerialNumber(): " + tm.getSimSerialNumber());
        sb.append("\n" + "TelephonyManager.getSubscriberId(): " + tm.getSubscriberId());
        sb.append("\n" + "TelephonyManager.getVoiceMailAlphaTag(): " + tm.getVoiceMailAlphaTag());
        sb.append("\n" + "TelephonyManager.getVoiceMailNumber(): " + tm.getVoiceMailNumber());
//            sb.append("\n" + "TelephonyManager.canChangeDtmfToneLength(): " + tm.canChangeDtmfToneLength());
        sb.append("\n" + "TelephonyManager.getCallState(): " + tm.getCallState());
        sb.append("\n" + "TelephonyManager.getDataActivity(): " + tm.getDataActivity());
        sb.append("\n" + "TelephonyManager.getDataState(): " + tm.getDataState());
        sb.append("\n" + "TelephonyManager.getNetworkType(): " + tm.getNetworkType());
//            sb.append("\n" + "TelephonyManager.getPhoneCount(): " + tm.getPhoneCount());
        sb.append("\n" + "TelephonyManager.getPhoneType(): " + tm.getPhoneType());
        sb.append("\n" + "TelephonyManager.getSimState(): " + tm.getSimState());
//            sb.append("\n" + "TelephonyManager.hasCarrierPrivileges(): " + tm.hasCarrierPrivileges());
        sb.append("\n" + "TelephonyManager.hasIccCard(): " + tm.hasIccCard());
//            sb.append("\n" + "TelephonyManager.isHearingAidCompatibilitySupported(): " + tm.isHearingAidCompatibilitySupported());
        sb.append("\n" + "TelephonyManager.isNetworkRoaming(): " + tm.isNetworkRoaming());
        sb.append("\n" + "TelephonyManager.isSmsCapable(): " + tm.isSmsCapable());
//            sb.append("\n" + "TelephonyManager.isTtyModeSupported(): " + tm.isTtyModeSupported());
//            sb.append("\n" + "TelephonyManager.isVoiceCapable(): " + tm.isVoiceCapable());
//            sb.append("\n" + "TelephonyManager.isWorldPhone(): " + tm.isWorldPhone());

        WindowManager wm = (WindowManager) this.getSystemService(WINDOW_SERVICE);
        sb.append("\n" + "WindowManager.getDefaultDisplay().getName(): " + wm.getDefaultDisplay().getName());
        sb.append("\n" + "WindowManager.getDefaultDisplay().getAppVsyncOffsetNanos(): " + wm.getDefaultDisplay().getAppVsyncOffsetNanos());
        sb.append("\n" + "WindowManager.getDefaultDisplay().getDisplayId(): " + wm.getDefaultDisplay().getDisplayId());
        sb.append("\n" + "WindowManager.getDefaultDisplay().getFlags(): " + wm.getDefaultDisplay().getFlags());
        sb.append("\n" + "WindowManager.getDefaultDisplay().getPresentationDeadlineNanos(): " + wm.getDefaultDisplay().getPresentationDeadlineNanos());
        sb.append("\n" + "WindowManager.getDefaultDisplay().getRefreshRate(): " + wm.getDefaultDisplay().getRefreshRate());
        sb.append("\n" + "WindowManager.getDefaultDisplay().getRotation(): " + wm.getDefaultDisplay().getRotation());
        sb.append("\n" + "WindowManager.getDefaultDisplay().getState(): " + wm.getDefaultDisplay().getState());
        sb.append("\n" + "WindowManager.getDefaultDisplay().isValid(): " + wm.getDefaultDisplay().isValid());
        return sb.toString();
    }
}
