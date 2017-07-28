package org.caojun.bloodpressure.activity;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import com.alibaba.android.arouter.facade.annotation.Route;
import org.caojun.bloodpressure.Constant;
import org.caojun.bloodpressure.R;

/**
 * Created by CaoJun on 2017/7/28.
 */

@Route(path = Constant.ACTIVITY_NOTIFICATION_SETTINGS)
public class NotificaitonSettings extends PreferenceActivity implements Preference.OnPreferenceChangeListener {
    public static final String PREFER_NAME = "org.caojun.bloodpressure.notificaiton.setting";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 指定保存文件名字
        getPreferenceManager().setSharedPreferencesName(PREFER_NAME);
        addPreferencesFromResource(R.xml.notificaiton_setting);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return true;
    }
}
