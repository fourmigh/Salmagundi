package org.caojun.library.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import org.caojun.library.R
import org.caojun.library.bean.Folder
import java.io.File
import java.util.ArrayList

/**
 * Created by CaoJun on 2017-12-20.
 */
class FolderAdapter : BaseAdapter {
    private var mContext: Context
    private var mInflater: LayoutInflater

    private var mFolders = ArrayList<Folder>()

    private var mImageSize = 0

    private var lastSelected = 0

    constructor(context: Context) {
        mContext = context
        mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mImageSize = mContext.resources.getDimensionPixelOffset(R.dimen.mis_folder_cover_size)
    }

    /**
     * 设置数据集
     * @param folders
     */
    fun setData(folders: List<Folder>?) {
        mFolders.clear()
        if (folders != null && folders.isNotEmpty()) {
            mFolders.addAll(folders)
        }
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return mFolders.size + 1
    }

    override fun getItem(i: Int): Folder? {
        return if (i == 0) null else mFolders[i - 1]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(i: Int, v: View?, viewGroup: ViewGroup): View? {
        var view = v
        val holder: ViewHolder?
        if (view == null) {
            view = mInflater.inflate(R.layout.mis_list_item_folder, viewGroup, false)
            holder = ViewHolder(view)
        } else {
            holder = view.tag as ViewHolder
        }
        if (i == 0) {
            holder.name.setText(R.string.mis_folder_all)
            holder.path.text = "/sdcard"
            holder.size.text = String.format("%d%s",
                    getTotalImageSize(), mContext.resources.getString(R.string.mis_photo_unit))
            if (mFolders.size > 0) {
                Picasso.with(mContext)
                        .load(File(mFolders[0].cover!!.path))
                        .error(R.drawable.mis_default_error)
                        .resizeDimen(R.dimen.mis_folder_cover_size, R.dimen.mis_folder_cover_size)
                        .centerCrop()
                        .into(holder.cover)
            }
        } else {
            holder.bindData(getItem(i))
        }
        holder.indicator.visibility = if (lastSelected == i) View.VISIBLE else View.INVISIBLE
        return view
    }

    private fun getTotalImageSize(): Int {
        var result = 0
        for (f in mFolders) {
            result += f.images.size
        }
        return result
    }

    fun setSelectIndex(i: Int) {
        if (lastSelected == i) return

        lastSelected = i
        notifyDataSetChanged()
    }

    fun getSelectIndex(): Int {
        return lastSelected
    }

    internal inner class ViewHolder(view: View) {
        var cover: ImageView
        var name: TextView
        var path: TextView
        var size: TextView
        var indicator: ImageView

        init {
            cover = view.findViewById(R.id.cover) as ImageView
            name = view.findViewById(R.id.name) as TextView
            path = view.findViewById(R.id.path) as TextView
            size = view.findViewById(R.id.size) as TextView
            indicator = view.findViewById(R.id.indicator) as ImageView
            view.tag = this
        }

        fun bindData(data: Folder?) {
            if (data == null) {
                return
            }
            name.text = data.name
            path.text = data.path
            if (data.images != null) {
                size.text = String.format("%d%s", data.images.size, mContext.resources.getString(R.string.mis_photo_unit))
            } else {
                size.text = "*" + mContext.resources.getString(R.string.mis_photo_unit)
            }
            if (data.cover != null) {
                // 显示图片
                Picasso.with(mContext)
                        .load(File(data.cover!!.path))
                        .placeholder(R.drawable.mis_default_error)
                        .resizeDimen(R.dimen.mis_folder_cover_size, R.dimen.mis_folder_cover_size)
                        .centerCrop()
                        .into(cover)
            } else {
                cover.setImageResource(R.drawable.mis_default_error)
            }
        }
    }
}