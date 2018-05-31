package org.caojun.mapman

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.preference.ListPreference
import android.preference.Preference
import android.preference.PreferenceActivity
import android.preference.PreferenceFragment
import android.text.TextUtils
import android.widget.ImageView
import org.caojun.color.ColorActivity
import org.jetbrains.anko.startActivityForResult
import android.preference.EditTextPreference
import org.caojun.color.ColorUtils


class SettingsActivity: PreferenceActivity() {

    companion object {
        val PREFER_NAME = "org.caojun.mapman.settings"

        val Request_Selected_Color = 1
        val Request_Unselected_Color = 2
    }



    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 指定保存文件名字
        fragmentManager.beginTransaction()
                .replace(android.R.id.content, SettingsFragment())
                .commit()
    }

    class SettingsFragment : PreferenceFragment(), Preference.OnPreferenceChangeListener {

        companion object {
            val Key_Selected_Color = "map_selected_color"
            val Key_Unselected_Color = "map_unselected_color"
        }
        private var mSharedPreferences: SharedPreferences? = null
        private var ivSetColor: ImageView? = null
        private val Key_Baike = "lp_baike"

        override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
            if (preference is ListPreference) {
                update(preference, newValue as String)
            }
            return true
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            preferenceManager.sharedPreferencesName = PREFER_NAME
            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.settings)

            val lp = findPreference(Key_Baike) as ListPreference
            lp.onPreferenceChangeListener = this

            if (TextUtils.isEmpty(lp.value)) {
                lp.setValueIndex(0)
            }

            mSharedPreferences = activity.getSharedPreferences(SettingsActivity.PREFER_NAME, Context.MODE_PRIVATE)
            val url = mSharedPreferences?.getString(Key_Baike, "")?:""
            update(lp, url)

            val etpSelectedColor = findPreference("etpSelectedColor")
            etpSelectedColor.onPreferenceClickListener = object : Preference.OnPreferenceClickListener {
                override fun onPreferenceClick(preference: Preference?): Boolean {
                    val dialog = (preference as EditTextPreference).dialog
                    ivSetColor = dialog.findViewById(R.id.ivSetColor)
                    ivSetColor?.setOnClickListener {
                        val color = getColor(Key_Selected_Color, Color.BLUE)
                        startActivityForResult<ColorActivity>(Request_Selected_Color, ColorActivity.KEY_HEX to color)
                    }

                    var color = getColor(Key_Selected_Color, Color.BLUE)
                    ivSetColor?.setBackgroundColor(Color.parseColor("#$color"))
                    return false
                }
            }

            val etpUnselectedColor = findPreference("etpUnselectedColor")
            etpUnselectedColor.onPreferenceClickListener = object : Preference.OnPreferenceClickListener {
                override fun onPreferenceClick(preference: Preference?): Boolean {
                    val dialog = (preference as EditTextPreference).dialog
                    ivSetColor = dialog.findViewById(R.id.ivSetColor)
                    ivSetColor?.setOnClickListener {
                        val color = getColor(Key_Unselected_Color, Color.GRAY)
                        startActivityForResult<ColorActivity>(Request_Unselected_Color, ColorActivity.KEY_HEX to color)
                    }

                    var color = getColor(Key_Unselected_Color, Color.GRAY)
                    ivSetColor?.setBackgroundColor(Color.parseColor("#$color"))
                    return false
                }
            }
        }

        private fun getColor(key: String, defColor: Int): String {
            var color = mSharedPreferences?.getString(key, ColorUtils.toHexEncoding(defColor))?:""
            if (TextUtils.isEmpty(color)) {
                color = ColorUtils.toHexEncoding(defColor)
            }
            return color
        }

        private fun update(lp: ListPreference, value: String) {
            val index = lp.findIndexOfValue(value)
            val baikes = resources.getStringArray(R.array.baike)

            lp.summary = baikes[index]
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            if (resultCode == Activity.RESULT_OK) {
                when(requestCode) {
                    Request_Selected_Color, Request_Unselected_Color -> {
                        val color = data?.getStringExtra(ColorActivity.KEY_HEX)
                        val edit = mSharedPreferences!!.edit()
                        if (requestCode == Request_Selected_Color) {
                            edit.putString(Key_Selected_Color, color)
                        } else if (requestCode == Request_Unselected_Color) {
                            edit.putString(Key_Unselected_Color, color)
                        }
                        edit.commit()
                        ivSetColor?.setBackgroundColor(Color.parseColor("#$color"))
                        return
                    }
                }
            }
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}