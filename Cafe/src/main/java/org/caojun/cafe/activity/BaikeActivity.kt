package org.caojun.cafe.activity

import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceActivity
import android.preference.PreferenceFragment
import org.caojun.cafe.R
import android.preference.ListPreference
import android.text.TextUtils


class BaikeActivity: PreferenceActivity(), Preference.OnPreferenceChangeListener {

    companion object {
        val PREFER_NAME = "org.caojun.cafe.baike"
    }


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 指定保存文件名字
        fragmentManager.beginTransaction()
                .replace(android.R.id.content, SettingsFragment())
                .commit()
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
        return true
    }

    class SettingsFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            preferenceManager.sharedPreferencesName = PREFER_NAME
            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.baike)

            val lp = findPreference("lp_baike") as ListPreference
            if(TextUtils.isEmpty(lp.value)) {
                lp.setValueIndex(0)
            }
        }
    }
}