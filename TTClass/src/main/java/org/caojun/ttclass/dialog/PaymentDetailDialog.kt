package org.caojun.ttclass.dialog

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.NumberPicker
import org.caojun.filter.DecimalDigitsInputFilter
import org.caojun.ttclass.R

/**
 * Created by CaoJun on 2017-12-13.
 */
class PaymentDetailDialog: AlertDialog, DialogInterface.OnClickListener, NumberPicker.OnValueChangeListener {

    private val MaxValue = "MaxValue"
    private val MinValue = "MinValue"
    private val CurrentValue = "CurrentValue"
    private val Amount = "Amount"
    private var mEditText: EditText? = null
    private var mNumberPicker: NumberPicker? = null
    private var callback: NumberPicker.OnValueChangeListener? = null

    private var number: Int = 0
    private var amount: Float = 0f

    constructor(context: Context, callback: NumberPicker.OnValueChangeListener, maxValueNumber: Int, minValueNumber: Int, currentValueNumber: Int): super(context) {
        this.callback = callback

        setButton(DialogInterface.BUTTON_POSITIVE, context.getString(android.R.string.ok), this);
        setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(android.R.string.cancel), this);

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.dialog_bill_detail, null)
        setView(view)
        mEditText = view.findViewById(R.id.editText)
        mNumberPicker = view.findViewById(R.id.numberPicker)

        mNumberPicker?.maxValue = maxValueNumber
        mNumberPicker?.minValue = minValueNumber
        mNumberPicker?.value = currentValueNumber
        mNumberPicker?.setOnValueChangedListener(this)

        number = currentValueNumber

        mEditText?.filters = arrayOf<InputFilter>(DecimalDigitsInputFilter())
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        when (which) {
            DialogInterface.BUTTON_POSITIVE -> {
                try {
                    amount = mEditText?.text.toString().toFloat()
                } catch (e: Exception) {
                    amount = 0f
                }
                callback?.onValueChange(mNumberPicker, (amount * 100).toInt(), number)
            }
        }
    }

    override fun onValueChange(picker: NumberPicker?, oldValue: Int, newValue: Int) {
        number = newValue;
    }

    override fun onSaveInstanceState(): Bundle {
        val state = super.onSaveInstanceState()
        state.putInt(MaxValue, mNumberPicker?.maxValue?:0)
        state.putInt(MinValue, mNumberPicker?.minValue?:0)
        state.putInt(CurrentValue, mNumberPicker?.value?:0)
        state.putString(Amount, mEditText?.text.toString())
        return state
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        mNumberPicker?.maxValue = savedInstanceState.getInt(MaxValue)
        mNumberPicker?.minValue = savedInstanceState.getInt(MinValue)
        mNumberPicker?.value = savedInstanceState.getInt(CurrentValue)
        mEditText?.setText(savedInstanceState.getString(Amount))
    }
}