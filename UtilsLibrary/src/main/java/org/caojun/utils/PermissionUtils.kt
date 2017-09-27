package org.caojun.utils

import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.os.Build
import org.caojun.activity.BaseActivity
import android.widget.Toast
import android.support.annotation.NonNull
import org.caojun.activity.BaseActivity.RequestPermissionListener




/**
 * Created by CaoJun on 2017/9/26.
 */
object PermissionUtils {

    fun checkSelfPermission(activity: BaseActivity, permission: String, requestCode: Int): Boolean {
        var grant = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                activity.setRequestPermissionListener(object : BaseActivity.RequestPermissionListener {
                    override fun onRequestPermissionListener(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
                        grant = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    }
                })
                ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
            } else {
                grant = true
            }
        } else {
            grant = true
        }
        return grant
    }
}