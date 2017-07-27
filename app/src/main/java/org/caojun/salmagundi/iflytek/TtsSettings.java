package org.caojun.salmagundi.iflytek;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import com.alibaba.android.arouter.facade.annotation.Route;
import org.caojun.salmagundi.Constant;
import org.caojun.salmagundi.R;

/**
 * Created by CaoJun on 2017/7/26.
 */

@Route(path = Constant.ACTIVITY_FLYTEK_TTSSETTINGS)
public class TtsSettings extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    public static final String PREFER_NAME = "com.iflytek.setting";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 指定保存文件名字
        getPreferenceManager().setSharedPreferencesName(PREFER_NAME);
        addPreferencesFromResource(R.xml.tts_setting);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return true;
    }
}
