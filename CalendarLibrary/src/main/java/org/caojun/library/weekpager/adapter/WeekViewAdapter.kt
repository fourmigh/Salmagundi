package org.caojun.library.weekpager.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import org.caojun.library.R
import org.caojun.library.model.CalendarDay
import org.caojun.library.util.DayUtils
import org.caojun.library.weekpager.listener.OnDayClickListener
import org.caojun.library.weekpager.view.WeekDayViewPager
import org.caojun.library.weekpager.view.WeekView
import java.util.ArrayList

/**
 * Created by CaoJun on 2017/8/23.
 */
class WeekViewAdapter: RecyclerView.Adapter<WeekViewAdapter.WeekViewHolder>, OnDayClickListener {

    class WeekViewHolder(view: View, firstShowDay: CalendarDay, viewPager: WeekDayViewPager, mAbleCalendayDays: ArrayList<CalendarDay>, weekViewAdapter: WeekViewAdapter) : RecyclerView.ViewHolder(view) {

        private var mWeekView: WeekView

        init {
            mWeekView = view as WeekView
            mWeekView.setDays(firstShowDay)
            mWeekView.setOnDayClickListener(viewPager)
            mWeekView.setOnAdapterDayClickListener(weekViewAdapter)
            mWeekView.setTextSize(view.getContext().resources.getDimension(R.dimen.si_default_text_size))
            mWeekView.setAbleDates(mAbleCalendayDays)
        }

        fun bind(position: Int, selectPostion: Int) {
            mWeekView.setPosition(position)
            mWeekView.setSelectPostion(selectPostion)
        }

    }

    private var mContext: Context? = null
    private var mStartDay: CalendarDay? = null
    private var mEndDay: CalendarDay? = null
    private var mFirstShowDay: CalendarDay? = null
    private var mWeekDayViewPager: WeekDayViewPager? = null
    private var mSelectPosition: Int = 0
    private val mAbleCalendayDays: ArrayList<CalendarDay> = ArrayList();

    /**
     * week view value
     */
    private var mTextNormalColor: Int = 0
    private var mTextSelectColor: Int = 0
    private var mTextUnableColor: Int = 0
    private var indicatorColor: Int = 0

    constructor(context: Context, viewPager: WeekDayViewPager) {
        mContext = context
        mWeekDayViewPager = viewPager
//        mAbleCalendayDays = ArrayList()
        updateColor()
    }

    private fun updateColor() {
        indicatorColor = ContextCompat.getColor(mContext, R.color.color_18ffff)
        mTextSelectColor = ContextCompat.getColor(mContext, android.R.color.white)
        mTextNormalColor = ContextCompat.getColor(mContext, R.color.text_color_normal)
        mTextUnableColor = ContextCompat.getColor(mContext, R.color.text_color_light)
    }

    fun setData(startDay: CalendarDay, endDay: CalendarDay, calendarDayArrayList: ArrayList<CalendarDay>?) {
        mStartDay = startDay
        mEndDay = endDay
        mFirstShowDay = DayUtils.calculateFirstShowDay(mStartDay!!)
        if (calendarDayArrayList != null) {
            mAbleCalendayDays.clear()
            mAbleCalendayDays.addAll(calendarDayArrayList)
        }
        notifyDataSetChanged()
    }

    fun getFirstShowDay(): CalendarDay? {
        return mFirstShowDay
    }

    override fun getItemCount(): Int {
        return if (mStartDay == null || mEndDay == null) {
            0
        } else DayUtils.calculateWeekCount(mStartDay!!, mEndDay!!)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): WeekViewHolder {
        val weekView = WeekView(mContext!!, mTextNormalColor, mTextSelectColor, mTextUnableColor, indicatorColor)
        val width = mContext!!.resources.displayMetrics.widthPixels
        val params = LinearLayout.LayoutParams(width,
                ViewGroup.LayoutParams.MATCH_PARENT)
        weekView.layoutParams = params
        return WeekViewHolder(weekView, mFirstShowDay!!, mWeekDayViewPager!!, mAbleCalendayDays, this)
    }

    override fun onBindViewHolder(viewHolder: WeekViewHolder, position: Int) {
        viewHolder.bind(position, mSelectPosition)
    }

    override fun onDayClick(simpleMonthView: WeekView, calendarDay: CalendarDay,
                   position: Int) {
        mSelectPosition = position
        notifyDataSetChanged()
    }

    fun setTextNormalColor(mTextNormalColor: Int) {
        this.mTextNormalColor = mTextNormalColor
    }

    fun setTextSelectColor(mTextSelectColor: Int) {
        this.mTextSelectColor = mTextSelectColor
    }

    fun setTextUnableColor(mTextUnableColor: Int) {
        this.mTextUnableColor = mTextUnableColor
    }

    fun setIndicatorColor(indicatorColor: Int) {
        this.indicatorColor = indicatorColor
    }
}