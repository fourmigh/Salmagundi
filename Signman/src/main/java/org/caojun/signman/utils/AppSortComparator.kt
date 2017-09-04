package org.caojun.signman.utils

import org.caojun.signman.room.App
import java.util.Comparator;

/**
 * Created by CaoJun on 2017/9/4.
 */
class AppSortComparator : Comparator<App> {
    override fun compare(p0: App?, p1: App?): Int {
        val c0 = p0?.getSortString()
        val c1 = p1?.getSortString()
        return (c0?:'0') - (c1?:'0')
    }
}