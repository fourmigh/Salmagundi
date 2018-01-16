package org.caojun.ttschulte.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.preference.PreferenceFragment
import org.caojun.ttschulte.R

/**
 * Created by CaoJun on 2017/9/25.
 */
class SettingsActivity: AppCompatActivity() {

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