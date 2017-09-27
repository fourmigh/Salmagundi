package org.caojun.activity

import android.app.Activity

/**
 * Created by CaoJun on 2017/9/26.
 */
open class BaseActivity: Activity() {

    interface RequestPermissionListener {
        fun onRequestPermissionListener(requestCode: Int, permissions: Array<String>, grantResults: IntArray)
    }

    private var requestPermissionListener: RequestPermissionListener? = null

    fun setRequestPermissionListener(requestPermissionListener: RequestPermissionListener) {
        this.requestPermissionListener = requestPermissionListener
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        requestPermissionListener?.onRequestPermissionListener(requestCode, permissions, grantResults);
    }
}