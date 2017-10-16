package org.caojun.morseman.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import org.caojun.morseman.R
import org.caojun.morseman.utils.MorseUtils

/**
 * Created by CaoJun on 2017/10/13.
 */
class MorseCodeAdapter: BaseAdapter {

    private var context: Context? = null
    private val data = ArrayList<Array<String>>()

    constructor(context: Context) : super() {
        this.context = context
        for (i in 26 until MorseUtils.CharMorse.size) {
            val value = arrayOf(MorseUtils.CharMorse[i].toString(), MorseUtils.toMorse(MorseUtils.CharMorse[i]))
            data.add(value)
        }
    }

    override fun getView(position: Int, convertView: View?, viewGrouop: ViewGroup?): View {
        var holder: ViewHolder
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_morsecode, null)
            holder = ViewHolder()
            holder.tvChar = view?.findViewById(R.id.tvChar)
            holder.tvCode = view?.findViewById(R.id.tvCode)
            view?.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        holder.tvChar?.text = data[position][0]
        holder.tvCode?.text = data[position][1]
        if (position % 2 == 0) {
            view?.setBackgroundColor(0x33333333)
        } else {
            view?.setBackgroundColor(0xffffff)
        }

        return view!!
    }

    override fun getItem(position: Int): Array<String> = data[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = data.size

    private inner class ViewHolder {
        internal var tvChar: TextView? = null
        internal var tvCode: TextView? = null
    }
}