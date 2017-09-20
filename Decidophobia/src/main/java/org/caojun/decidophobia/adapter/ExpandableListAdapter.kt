package org.caojun.decidophobia.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import org.caojun.decidophobia.R
import org.caojun.decidophobia.ormlite.Options

/**
 * Created by CaoJun on 2017/9/20.
 */
class ExpandableListAdapter: BaseExpandableListAdapter {

    private var context: Context? = null
    private var list: List<Options> = ArrayList()

    constructor(context: Context, list: List<Options>) : super() {
        this.context = context
        setData(list)
    }

    fun setData(list: List<Options>) {
        this.list = list
    }

    private inner class ViewHolderTitle {
        internal var tvTitle: TextView? = null
    }

    private inner class ViewHolderChoice {
        internal var tvChoice: TextView? = null
    }

    override fun getGroup(groupPosition: Int): Any {
        return list[groupPosition].title
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return false
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        var holder: ViewHolderTitle
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_title, null)
            holder = ViewHolderTitle()
            holder.tvTitle = view?.findViewById(R.id.tvTitle)
            view?.setTag(holder)
        } else {
            holder = view.getTag() as ViewHolderTitle
        }

        holder.tvTitle?.text = list[groupPosition].title

        return view!!
    }

    override fun getChildrenCount(p0: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getChild(p0: Int, p1: Int): Any {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getGroupId(p0: Int): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getChildView(p0: Int, p1: Int, p2: Boolean, p3: View?, p4: ViewGroup?): View {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getChildId(p0: Int, p1: Int): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getGroupCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}