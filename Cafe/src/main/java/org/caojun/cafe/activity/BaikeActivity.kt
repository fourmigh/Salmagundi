package org.caojun.cafe.activity

import android.content.Context
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceActivity
import android.preference.PreferenceFragment
import org.caojun.cafe.R
import android.preference.ListPreference
import android.text.TextUtils

class BaikeActivity : PreferenceActivity() {

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

    class SettingsFragment : PreferenceFragment(), Preference.OnPreferenceChangeListener {

        override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
            refresh(preference as ListPreference, newValue as String)
            return true
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            preferenceManager.sharedPreferencesName = PREFER_NAME
            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.baike)

            val lp = findPreference("lp_baike") as ListPreference
            lp.onPreferenceChangeListener = this

            if (TextUtils.isEmpty(lp.value)) {
                lp.setValueIndex(0)
            }

            val mSharedPreferences = activity.getSharedPreferences(BaikeActivity.PREFER_NAME, Context.MODE_PRIVATE)
            var url = mSharedPreferences.getString("lp_baike", "")
            refresh(lp, url)
        }

        private fun refresh(lp: ListPreference, value: String) {
            val index = lp.findIndexOfValue(value)
            val baikes = resources.getStringArray(R.array.baike)

            lp.summary = baikes[index]
        }
    }
}