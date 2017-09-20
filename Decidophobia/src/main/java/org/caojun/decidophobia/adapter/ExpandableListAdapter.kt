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
    private var list: List<Options>? = null

    constructor(context: Context, list: List<Options>?) : super() {
        this.context = context
        setData(list)
    }

    fun setData(list: List<Options>?) {
        if (list == null || list.isEmpty()) {
            this.list = ArrayList()
        } else {
            this.list = list
        }
    }

    private inner class ViewHolderTitle {
        internal var tvTitle: TextView? = null
    }

    private inner class ViewHolderChoice {
        internal var tvChoice: TextView? = null
    }

    override fun getGroup(groupPosition: Int): Any {
        return list!![groupPosition].title
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
            view?.tag = holder
        } else {
            holder = view.tag as ViewHolderTitle
        }

        holder.tvTitle?.text = list!![groupPosition].title

        return view!!
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return list!![groupPosition].option.size
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return list!![groupPosition].option[childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        var holder: ViewHolderChoice
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_choice, null)
            holder = ViewHolderChoice()
            holder.tvChoice = view?.findViewById(R.id.tvChoice)
            view?.tag = holder
        } else {
            holder = view.tag as ViewHolderChoice
        }

        holder.tvChoice?.text = list!![groupPosition].option[childPosition]

        return view!!
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return list?.size?:0
    }
}