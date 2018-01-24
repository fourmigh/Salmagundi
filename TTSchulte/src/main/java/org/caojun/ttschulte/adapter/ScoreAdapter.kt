package org.caojun.ttschulte.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import org.caojun.ttschulte.R
import org.caojun.ttschulte.room.Score
import org.caojun.utils.DeviceUtils
import org.caojun.utils.DigitUtils
import org.caojun.utils.TimeUtils

/**
 * Created by CaoJun on 2018-1-16.
 */
class ScoreAdapter : BaseAdapter {
    private var context: Context? = null
    private val list = ArrayList<Score>()
    private val imei: String

    constructor(context: Context, list: List<Score>?) : super() {
        this.context = context
        setData(list)
        imei = DeviceUtils.getImei(context)
    }

    fun setData(list: List<Score>?) {
        this.list.clear()
        if (list != null) {
            this.list.addAll(list)
        }
    }

    override fun getView(position: Int, convertView: View?, viewGrouop: ViewGroup?): View {
        var holder: ViewHolder
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_score, null)
            holder = ViewHolder()
            holder.tvTime = view?.findViewById(R.id.tvTime)
            holder.tvName = view?.findViewById(R.id.tvName)
            holder.tvScore = view?.findViewById(R.id.tvScore)
            holder.tvMe = view?.findViewById(R.id.tvMe)
            view?.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        val data = getItem(position)
        holder.tvTime?.text = TimeUtils.getTime("yyyy/MM/dd HH:mm:ss", data.time)
        holder.tvName?.text = data.name
        holder.tvScore?.text = context!!.getString(R.string.score_time, DigitUtils.getRound(data.score, 3))
        if (imei == data.imei) {
            holder.tvMe?.setText(data.name[0].toString())
            holder.tvMe?.visibility = View.VISIBLE
        } else {
            holder.tvMe?.visibility = View.INVISIBLE
        }
        if (position % 2 == 0) {
            view?.setBackgroundColor(0x33333333)
        } else {
            view?.setBackgroundColor(0xffffff)
        }

        return view!!
    }

    override fun getItem(position: Int): Score = list[position]

    override fun getItemId(position: Int): Long = list[position].id.toLong()

    override fun getCount(): Int = list.size

    private inner class ViewHolder {
        internal var tvTime: TextView? = null
        internal var tvName: TextView? = null
        internal var tvScore: TextView? = null
        internal var tvMe: TextView? = null
    }
}