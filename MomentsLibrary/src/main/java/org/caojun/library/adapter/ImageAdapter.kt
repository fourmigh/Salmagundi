package org.caojun.library.adapter

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.nostra13.universalimageloader.core.ImageLoader
import org.caojun.library.MultiImageSelectorActivity
import org.caojun.library.R
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import org.caojun.library.activity.GalleryActivity

/**
 * Created by CaoJun on 2017-12-20.
 */
class ImageAdapter: RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private var paths: ArrayList<String>? = null
    private var mLayoutInflater: LayoutInflater? = null
    private var activity: Activity? = null
    private val PlusPath = "drawable://" + R.drawable.mine_btn_plus
    private var options: DisplayImageOptions? = null

    constructor(activity: Activity?, paths: ArrayList<String>) : super() {
        this.activity = activity
        this.mLayoutInflater = LayoutInflater.from(activity)
        setData(paths)

        options = DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
//                .bitmapConfig(Bitmap.Config.RGB_565)
                .build()
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(activity));
    }

    fun setData(paths: ArrayList<String>) {
        this.paths = paths
    }

    fun getData(): ArrayList<String>? {
        return paths
    }

    override fun getItemCount(): Int {
        return if (paths == null) 1 else (paths!!.size + 1)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        if (position >= MultiImageSelectorActivity.DEFAULT_IMAGE_SIZE) {//图片已选完时，隐藏添加按钮
            holder?.imageView?.visibility = View.GONE
            return
        } else {
            holder?.imageView?.visibility = View.VISIBLE
        }
        var path = PlusPath
        if (paths != null) {
            val size = if (paths == null) 0 else paths!!.size
            if (position < size) {
                path = "file://" + paths!![position]
                holder?.imageView?.setOnClickListener({ v ->
                    val location = IntArray(2)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        val frame = Rect()
                        activity!!.window.decorView.getWindowVisibleDisplayFrame(frame)
                        val statusBarHeight = frame.top
                        v.getLocationOnScreen(location)
                        location[1] += statusBarHeight
                    } else {
                        v.getLocationOnScreen(location)
                    }
                    v.invalidate()
                    val width = v.width
                    val height = v.height

                    val intent = Intent(activity, GalleryActivity::class.java)
                    intent.putExtra(GalleryActivity.PHOTO_SOURCE_ID, paths)
                    intent.putExtra(GalleryActivity.PHOTO_SELECT_POSITION, position)
                    intent.putExtra(GalleryActivity.PHOTO_SELECT_X_TAG, location[0])
                    intent.putExtra(GalleryActivity.PHOTO_SELECT_Y_TAG, location[1])
                    intent.putExtra(GalleryActivity.PHOTO_SELECT_W_TAG, width)
                    intent.putExtra(GalleryActivity.PHOTO_SELECT_H_TAG, height)
                    activity?.startActivity(intent)
                    activity?.overridePendingTransition(0, 0)
                })
            }
        }
        ImageLoader.getInstance().displayImage(path, holder?.imageView, options)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(mLayoutInflater?.inflate(R.layout.item_image, parent, false))
    }

    inner class ViewHolder: RecyclerView.ViewHolder {
        internal var imageView: ImageView? = null

        constructor(itemView: View?) : super(itemView) {
            imageView = itemView?.findViewById(R.id.imageView)
        }
    }
}