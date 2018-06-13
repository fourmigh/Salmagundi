package org.caojun.imageview

import android.app.Activity
import org.caojun.imageview.activity.ImageActivity
import org.jetbrains.anko.startActivity

object ImageLoader {

    fun show(activity: Activity, url: String) {
        activity.startActivity<ImageActivity>(ImageActivity.Key_Url to url)
    }
}