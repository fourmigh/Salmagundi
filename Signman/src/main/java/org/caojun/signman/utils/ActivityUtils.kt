package org.caojun.signman.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.preference.PreferenceManager
import org.caojun.signman.R

/**
 * Created by CaoJun on 2017/9/5.
 */
object ActivityUtils {

    private val anims = arrayOf(
            intArrayOf(R.anim.fade, R.anim.hold),
            intArrayOf(R.anim.my_scale_action, R.anim.my_alpha_action),
            intArrayOf(R.anim.scale_rotate, R.anim.my_alpha_action),
            intArrayOf(R.anim.scale_translate_rotate, R.anim.my_alpha_action),
            intArrayOf(R.anim.scale_translate, R.anim.my_alpha_action),
            intArrayOf(R.anim.hyperspace_in, R.anim.hyperspace_out),
            intArrayOf(R.anim.push_left_in, R.anim.push_left_out),
            intArrayOf(R.anim.push_up_in, R.anim.push_up_out),
            intArrayOf(R.anim.slide_left, R.anim.slide_right),
            intArrayOf(R.anim.wave_scale, R.anim.my_alpha_action),
            intArrayOf(R.anim.zoom_enter, R.anim.zoom_exit),
            intArrayOf(R.anim.slide_up_in, R.anim.slide_down_out)
    )

    fun startActivity(context: Context, packageName: String): Boolean {
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        try {
            context.startActivity(intent)
            val settings = PreferenceManager.getDefaultSharedPreferences(context)
            if (settings.getBoolean(context.getString(R.string.settings_anim_key), false)) {
                val index = RandomUtils.getRandom(0, anims.size)
                if (index < anims.size) {
                    (context as Activity).overridePendingTransition(anims[index][0], anims[index][1])
                }
            }
            return true
        } catch (e: Exception) {
            return false
        }
    }

    fun gotoMarket(context: Context, packageName: String) {
        val uri = Uri.parse("market://details?id=" + packageName)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}