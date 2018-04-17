package org.caojun.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.content.ContentResolver

/**
 * Created by CaoJun on 2017/9/5.
 */
object ActivityUtils {

    interface RequestPermissionListener {
        fun onSuccess()
        fun onFail()
    }

    private var requestPermissionListener: RequestPermissionListener? = null

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            requestPermissionListener?.onSuccess()
        } else {
            requestPermissionListener?.onFail()
        }
        requestPermissionListener = null
    }

    fun checkSelfPermission(activity: Activity, permission: String, listener: RequestPermissionListener): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissionListener = listener
            ActivityCompat.requestPermissions(activity, arrayOf(permission), 0)
            return false
        }
        listener.onSuccess()
        return true
    }

    fun startApplication(context: Context, packageName: String): Boolean {
        return try {
            val intent = context.packageManager.getLaunchIntentForPackage(packageName)
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun gotoMarket(context: Context, packageName: String) {
        val uri = Uri.parse("market://details?id=$packageName")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun gotoPermission(context: Context) {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }

    fun getIMEI(activity: Activity, listener: RequestPermissionListener) {
        checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE, listener)
    }

    fun call(context: Context, number: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun getSharedPreferences(context: Context, name: String, key: String, defValue: Boolean): Boolean {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE).getBoolean(key, defValue)
    }

    fun getSharedPreferences(context: Context, name: String, key: String, defValue: String): String {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE).getString(key, defValue)
    }

    fun getSharedPreferences(context: Context, name: String, key: String, defValue: Int): Int {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE).getInt(key, defValue)
    }

    private fun getShareIntent(): Intent {
        return Intent(Intent.ACTION_SEND)
    }

    private fun getShareText(): Intent {
        val intent = getShareIntent()
        intent.type = "text/plain"
        return intent
    }

    private fun getShareImage(): Intent {
        val intent = getShareIntent()
        intent.type = "image/jpeg"
        return intent
    }

    fun shareText(context: Context, title: String, msg: String) {
        val intent = getShareText()
        intent.putExtra(Intent.EXTRA_TEXT, msg)
        context.startActivity(Intent.createChooser(intent, title))
    }

    private fun getResourcesUri(context: Context, resId: Int): Uri {
        val resources = context.resources
        val path = ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + resources.getResourcePackageName(resId) + "/" + resources.getResourceTypeName(resId) + "/" + resources.getResourceEntryName(resId)
        return Uri.parse(path)
    }

    fun shareImage(context: Context, title: String, resId: Int) {
        val intent = getShareImage()
        val path = getResourcesUri(context, resId)
        intent.putExtra(Intent.EXTRA_STREAM, path)
        context.startActivity(Intent.createChooser(intent, title))
    }
}