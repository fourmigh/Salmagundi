package org.caojun.rotaryphone.activity

import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceActivity
import org.caojun.rotaryphone.R
import android.preference.PreferenceFragment



class SettingsActivity: PreferenceActivity(), Preference.OnPreferenceChangeListener {

    companion object {
        val PREFER_NAME = "org.caojun.rotaryphone.settings"
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
            addPreferencesFromResource(R.xml.settings)
        }
    }
}