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
//    private SeekBarPreference mSpeedPreference;
//    private SeekBarPreference mPitchPreference;
//    private SeekBarPreference mVolumePreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 指定保存文件名字
        getPreferenceManager().setSharedPreferencesName(PREFER_NAME);
        addPreferencesFromResource(R.xml.tts_setting);
//        mSpeedPreference = (SeekBarPreference) findPreference("speed_preference");
//        mPitchPreference = (SeekBarPreference) findPreference("pitch_preference");
//        mVolumePreference = (SeekBarPreference) findPreference("volume_preference");
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return true;
    }
}
