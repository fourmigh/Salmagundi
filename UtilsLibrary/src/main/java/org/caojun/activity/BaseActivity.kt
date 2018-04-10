package org.caojun.activity

import android.app.Activity
import android.content.Context
import org.caojun.utils.ActivityUtils

/**
 * Created by CaoJun on 2017/9/26.
 */
open class BaseActivity : Activity() {

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        ActivityUtils.onRequestPermissionsResult(requestCode, permissions, grantResults)
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
}