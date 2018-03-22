package org.caojun.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity

/**
 * Created by CaoJun on 2017/9/26.
 */
open class BaseAppCompatActivity : AppCompatActivity() {

    interface RequestPermissionListener {
        fun onSuccess()
        fun onFail()
    }

    private var requestPermissionListener: RequestPermissionListener? = null

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            requestPermissionListener?.onSuccess()
        } else {
            requestPermissionListener?.onFail()
        }
        requestPermissionListener = null
    }

    fun checkSelfPermission(permission: String, listener: RequestPermissionListener): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissionListener = listener
            ActivityCompat.requestPermissions(this, arrayOf(permission), 0)
            return false
        }
        listener.onSuccess()
        return true
    }

    fun getIMEI(listener: RequestPermissionListener) {
        checkSelfPermission(Manifest.permission.READ_PHONE_STATE, listener)
    }
}