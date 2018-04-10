package org.caojun.rotaryphone.preference;

import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by CaoJun on 2017/7/26.
 */

public class SeekBarPreference extends DialogPreference implements DialogInterface.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private static final String ANDROIDNS = "http://schemas.android.com/apk/res/android";
    private SeekBar mSeekBar;
    private TextView mValueText;
    private int mDefault = 3, mMax = 9, mValue = 3, mMin = 3;

    public SeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDefault = attrs.getAttributeIntValue(ANDROIDNS,"defaultValue", mDefault);
        mMax = attrs.getAttributeIntValue(ANDROIDNS,"max", mMax);
        mMin = attrs.getAttributeIntValue(ANDROIDNS,"min", mMin);
    }

    @Override
    protected View onCreateDialogView() {
        LinearLayout.LayoutParams params;
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(6,6,6,6);

        mValueText = new TextView(getContext());
        mValueText.setGravity(Gravity.CENTER_HORIZONTAL);
        mValueText.setTextSize(32);
        params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.addView(mValueText, params);

        mSeekBar = new SeekBar(getContext());
        mSeekBar.setOnSeekBarChangeListener(this);
        layout.addView(mSeekBar, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        if (shouldPersist()) {
            mValue = getPersistedInt(mDefault);
        }

        mSeekBar.setMax(mMax);
        mSeekBar.setProgress(mValue);
        return layout;
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);
        mSeekBar.setMax(mMax);
        mSeekBar.setProgress(mValue);
    }

    @Override
    protected void onSetInitialValue(boolean restore, Object defaultValue)
    {
        super.onSetInitialValue(restore, defaultValue);
        if (restore) {
            mValue = shouldPersist() ? getPersistedInt(mDefault) : 0;
        } else {
            mValue = (Integer) defaultValue;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seek, int value, boolean fromTouch) {
        if (value < mMin) {
            value = mMin;
            mSeekBar.setProgress(value);
        }
        String t = String.valueOf(value);
        mValueText.setText(t);
        callChangeListener(new Integer(value));
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    public void setMax(int max) {
        mMax = max;
    }

    public int getMax() {
        return mMax;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                if (shouldPersist()) {
                    persistInt(mSeekBar.getProgress());
                }
                break;
        }
        super.onClick(dialog, which);
    }
}
