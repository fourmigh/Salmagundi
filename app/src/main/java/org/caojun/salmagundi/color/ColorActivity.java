package org.caojun.salmagundi.color;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import org.caojun.salmagundi.R;

/**
 * 渐变色
 * Created by CaoJun on 2016/10/28.
 */

public class ColorActivity extends AppCompatActivity {

    private Button btnOK;
//    private CheckBox cbHex;
    private EditText[] etColors;
    private final int[] ResIdColors = {R.id.etStartRed, R.id.etStartGreen, R.id.etStartBlue, R.id.etEndRed, R.id.etEndGreen, R.id.etEndBlue};
    private final int[] ResIdNumbers = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9/*, R.id.btnA, R.id.btnB, R.id.btnC, R.id.btnD, R.id.btnE, R.id.btnF*/};
    private Button[] btnNumbers;
    private ImageView ivColor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_color);

        btnOK = (Button) this.findViewById(R.id.btnOK);

        etColors = new EditText[ResIdColors.length];
        for(int i = 0;i < ResIdColors.length;i ++)
        {
            etColors[i] = (EditText) this.findViewById(ResIdColors[i]);
            etColors[i].setOnTouchListener(onTouchListener);

            formatColor(etColors[i], "0");
        }

        ivColor = (ImageView) this.findViewById(R.id.ivColor);
        btnNumbers = new Button[ResIdNumbers.length];
        for (int i = 0; i < ResIdNumbers.length; i++) {
            btnNumbers[i] = (Button) this.findViewById(ResIdNumbers[i]);
            btnNumbers[i].setOnClickListener(oclButtonNumber);
            btnNumbers[i].setOnLongClickListener(onLongClickListener);
        }
    }

    private OnLongClickListener onLongClickListener = new OnLongClickListener()
    {
        @Override
        public boolean onLongClick(View v) {
            View view = ColorActivity.this.getCurrentFocus();
            if(view instanceof EditText) {
                Button button = (Button) v;
                EditText editText = (EditText) view;
                editText.setText(button.getText());
                String text = editText.getText().toString();
                editText.setTag(text);
                editText.setSelection(text.length());
            }
            return true;
        }
    };

    private OnClickListener oclButtonNumber = new OnClickListener() {

        @Override
        public void onClick(View v) {
            View view = ColorActivity.this.getCurrentFocus();
            if(view instanceof EditText)
            {
                Button button = (Button) v;
                EditText editText = (EditText) view;
                String text = editText.getText().toString();
                text += button.getText().toString();
                formatColor(editText, text);
            }
        }
    };

    private OnTouchListener onTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            EditText editText = (EditText) v;
            int inputType = editText.getInputType();
            editText.setInputType(InputType.TYPE_NULL);
            editText.onTouchEvent(event);
            editText.setInputType(inputType);
            formatColor(editText, editText.getText().toString());
            return true;
        }
    };

    private void formatColor(EditText editText, String text)
    {
        if(TextUtils.isEmpty(text))
        {
            editText.setText("0");
        }
        else
        {
            int color = Integer.parseInt(text);
            if(color > 255)
            {
                color = 255;
            }
            editText.setText(String.valueOf(color));
        }
        text = editText.getText().toString();
        editText.setSelection(text.length());
    }
}
