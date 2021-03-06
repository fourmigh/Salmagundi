package org.caojun.library.moments.adapter

import android.app.Activity
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import org.caojun.library.moments.R
import org.caojun.library.moments.widget.SmoothImageView

/**
 * Created by CaoJun on 2017-12-25.
 */
class GalleryAdapter: PagerAdapter {
    private var activity: Activity? = null
    private var paths: ArrayList<String>? = null
    private var locationW = 0
    private var locationH = 0
    private var locationX = 0
    private var locationY = 0
    private var pos = 0
//    private var options: DisplayImageOptions? = null

    constructor(activity: Activity, paths: ArrayList<String>, w: Int, h: Int, x: Int, y: Int, pos: Int) {
        this.activity = activity
        setData(paths)
        this.locationH = h
        this.locationW = w
        this.locationX = x
        this.locationY = y
        this.pos = pos
//        options = DisplayImageOptions.Builder()
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .considerExifParams(true)
////                .bitmapConfig(Bitmap.Config.RGB_565)
//                .build()
//        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(activity));
    }

    fun setData(paths: ArrayList<String>) {
        this.paths = paths
    }

    override fun getCount(): Int {
        return paths?.size?:0
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val smoothImageView = LayoutInflater.from(activity).inflate(R.layout.item_pager, null) as SmoothImageView
        container.addView(smoothImageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        smoothImageView.setOriginalInfo(locationW, locationH, locationX, locationY)
        smoothImageView.transformIn()

        if (paths != null && position < count) {
            val path = paths!![position]
//            ImageLoader.getInstance().displayImage(path, smoothImageView, options)
            Glide.with(activity).load(path).into(smoothImageView)
        }


        smoothImageView.setOnTransformListener(object : SmoothImageView.TransformListener {
            override fun onTransformComplete(mode: Int) {
                if (mode == 2) {
                    activity?.finish()
                    activity?.overridePendingTransition(0, 0)
                }
            }
        })

        smoothImageView.setOnClickListener({ v ->
            if (position == pos) {
                (v as SmoothImageView).transformOut()
            } else {
                activity?.finish()
                activity?.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
        })

        return smoothImageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
}