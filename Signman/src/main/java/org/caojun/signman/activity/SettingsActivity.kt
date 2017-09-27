package org.caojun.signman.activity

import android.app.Activity
import android.os.Bundle
import android.preference.PreferenceFragment
import org.caojun.signman.R

/**
 * Created by CaoJun on 2017/9/25.
 */
class SettingsActivity: Activity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentManager.beginTransaction().replace(android.R.id.content, SettingsFragment()).commit()
    }

    class SettingsFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.settings)
        }
    }
}