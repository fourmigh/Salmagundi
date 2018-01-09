package org.caojun.ttshulte.utils

import android.view.ViewGroup
import android.widget.Button

/**
 * Created by CaoJun on 2018-1-5.
 */
object ViewUtils {
    fun findButtons(viewGroup: ViewGroup, list: ArrayList<Button>) {
        (0 until viewGroup.childCount)
                .map { viewGroup.getChildAt(it) }
                .forEach {
                    if (it is ViewGroup) {
                        findButtons(it, list)
                    } else if (it is Button) {
                        list.add(it)
                    }
                }
    }
}