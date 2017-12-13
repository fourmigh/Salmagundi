package org.caojun.widget

import android.content.Context
import android.content.DialogInterface
import android.preference.DialogPreference
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView

/**
 * Created by CaoJun on 2017-12-8.
 */
class SeekBarPreference: DialogPreference, DialogInterface.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private val ANDROIDNS = "http://schemas.android.com/apk/res/android"
    private var mSeekBar: SeekBar? = null
    private var mValueText: TextView? = null
    private var mContext: Context? = null
    private var mDefault = 24
    private var mMax = 24
    private var mValue = 24

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        mContext = context
        mDefault = attrs?.getAttributeIntValue(ANDROIDNS, "defaultValue", 24)?:24
        mMax = attrs?.getAttributeIntValue(ANDROIDNS, "max", 24)?:24
    }

    override fun onCreateDialogView(): View {
        val layout = LinearLayout(mContext)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(6, 6, 6, 6)

        mValueText = TextView(mContext)
        mValueText?.gravity = Gravity.CENTER_HORIZONTAL
        mValueText?.textSize = 32f
        val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        layout.addView(mValueText, params)

        mSeekBar = SeekBar(mContext)
        mSeekBar?.setOnSeekBarChangeListener(this)
        layout.addView(mSeekBar, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))

        if (shouldPersist()) {
            mValue = getPersistedInt(mDefault)
        }

        mSeekBar?.max = mMax
        mSeekBar?.progress = mValue
        return layout
    }

    override fun onBindDialogView(v: View) {
        super.onBindDialogView(v)
        mSeekBar?.max = mMax
        mSeekBar?.progress = mValue
    }

    override fun onSetInitialValue(restore: Boolean, defaultValue: Any) {
        super.onSetInitialValue(restore, defaultValue)
        mValue = if (restore) {
            if (shouldPersist()) getPersistedInt(mDefault) else 0
        } else {
            defaultValue as Int
        }
    }

    override fun onProgressChanged(seek: SeekBar, value: Int, fromTouch: Boolean) {
        mValueText?.text = value.toString()
        callChangeListener(value)
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {

    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {

    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        when (which) {
            DialogInterface.BUTTON_POSITIVE -> if (shouldPersist()) {
                persistInt(mSeekBar?.progress?:0)
            }
        }
        super.onClick(dialog, which)
    }
}