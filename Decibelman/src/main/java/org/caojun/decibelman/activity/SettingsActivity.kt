package org.caojun.decibelman.activity

import android.content.Context
import android.os.Bundle
import android.preference.Preference
import android.support.v7.app.AppCompatActivity
import org.caojun.decibelman.R
import android.preference.PreferenceFragment
import android.preference.SwitchPreference
import org.jetbrains.anko.toast

/**
 * Created by CaoJun on 2017/9/25.
 */
class SettingsActivity: AppCompatActivity() {

    companion object {
        val PREFER_NAME = "org.caojun.decibelman.settings"
        val Key_Switch_LP = "sp_switch_lp"
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentManager.beginTransaction().replace(android.R.id.content, SettingsFragment()).commit()
    }

    class SettingsFragment : PreferenceFragment(), Preference.OnPreferenceChangeListener {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            preferenceManager.sharedPreferencesName = PREFER_NAME
            addPreferencesFromResource(R.xml.settings)

            val sp = findPreference(Key_Switch_LP) as SwitchPreference
            sp.onPreferenceChangeListener = this

            val mSharedPreferences = activity.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE)
            var value = mSharedPreferences.getBoolean(Key_Switch_LP, true)
            refresh(sp, value)
        }

        override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
            refresh(preference as SwitchPreference, newValue as Boolean)
            toast(R.string.content_switch_lp)
            return true
        }

        private fun refresh(sp: SwitchPreference, newValue: Boolean) {
            sp.title = if (newValue) getString(R.string.portrait) else getString(R.string.landscape)
        }
    }
}