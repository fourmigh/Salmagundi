package org.caojun.activity

import android.content.Context
import android.support.v7.app.AppCompatActivity
import org.caojun.utils.ActivityUtils

/**
 * Created by CaoJun on 2017/9/26.
 */
open class BaseAppCompatActivity : AppCompatActivity() {

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        ActivityUtils.onRequestPermissionsResult(grantResults)
    }

    fun checkSelfPermission(permission: String, listener: ActivityUtils.RequestPermissionListener): Boolean {
        return ActivityUtils.checkSelfPermission(this, permission, listener)
    }

    fun getIMEI(listener: ActivityUtils.RequestPermissionListener) {
        ActivityUtils.getIMEI(this, listener)
    }

    fun call(number: String) {
        ActivityUtils.call(this, number)
    }

    fun getSharedPreferences(name: String, key: String, defValue: Boolean): Boolean {
        return ActivityUtils.getSharedPreferences(this, name, key, defValue)
    }

    fun getSharedPreferences(name: String, key: String, defValue: String): String {
        return ActivityUtils.getSharedPreferences(this, name, key, defValue)
    }

    fun getSharedPreferences(name: String, key: String, defValue: Int): Int {
        return ActivityUtils.getSharedPreferences(this, name, key, defValue)
    }

    fun getString(context: Context, name: String): Int? {
        return ActivityUtils.getStringResId(context, name)
    }

    fun getRaw(context: Context, name: String): Int? {
        return ActivityUtils.getRowResId(context, name)
    }
}