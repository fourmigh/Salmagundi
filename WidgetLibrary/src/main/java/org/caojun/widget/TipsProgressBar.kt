package org.caojun.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView

/**
 * Created by CaoJun on 2017/9/6.
 */
class TipsProgressBar: LinearLayout {
    private var tvTips: TextView? = null

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize(attrs)
    }

    override fun setTooltipText(tooltipText: CharSequence?) {
        tvTips?.text = tooltipText
    }

    private fun initialize(attrs: AttributeSet?) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_tips_progressbar, this)
        tvTips = findViewById(R.id.tvTips)

        val count = attrs?.attributeCount?:0
        for (i in 0..(count - 1)) {
            if (attrs?.getAttributeName(i).equals("tooltipText")) {
                val value = attrs?.getAttributeValue(i)
                if (!TextUtils.isEmpty(value)) {
                    if (value!![0] == '@') {
                        try {
                            val resId = Integer.parseInt(value.substring(1))
                            tvTips?.text = context.getString(resId)
                        } catch (e: Exception) {
                        }
                    } else {
                        tvTips?.text = value
                    }
                }
                break
            }
        }
    }
}