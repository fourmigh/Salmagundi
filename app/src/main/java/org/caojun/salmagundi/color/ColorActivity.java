package org.caojun.salmagundi.color;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import org.caojun.salmagundi.BaseActivity;
import org.caojun.salmagundi.R;
import org.caojun.salmagundi.qrcode.QRCodeActivity;
import org.caojun.salmagundi.utils.DataStorageUtils;
import org.caojun.salmagundi.utils.LogUtils;

/**
 * 渐变色
 * Created by CaoJun on 2016/10/28.
 */

public class ColorActivity extends BaseActivity {

//    private CheckBox cbHex;
    private EditText[] etColors;
    private final int[] ResIdColors = {R.id.etStartRed, R.id.etStartGreen, R.id.etStartBlue, R.id.etEndRed, R.id.etEndGreen, R.id.etEndBlue};
    private final int[] ResIdNumbers = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9/*, R.id.btnA, R.id.btnB, R.id.btnC, R.id.btnD, R.id.btnE, R.id.btnF*/};
    private Button[] btnNumbers;
    private ImageView ivColor;
    private Bitmap bmGradient;
    private static Color LastStartColor, LastEndColor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_color);

        etColors = new EditText[ResIdColors.length];
        for(int i = 0;i < ResIdColors.length;i ++)
        {
            etColors[i] = (EditText) this.findViewById(ResIdColors[i]);
            etColors[i].setOnTouchListener(onTouchListener);
            etColors[i].addTextChangedListener(textWatcher);
            formatColor(etColors[i], "0");
        }

        ivColor = (ImageView) this.findViewById(R.id.ivColor);
        ViewTreeObserver vto = ivColor.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                int width = ivColor.getMeasuredWidth();
                int height = ivColor.getMeasuredHeight();
                checkColor(width, height);
                return true;
            }
        });

        btnNumbers = new Button[ResIdNumbers.length];
        for (int i = 0; i < ResIdNumbers.length; i++) {
            btnNumbers[i] = (Button) this.findViewById(ResIdNumbers[i]);
            btnNumbers[i].setOnClickListener(oclButtonNumber);
            btnNumbers[i].setOnLongClickListener(onLongClickListener);
        }

        ibGallery3d.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                String message = getString(R.string.color_save, labelGallery3d, getString(R.string.successed));
                String description = getStartColor().toString() + "-" + getEndColor().toString();
                LogUtils.e("description", description);
                if(TextUtils.isEmpty(MediaStore.Images.Media.insertImage(getContentResolver(), bmGradient, getString(R.string.color_title), description)))
                {
                    message = getString(R.string.qrcode_save, labelGallery3d, getString(R.string.failed));
                    ibGallery3d.setEnabled(true);
                }
                else
                {
                    LastStartColor = getStartColor();
                    LastEndColor = getEndColor();
                    ibGallery3d.setEnabled(false);
                }
                Toast.makeText(ColorActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadColors()
    {
        Integer[] intColorStart = DataStorageUtils.loadIntArray(this, "GradientColor", "colorStart", 0);
        if(intColorStart == null)
        {
            intColorStart = new Integer[]{0xFF, 0xFF, 0, 0};
        }
        Integer[] intColorEnd = DataStorageUtils.loadIntArray(this, "GradientColor", "colorEnd", 0);
        if(intColorEnd == null)
        {
            intColorEnd = new Integer[]{0xFF, 0, 0, 0xFF};
        }
        formatColor(etColors[0], String.valueOf(intColorStart[1]));
        formatColor(etColors[1], String.valueOf(intColorStart[2]));
        formatColor(etColors[2], String.valueOf(intColorStart[3]));
        formatColor(etColors[3], String.valueOf(intColorEnd[1]));
        formatColor(etColors[4], String.valueOf(intColorEnd[2]));
        formatColor(etColors[5], String.valueOf(intColorEnd[3]));
    }

    private void saveColors(Color colorStart, Color colorEnd)
    {
        int[] intColorStart = new int[]{colorStart.getAlpha(), colorStart.getRed(), colorStart.getGreen(), colorStart.getBlue()};
        int[] intColorEnd = new int[]{colorEnd.getAlpha(), colorEnd.getRed(), colorEnd.getGreen(), colorEnd.getBlue()};
        DataStorageUtils.saveIntArray(this, "GradientColor", "colorStart", intColorStart);
        DataStorageUtils.saveIntArray(this, "GradientColor", "colorEnd", intColorEnd);
    }

    private void refreshColor(int width, int height)
    {
        int red = Integer.parseInt(etColors[0].getText().toString());
        int green = Integer.parseInt(etColors[1].getText().toString());
        int blue = Integer.parseInt(etColors[2].getText().toString());
        Color colorStart = new Color(red, green, blue);
        red = Integer.parseInt(etColors[3].getText().toString());
        green = Integer.parseInt(etColors[4].getText().toString());
        blue = Integer.parseInt(etColors[5].getText().toString());
        Color colorEnd = new Color(red, green, blue);
        Color[] colors = ColorUtils.getGradientColor(colorStart, colorEnd, width);
        bmGradient = ColorUtils.createGradientImage(colors, width, height);
        if(bmGradient != null) {
            ivColor.setImageBitmap(bmGradient);
        }
        ibGallery3d.setEnabled(true);
        saveColors(colorStart, colorEnd);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadColors();
        if(ivColor != null) {
            checkColor(ivColor.getWidth(), ivColor.getHeight());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LastStartColor = null;
        LastEndColor = null;
    }

    private TextWatcher textWatcher = new TextWatcher(){
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(ivColor != null) {
                checkColor(ivColor.getWidth(), ivColor.getHeight());
            }
        }
    };

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

    private void checkColor(int width, int height)
    {
        if(etColors == null || etColors.length != 6)
        {
            return;
        }
        for(int i = 0;i < etColors.length;i ++)
        {
            if(etColors[i] == null)
            {
                return;
            }
        }
        Color colorStart = getStartColor();
        Color colorEnd = getEndColor();
        if(colorStart.equals(LastStartColor) && colorEnd.equals(LastEndColor))
        {
            return;
        }
        if(!colorStart.equals(colorEnd))
        {
            refreshColor(width, height);
        }
    }

    private Color getStartColor()
    {
        int red = Integer.parseInt(etColors[0].getText().toString());
        int green = Integer.parseInt(etColors[1].getText().toString());
        int blue = Integer.parseInt(etColors[2].getText().toString());
        return new Color(red, green, blue);
    }

    private Color getEndColor()
    {
        int red = Integer.parseInt(etColors[3].getText().toString());
        int green = Integer.parseInt(etColors[4].getText().toString());
        int blue = Integer.parseInt(etColors[5].getText().toString());
        return new Color(red, green, blue);
    }
}
