package org.caojun.library.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import org.caojun.library.MultiImageSelectorActivity
import org.caojun.library.R

/**
 * Created by CaoJun on 2017-12-20.
 */
class ImageAdapter: RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private var paths: ArrayList<String>? = null
    private var mLayoutInflater: LayoutInflater? = null
    private var context: Context? = null

    constructor(context: Context?, paths: ArrayList<String>) : super() {
        this.context = context
        this.mLayoutInflater = LayoutInflater.from(context)
        setData(paths)
    }

    private fun setData(paths: ArrayList<String>) {
//        this.paths.clear()
//        this.paths.addAll(paths)
        this.paths = paths
    }

    override fun getItemCount(): Int {
        return paths?.size?:0
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        if (position >= MultiImageSelectorActivity.DEFAULT_IMAGE_SIZE) {//图片已选完时，隐藏添加按钮
            holder?.imageView?.visibility = View.GONE
        } else {
            holder?.imageView?.visibility = View.VISIBLE
        }
        Glide.with(context).load(paths!![position]).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder?.imageView)
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