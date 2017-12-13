package org.caojun.ttclass.room

import org.caojun.ttclass.R

/**
 * Created by CaoJun on 2017-12-12.
 */
class Schedule {

    private val TimeSunday = arrayOf("", "")
    private val TimeMonday = arrayOf("", "")
    private val TimeTuesday = arrayOf("", "")
    private val TimeWednesday = arrayOf("", "")
    private val TimeThursday = arrayOf("", "")
    private val TimeFriday = arrayOf("", "")
    private val TimeSaturday = arrayOf("", "")
    val time = arrayOf(TimeSunday, TimeMonday, TimeTuesday, TimeWednesday, TimeThursday, TimeFriday, TimeSaturday)
    val checked = arrayOf(false, false, false, false, false, false, false)
    var idClass: Long = -1
}