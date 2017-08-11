package org.caojun.widget;

import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import org.caojun.bloodpressure.R;

/**
 * Created by CaoJun on 2017/8/11.
 */

public class DigitalKeyboard extends TableLayout implements View.OnClickListener {

    public interface OnClickListener {
        boolean onClick(int key);
    }

    public static final int Key0 = 0;
    public static final int Key1 = 1;
    public static final int Key2 = 2;
    public static final int Key3 = 3;
    public static final int Key4 = 4;
    public static final int Key5 = 5;
    public static final int Key6 = 6;
    public static final int Key7 = 7;
    public static final int Key8 = 8;
    public static final int Key9 = 9;
    public static final int KeyDot = 10;
    public static final int KeyDelete = 11;
    public static final int KeyPrevious = 12;
    public static final int KeyNext = 13;
    public static final int KeyClose = 14;
    private static final int[] StringResId = {R.string.dk_0, R.string.dk_1, R.string.dk_2, R.string.dk_3, R.string.dk_4, R.string.dk_5, R.string.dk_6, R.string.dk_7, R.string.dk_8, R.string.dk_9, R.string.dk_dot};
    private static final int[] ResId = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9, R.id.btnDot, R.id.btnDelete, R.id.btnPrevious, R.id.btnNext, R.id.btnClose};
    private Button[] buttons;
    private EditText editText;
    private OnClickListener onClickListener;
    private Context context;
    private int inputType;

    public DigitalKeyboard(Context context) {
        super(context, null);
    }

    public DigitalKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.digital_keyboard, this);

        buttons = new Button[ResId.length];
        for (int i = 0;i < ResId.length;i ++) {
            buttons[i] = (Button) this.findViewById(ResId[i]);
            buttons[i].setOnClickListener(this);
        }

        setVisibility(View.GONE);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        buttons[KeyPrevious].setEnabled(onClickListener != null);
        buttons[KeyNext].setEnabled(onClickListener != null);
    }

    public void setPreviousEnabled(boolean enabled) {
        buttons[KeyPrevious].setEnabled(enabled);
    }

    public void setNextEnabled(boolean enabled) {
        buttons[KeyNext].setEnabled(enabled);
    }

    public void setEditText(EditText editText) {
        resetEditText();

        this.editText = editText;

        buttons[KeyDot].setEnabled((editText.getInputType() & InputType.TYPE_NUMBER_FLAG_DECIMAL) == InputType.TYPE_NUMBER_FLAG_DECIMAL);

        inputType = editText.getInputType();
        editText.setInputType(InputType.TYPE_NULL);

        setVisibility(View.VISIBLE);
    }

    public void close() {
        resetEditText();
        setVisibility(View.GONE);
    }

    private void resetEditText() {
        if (editText != null) {
            editText.setInputType(inputType);
        }
    }

    private void setEditTextContent(String text) {
        editText.setText(text);
        text = editText.getText().toString();
        editText.setSelection(text.length());
    }

    @Override
    public void onClick(View v) {
        if (editText == null) {
            return;
        }
        for (int i = 0;i < ResId.length;i ++) {
            if (v.getId() == ResId[i]) {
                if (i <= KeyDot) {
                    String text = editText.getText().toString();
                    if (i == KeyDot && text.contains(".")) {
                        //不能输入多个.
                        return;
                    }
                    if (!TextUtils.isEmpty(text) && Double.valueOf(text) == 0 && i <= Key9) {
                        //数值为0，再输入数字时，将0清除
                        text = "";
                    }
                    text += context.getString(StringResId[i]);
                    setEditTextContent(text);
                } else {
                    switch (i) {
                        case KeyDelete:
                            String text = editText.getText().toString();
                            if (text.length() > 0) {
                                text = text.substring(0, text.length() - 1);
                                setEditTextContent(text);
                            }
                            break;
                        case KeyPrevious:
                        case KeyNext:
                            if (onClickListener != null) {
                                close();
                                buttons[i].setEnabled(onClickListener.onClick(i));
                            }
                            break;
                        case KeyClose:
                            close();
                            break;
                    }
                }
                return;
            }
        }
    }
}
