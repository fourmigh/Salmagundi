package org.caojun.ttclass.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import org.caojun.ttclass.R
import org.caojun.ttclass.room.School
import org.caojun.ttclass.room.Teacher

/**
 * Created by CaoJun on 2017-12-12.
 */
class SchoolAdapter : BaseAdapter {
    private var context: Context? = null
    private val schools = ArrayList<School>()
    private val teachers = ArrayList<Teacher?>()

    constructor(context: Context, schools: List<School>, teachers: List<Teacher?>) : super() {
        this.context = context
        setData(schools, teachers)
    }

    fun setData(schools: List<School>, teachers: List<Teacher?>) {
        this.schools.clear()
        this.schools.addAll(schools)

        this.teachers.clear()
        this.teachers.addAll(teachers)
    }

    override fun getView(position: Int, convertView: View?, viewGrouop: ViewGroup?): View {
        var holder: ViewHolder
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_school, null)
            holder = ViewHolder()
            holder.tvName = view?.findViewById(R.id.tvName)
            holder.tvAddress = view?.findViewById(R.id.tvAddress)
            view?.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        val data = getItem(position)
        if (teachers[position] != null) {
            holder.tvName?.text = teachers[position]?.name
        } else {
            holder.tvName?.text = data.name
        }
        holder.tvAddress?.text = data.address
        if (position % 2 == 0) {
            view?.setBackgroundColor(0x33333333)
        } else {
            view?.setBackgroundColor(0xffffff)
        }

        return view!!
    }

    override fun getItem(position: Int): School = schools[position]

    override fun getItemId(position: Int): Long = schools[position].id.toLong()

    override fun getCount(): Int = schools.size

    private inner class ViewHolder {
        internal var tvName: TextView? = null
        internal var tvAddress: TextView? = null
    }
}