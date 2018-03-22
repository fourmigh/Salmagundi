package org.caojun.signman.adapter

import android.app.Activity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.ToggleButton
import kotlinx.android.synthetic.main.activity_main.*
import org.caojun.signman.R
import org.caojun.signman.activity.MainActivity
import org.caojun.signman.listener.OnSignListener
import org.caojun.signman.room.App
import org.caojun.signman.room.AppDatabase
import org.caojun.signman.utils.ActivityUtils
import org.caojun.signman.utils.TimeUtils
import org.jetbrains.anko.doAsync

/**
 * Created by CaoJun on 2017/8/31.
 */
class AppAdapter: BaseAdapter {

    private var activity: MainActivity? = null
    private var listener: OnSignListener? = null

    constructor(context: MainActivity, list: ArrayList<App>, listener: OnSignListener?) : super(context, list) {
        activity = context
        this.listener = listener
    }

    override fun getView(position: Int, convertView: View?, viewGrouop: ViewGroup?): View {
        var holder: ViewHolder
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_app, null)
            holder = ViewHolder()
            holder.tvName = view.findViewById(R.id.tvName)
            holder.tbSign = view.findViewById(R.id.tbSign) as ToggleButton
            holder.btnSign = view.findViewById(R.id.btnSign)
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        val icon_size: Int = context?.resources?.getDimension(R.dimen.icon_size)?.toInt()?:128
        val icon_padding: Int = context?.resources?.getDimension(R.dimen.app_icon_padding)?.toInt()?:10

        val app = getItem(position)
        app.icon?.setBounds(0, 0, icon_size, icon_size)

        holder.tvName?.text = app.name
        holder.tvName?.compoundDrawablePadding = icon_padding
        holder.tvName?.gravity = Gravity.CENTER_VERTICAL
        holder.tvName?.setCompoundDrawables(app.icon, null, null, null)

        holder.tbSign?.isChecked = app.isSigned
        if (app.isSigned) {
            holder.btnSign?.visibility = View.GONE
        } else {
            holder.btnSign?.visibility = View.VISIBLE
        }
        if (app.time.size < 1) {
            holder.tbSign?.visibility = View.GONE
        } else {
            val time = app.getLastTime()
            if (TimeUtils.isToday(time)) {
                holder.tbSign?.visibility = View.VISIBLE
            } else {
                holder.tbSign?.visibility = View.GONE
            }
        }

        holder.btnSign?.setOnClickListener({
            //启动应用
            if (!ActivityUtils.startActivity(context!!, app.packageName!!)) {
                activity?.doUninstalledAlert(app)
                return@setOnClickListener
            }
            (context as MainActivity).progressBar?.visibility = View.VISIBLE
            //添加时间
            app.addTime()
            app.isSigned = true
            doAsync {
                AppDatabase.getDatabase(context!!).getAppDao().update(app)
            }
            listener?.onSignChange()
        })
        holder.tbSign?.setOnCheckedChangeListener { toggleButton, checked ->
            if (toggleButton.isPressed) {
                if (checked) {
                    holder.btnSign?.visibility = View.GONE
                } else {
                    holder.btnSign?.visibility = View.VISIBLE
                }
                app.isSigned = checked
                if (app.isSigned) {
                    app.addTime()
                }
                doAsync {
                    AppDatabase.getDatabase(context!!).getAppDao().update(app)
                }
                listener?.onSignChange()
            }
        }
        return view!!
    }

    private inner class ViewHolder {
        internal var tvName: TextView? = null
        internal var tbSign: ToggleButton? = null
        internal var btnSign: Button? = null
    }
}