package org.caojun.library.adapter

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import org.caojun.library.R
import org.caojun.library.bean.Image
import org.caojun.library.utils.ScreenUtils
import java.util.ArrayList
//import org.caojun.universalimageloader.core.DisplayImageOptions
//import org.caojun.universalimageloader.core.ImageLoaderConfiguration
//import org.caojun.universalimageloader.core.ImageLoader

/**
 * Created by CaoJun on 2017-12-20.
 */
class ImageGridAdapter: BaseAdapter {
    private val TYPE_CAMERA = 0
    private val TYPE_NORMAL = 1

    private var mContext: Context

//    private var mInflater: LayoutInflater
    private var showCamera = true
    private var showSelectIndicator = true

    private var mImages = ArrayList<Image?>()
    private val mSelectedImages = ArrayList<Image>()

//    internal var mGridWidth: Int

//    private var options: DisplayImageOptions? = null

    constructor(context: Context, showCamera: Boolean/*, column: Int*/) {
        mContext = context
//        mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        this.showCamera = showCamera
//        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
//        var width: Int
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
//            val size = Point()
//            wm.defaultDisplay.getSize(size)
//            width = size.x
//        } else {
//            width = wm.defaultDisplay.width
//        }
//        val point = ScreenUtils.getScreenSize(context)
//        val width = point.x
//        mGridWidth = width / column

//        options = DisplayImageOptions.Builder()
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .considerExifParams(true)
////                .bitmapConfig(Bitmap.Config.RGB_565)
//                .build()
//        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context))
    }

    /**
     * 显示选择指示器
     * @param b
     */
    fun showSelectIndicator(b: Boolean) {
        showSelectIndicator = b
    }

    fun setShowCamera(b: Boolean) {
        if (showCamera == b) return

        showCamera = b
        notifyDataSetChanged()
    }

    fun isShowCamera(): Boolean {
        return showCamera
    }

    /**
     * 选择某个图片，改变选择状态
     * @param image
     */
    fun select(image: Image) {
        if (mSelectedImages.contains(image)) {
            mSelectedImages.remove(image)
        } else {
            mSelectedImages.add(image)
        }
        notifyDataSetChanged()
    }

    /**
     * 通过图片路径设置默认选择
     * @param resultList
     */
    fun setDefaultSelected(resultList: ArrayList<String>) {
        for (path in resultList) {
            val image = getImageByPath(path)
            if (image != null) {
                mSelectedImages.add(image)
            }
        }
        if (mSelectedImages.size > 0) {
            notifyDataSetChanged()
        }
    }

    private fun getImageByPath(path: String): Image? {
        for (image in mImages) {
            if (image?.path.equals(path, ignoreCase = true)) {
                return image
            }
        }
        return null
    }

    /**
     * 设置数据集
     * @param images
     */
    fun setData(images: ArrayList<Image>?) {
        mSelectedImages.clear()

        mImages.clear()
        if (images != null && images.isNotEmpty()) {
            mImages.addAll(images)
        }
        notifyDataSetChanged()
    }

    override fun getViewTypeCount(): Int {
        return 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (showCamera) {
            if (position == 0) TYPE_CAMERA else TYPE_NORMAL
        } else TYPE_NORMAL
    }

    override fun getCount(): Int {
        return if (showCamera) mImages.size + 1 else mImages.size
    }

    override fun getItem(i: Int): Image? {
        return if (showCamera) {
            if (i == 0) {
                null
            } else mImages[i - 1]
        } else {
            mImages[i]
        }
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(i: Int, v: View?, viewGroup: ViewGroup): View? {
        var view = v
        if (isShowCamera()) {
            if (i == 0) {
                view = LayoutInflater.from(mContext).inflate(R.layout.mis_list_item_camera, viewGroup, false)
                return view
            }
        }

        val holder: ViewHolder?
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.mis_list_item_image, viewGroup, false)
            holder = ViewHolder(view)
        } else {
            holder = view.tag as ViewHolder
        }

        holder.bindData(getItem(i))

        return view
    }

    internal inner class ViewHolder(view: View) {
        var image: ImageView
        var indicator: ImageView
        var mask: View

        init {
            image = view.findViewById<View>(R.id.image) as ImageView
            indicator = view.findViewById(R.id.checkmark) as ImageView
            mask = view.findViewById(R.id.mask)
            view.tag = this
        }

        fun bindData(data: Image?) {
            if (data == null) return
            // 处理单选和多选状态
            if (showSelectIndicator) {
                indicator.visibility = View.VISIBLE
                if (mSelectedImages.contains(data)) {
                    // 设置选中状态
                    indicator.setImageResource(R.drawable.mis_btn_selected)
                    mask.visibility = View.VISIBLE
                } else {
                    // 未选择
                    indicator.setImageResource(R.drawable.mis_btn_unselected)
                    mask.visibility = View.GONE
                }
            } else {
                indicator.visibility = View.GONE
            }
//            val imageFile = File(data.path)
//            if (imageFile.exists()) {
//                // 显示图片
//                Picasso.with(mContext)
//                        .load(imageFile)
//                        .placeholder(R.drawable.mis_default_error)
//                        .tag(MultiImageSelectorFragment.TAG)
//                        .resize(mGridWidth, mGridWidth)
//                        .centerCrop()
//                        .into(image)
//            } else {
//                image.setImageResource(R.drawable.mis_default_error)
//            }
//            ImageLoader.getInstance().displayImage(data.path, image, options)
            Glide.with(mContext).load(data.path).into(image)
        }
    }
}