package org.caojun.library.moments.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import kotlinx.android.synthetic.main.activity_gallery.*
import org.caojun.library.moments.R
import org.caojun.library.moments.adapter.GalleryAdapter

/**
 * Created by CaoJun on 2017-12-25.
 */
class GalleryActivity : AppCompatActivity() {
    companion object {
        val PHOTO_SOURCE_ID = "PHOTO_SOURCE_ID"
        val PHOTO_SELECT_POSITION = "PHOTO_SELECT_POSITION"
        val PHOTO_SELECT_X_TAG = "PHOTO_SELECT_X_TAG"
        val PHOTO_SELECT_Y_TAG = "PHOTO_SELECT_Y_TAG"
        val PHOTO_SELECT_W_TAG = "PHOTO_SELECT_W_TAG"
        val PHOTO_SELECT_H_TAG = "PHOTO_SELECT_H_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        val title = intent.getStringExtra(MomentsActivity.Key_Title)
        if (!TextUtils.isEmpty(title)) {
            this.title = title
        }

        val paths = intent.getStringArrayListExtra(PHOTO_SOURCE_ID)
        val position = intent.getIntExtra(PHOTO_SELECT_POSITION, 0)
        val locationX = intent.getIntExtra(PHOTO_SELECT_X_TAG, 0)
        val locationY = intent.getIntExtra(PHOTO_SELECT_Y_TAG, 0)
        val locationW = intent.getIntExtra(PHOTO_SELECT_W_TAG, 0)
        val locationH = intent.getIntExtra(PHOTO_SELECT_H_TAG, 0)

        viewPager.adapter = GalleryAdapter(this, paths, locationW, locationH, locationX, locationY, position)
        viewPager.currentItem = position
    }
}