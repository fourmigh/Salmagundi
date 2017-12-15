package org.caojun.ttclass.room

import android.arch.persistence.room.Ignore
import android.os.Parcel
import android.os.Parcelable
import org.caojun.ttclass.R

/**
 * Created by CaoJun on 2017-12-12.
 */
class Schedule: Parcelable {

    private val TimeSunday = arrayOf("", "")
    private val TimeMonday = arrayOf("", "")
    private val TimeTuesday = arrayOf("", "")
    private val TimeWednesday = arrayOf("", "")
    private val TimeThursday = arrayOf("", "")
    private val TimeFriday = arrayOf("", "")
    private val TimeSaturday = arrayOf("", "")
    val time = arrayOf(TimeSunday, TimeMonday, TimeTuesday, TimeWednesday, TimeThursday, TimeFriday, TimeSaturday)
    val checked = BooleanArray(time.size)

    constructor()
    constructor(_in: Parcel): this() {
        _in.readStringArray(TimeSunday)
        _in.readStringArray(TimeMonday)
        _in.readStringArray(TimeTuesday)
        _in.readStringArray(TimeWednesday)
        _in.readStringArray(TimeThursday)
        _in.readStringArray(TimeFriday)
        _in.readStringArray(TimeSaturday)
        _in.readBooleanArray(checked)
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeStringArray(TimeSunday)
        dest.writeStringArray(TimeMonday)
        dest.writeStringArray(TimeTuesday)
        dest.writeStringArray(TimeWednesday)
        dest.writeStringArray(TimeThursday)
        dest.writeStringArray(TimeFriday)
        dest.writeStringArray(TimeSaturday)
        dest.writeBooleanArray(checked)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        @Ignore
        val CREATOR: Parcelable.Creator<Schedule> = object : Parcelable.Creator<Schedule> {
            override fun createFromParcel(_in: Parcel): Schedule {
                return Schedule(_in)
            }

            override fun newArray(size: Int): Array<Schedule?> {
                return arrayOfNulls(size)
            }
        }
    }
}