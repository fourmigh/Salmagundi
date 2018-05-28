package org.caojun.contacts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter
import java.util.*

class ContactAdapter: BaseAdapter, StickyListHeadersAdapter {

    private var mList: List<Contact> = ArrayList()
    private val mSelectedList = ArrayList<Contact>()
    private val mContext: Context

    constructor(mContext: Context, list: List<Contact>?) {
        this.mContext = mContext
        if (list == null) {
            this.mList = ArrayList()
        } else {
            this.mList = list
        }
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     * @param list
     */
    fun update(list: List<Contact>?) {
        if (list == null) {
            this.mList = ArrayList()
        } else {
            this.mList = list
        }
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var viewHolder: ViewHolder?
        val mContent = mList[position]
        var view = convertView
        if (view == null) {
            viewHolder = ViewHolder()
            view = LayoutInflater.from(mContext).inflate(R.layout.item_contact, null)
            viewHolder.tvTitle = view.findViewById(R.id.title)
            viewHolder.tvNumber = view.findViewById(R.id.number)
            viewHolder.cbChecked = view.findViewById(R.id.cbChecked)
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        viewHolder.tvTitle?.text = this.mList[position].getTitle()
        viewHolder.tvNumber?.text = this.mList[position].getContent()
        viewHolder.cbChecked?.isChecked = isSelected(mContent)

        return view!!
    }

    override fun getItem(position: Int): Any {
        return mList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return this.mList.size
    }

    class ViewHolder {
        var tvTitle: TextView? = null
        var tvNumber: TextView? = null
        var cbChecked: CheckBox? = null
    }

    private fun isSelected(contact: Contact): Boolean {
        return mSelectedList.contains(contact)
    }

    fun toggleChecked(position: Int) {
        if (isSelected(mList[position])) {
            removeSelected(position)
        } else {
            setSelected(position)
        }

    }

    private fun setSelected(position: Int) {
        if (!mSelectedList.contains(mList[position])) {
            mSelectedList.add(mList[position])
        }
    }

    private fun removeSelected(position: Int) {
        if (mSelectedList.contains(mList[position])) {
            mSelectedList.remove(mList[position])
        }
    }

    fun getPositionForSection(c: Char): Int {
        for (i in 0 until count) {
            val letter = mList[i].getSortLetter()
            if (letter == c) {
                return i
            }
        }

        return -1
    }

    override fun getHeaderId(position: Int): Long {
        return mList[position].getSortLetter().toLong()
    }

    override fun getHeaderView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: HeaderViewHolder
        var view = convertView
        if (view == null) {
            holder = HeaderViewHolder()
            view = LayoutInflater.from(mContext).inflate(R.layout.item_contact_header, parent, false)
            holder.tvHeader = view.findViewById<View>(R.id.tvHeader) as TextView
            view?.tag = holder
        } else {
            holder = view.tag as HeaderViewHolder
        }
        val header = this.mList[position].getSortLetter()
        holder.tvHeader?.text = header.toString()
        return view!!
    }

    private inner class HeaderViewHolder {
        internal var tvHeader: TextView? = null
    }
}