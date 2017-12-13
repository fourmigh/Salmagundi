package org.caojun.dialog

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.widget.NumberPicker
import android.view.LayoutInflater
import org.caojun.widget.R
import android.os.Bundle




/**
 * Created by CaoJun on 2017-12-12.
 */
class NumberPickerDialog: AlertDialog, DialogInterface.OnClickListener, NumberPicker.OnValueChangeListener {

    private val MaxValue = "MaxValue"
    private val MinValue = "MinValue"
    private val CurrentValue = "CurrentValue"
    private var mNumberPicker: NumberPicker? = null
    private var callback: NumberPicker.OnValueChangeListener? = null

    private var newValue: Int = 0
    private var oldValue: Int = 0

    constructor(context: Context, callback: NumberPicker.OnValueChangeListener, maxValueNumber: Int, minValueNumber: Int, currentValueNumber: Int): super(context) {
        this.callback = callback

        setButton(DialogInterface.BUTTON_POSITIVE, context.getString(android.R.string.ok), this);
        setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(android.R.string.cancel), this);

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.dialog_number_picker, null)
        setView(view)
        mNumberPicker = view.findViewById(R.id.numberPicker)

        mNumberPicker?.maxValue = maxValueNumber
        mNumberPicker?.minValue = minValueNumber
        mNumberPicker?.value = currentValueNumber
        mNumberPicker?.setOnValueChangedListener(this)

        newValue = currentValueNumber
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        when (which) {
            DialogInterface.BUTTON_POSITIVE -> callback?.onValueChange(mNumberPicker, oldValue, newValue)
        }
    }

    override fun onValueChange(picker: NumberPicker?, oldValue: Int, newValue: Int) {
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    override fun onSaveInstanceState(): Bundle {
        val state = super.onSaveInstanceState()
        state.putInt(MaxValue, mNumberPicker?.maxValue?:0)
        state.putInt(MinValue, mNumberPicker?.minValue?:0)
        state.putInt(CurrentValue, mNumberPicker?.value?:0)
        return state
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        mNumberPicker?.maxValue = savedInstanceState.getInt(MaxValue)
        mNumberPicker?.minValue = savedInstanceState.getInt(MinValue)
        mNumberPicker?.value = savedInstanceState.getInt(CurrentValue)
    }
}