package org.caojun.yujiyizidi.utils

import org.caojun.yujiyizidi.bean.Express
import java.util.Comparator;

/**
 * Created by CaoJun on 2017/9/4.
 */
class ExpressSortComparator : Comparator<Express> {
    override fun compare(p0: Express, p1: Express): Int {
        val c0 = p0.getSortString()[0]
        val c1 = p1.getSortString()[0]
        return c0 - c1
    }
}