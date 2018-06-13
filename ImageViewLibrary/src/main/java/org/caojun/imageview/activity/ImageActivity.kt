package org.caojun.imageview.activity

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.widget.ImageView
import org.caojun.imageview.R

class ImageActivity: Activity() {

    companion object {
        val Key_Url = "Key_Url"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        val url = intent.getStringExtra(Key_Url)
        if (TextUtils.isEmpty(url)) {
            return
        }

        val imageView = findViewById<ImageView>(R.id.imageView)
    }
}