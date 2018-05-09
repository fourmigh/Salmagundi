package org.caojun.smasher

import android.content.res.Resources

object Utils {

    private val DENSITY = Resources.getSystem().displayMetrics.density

    /**
     * dp转换px
     * @param dp   dp值
     * @return  转换后的px值
     */
    fun dp2Px(dp: Int): Int {
        return Math.round(dp * DENSITY)
    }
}