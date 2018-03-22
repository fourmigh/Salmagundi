package org.caojun.library.moments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.widget.Toast
import org.caojun.library.moments.activity.MultiImageSelectorActivity
import java.util.ArrayList

/**
 * Created by CaoJun on 2017-12-20.
 */
class MultiImageSelector {
    companion object {
        val EXTRA_RESULT = MultiImageSelectorActivity.EXTRA_RESULT
        private var sSelector: MultiImageSelector? = null

        fun create(): MultiImageSelector {
            if (sSelector == null) {
                sSelector = MultiImageSelector()
            }
            return sSelector!!
        }
    }

    private var mShowCamera = true
    private var mMaxCount = 9
    private var mMode = MultiImageSelectorActivity.MODE_MULTI
    private var mOriginData: ArrayList<String>? = null

    constructor()

    fun showCamera(show: Boolean): MultiImageSelector {
        mShowCamera = show
        return create()
    }

    fun count(count: Int): MultiImageSelector {
        mMaxCount = count
        return create()
    }

    fun single(): MultiImageSelector {
        mMode = MultiImageSelectorActivity.MODE_SINGLE
        return create()
    }

    fun multi(): MultiImageSelector {
        mMode = MultiImageSelectorActivity.MODE_MULTI
        return create()
    }

    fun origin(images: ArrayList<String>): MultiImageSelector {
        mOriginData = images
        return create()
    }

    fun start(activity: Activity, requestCode: Int) {
        if (hasPermission(activity)) {
            activity.startActivityForResult(createIntent(activity), requestCode)
        } else {
            Toast.makeText(activity, R.string.mis_error_no_permission, Toast.LENGTH_SHORT).show()
        }
    }

    fun start(fragment: Fragment, requestCode: Int) {
        val context = fragment.context
        if (hasPermission(context)) {
            fragment.startActivityForResult(createIntent(context), requestCode)
        } else {
            Toast.makeText(context, R.string.mis_error_no_permission, Toast.LENGTH_SHORT).show()
        }
    }

    private fun hasPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            // Permission was added in API Level 16
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        } else true
    }

    private fun createIntent(context: Context): Intent {
        val intent = Intent(context, MultiImageSelectorActivity::class.java)
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, mShowCamera)
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, mMaxCount)
        if (mOriginData != null) {
            intent.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mOriginData)
        }
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, mMode)
        return intent
    }
}