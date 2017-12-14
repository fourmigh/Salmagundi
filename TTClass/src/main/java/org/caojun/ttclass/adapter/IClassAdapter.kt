package org.caojun.ttclass.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import org.caojun.ttclass.R
import org.caojun.ttclass.room.IClass

/**
 * Created by CaoJun on 2017-12-12.
 */
class IClassAdapter : BaseAdapter {
    private var context: Context? = null
    private val list = ArrayList<IClass>()

    constructor(context: Context, list: List<IClass>) : super() {
        this.context = context
        setData(list)
    }

    fun setData(list: List<IClass>) {
        this.list.clear()
        this.list.addAll(list)
    }

    override fun getView(position: Int, convertView: View?, viewGrouop: ViewGroup?): View {
        var holder: ViewHolder
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_class, null)
            holder = ViewHolder()
            holder.tvClassName = view?.findViewById(R.id.tvClassName)
            holder.tvGradeRemark = view?.findViewById(R.id.tvGradeRemark)
            holder.tvRemainder = view?.findViewById(R.id.tvRemainder)
            view?.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        val data = getItem(position)
        holder.tvClassName?.text = data.name
        holder.tvGradeRemark?.text = data.grade
        holder.tvRemainder?.text = data.reminder.toString()
        if (position % 2 == 0) {
            view?.setBackgroundColor(0x33333333)
        } else {
            view?.setBackgroundColor(0xffffff)
        }

        return view!!
    }

    override fun getItem(position: Int): IClass = list[position]

    override fun getItemId(position: Int): Long = list[position].id.toLong()

    override fun getCount(): Int = list.size

    private inner class ViewHolder {
        internal var tvClassName: TextView? = null
        internal var tvGradeRemark: TextView? = null
        internal var tvRemainder: TextView? = null
    }
}