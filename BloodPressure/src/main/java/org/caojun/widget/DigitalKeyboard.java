package org.caojun.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TableLayout;
import org.caojun.bloodpressure.R;

/**
 * Created by CaoJun on 2017/8/11.
 */

public class DigitalKeyboard extends TableLayout {
    public DigitalKeyboard(Context context) {
        super(context, null);
    }

    public DigitalKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.digital_keyboard, this);
    }
}
