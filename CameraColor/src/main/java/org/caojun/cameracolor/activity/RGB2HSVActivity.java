package org.caojun.cameracolor.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import org.caojun.cameracolor.Constant;
import org.caojun.cameracolor.R;
import org.caojun.cameracolor.utils.ColorUtils;
import java.text.DecimalFormat;

/**
 * RGB转HSV
 * Created by CaoJun on 2017/7/13.
 */

@Route(path = Constant.ACTIVITY_RGB2HSV)
public class RGB2HSVActivity extends AppCompatActivity {

    private EditText etRGB;
    private ImageView[] ivMinus, ivPlus;
    private EditText[] etColor;
    private SeekBar[] sbColor;
    private final static int[] IDMinus = {R.id.ivMinusR, R.id.ivMinusG, R.id.ivMinusB};
    private final static int[] IDPlus = {R.id.ivPlusR, R.id.ivPlusG, R.id.ivPlusB};
    private final static int[] IDETColor = {R.id.etR, R.id.etG, R.id.etB};
    private final static int[] IDSBColor = {R.id.sbR, R.id.sbG, R.id.sbB};

    private ImageView ivColor;
    private EditText[] etHSV;
    private final static int[] IDETHSV = {R.id.etH, R.id.etS, R.id.etV};

    @Autowired
    protected String HEX;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rgb2hsv);
        ARouter.getInstance().inject(this);

        etRGB = (EditText) findViewById(R.id.etRGB);
        ivMinus = new ImageView[IDMinus.length];
        for (int i = 0;i < IDMinus.length;i ++) {
            ivMinus[i] = (ImageView) findViewById(IDMinus[i]);
            final int index = i;
            ivMinus[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        int color = Integer.parseInt(etColor[index].getText().toString());
                        color --;
                        if (color < 0) {
                            color = 0;
                        }
                        setEditTextColor(etColor[index], color);
                        sbColor[index].setProgress(color);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        ivPlus = new ImageView[IDPlus.length];
        for (int i = 0;i < IDPlus.length;i ++) {
            ivPlus[i] = (ImageView) findViewById(IDPlus[i]);
            final int index = i;
            ivPlus[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        int color = Integer.parseInt(etColor[index].getText().toString());
                        color ++;
                        if (color > 255) {
                            color = 255;
                        }
                        setEditTextColor(etColor[index], color);
                        sbColor[index].setProgress(color);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        etColor = new EditText[IDETColor.length];
        for (int i = 0;i < IDETColor.length;i ++) {
            etColor[i] = (EditText) findViewById(IDETColor[i]);
            final int index = i;
            etColor[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String text = s.toString();
                    if (TextUtils.isEmpty(text)) {
                        sbColor[index].setProgress(0);
                        return;
                    }
                    int color = Integer.parseInt(text);
                    if (color > 255) {
                        color = 255;
                    } else if (color < 0) {
                        color = 0;
                    }
                    sbColor[index].setProgress(color);
                }
            });
        }
        sbColor = new SeekBar[IDSBColor.length];
        for (int i = 0;i < IDSBColor.length;i ++) {
            sbColor[i] = (SeekBar) findViewById(IDSBColor[i]);
            final int index = i;
            sbColor[i].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    setEditTextColor(etColor[index], progress);
                    resetEditTextRGB();
                    setHSV();
                    resetImageView();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }

        ivColor = (ImageView) findViewById(R.id.ivColor);
        etHSV = new EditText[IDETHSV.length];
        for (int i = 0;i < IDETHSV.length;i ++) {
            etHSV[i] = (EditText) findViewById(IDETHSV[i]);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(HEX)) {
            etRGB.setText(HEX);
            etRGB.setSelection(HEX.length());

            int color = Integer.parseInt(HEX, 16);
            int[] colors = {Color.red(color), Color.green(color), Color.blue(color)};
            for (int i = 0;i < colors.length;i ++) {
                setEditTextColor(etColor[i], colors[i]);
                sbColor[i].setProgress(colors[i]);
            }

            setHSV();

            ViewTreeObserver vto = ivColor.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ivColor.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    resetImageView();
                }
            });
        }
    }

    private void resetImageView() {
        int color = getColor();
        ivColor.setBackgroundColor(color);
    }

    private void setHSV() {
        int r = sbColor[0].getProgress();
        int g = sbColor[1].getProgress();
        int b = sbColor[2].getProgress();
        float hsv[] = {0, 0, 0};
        ColorUtils.RGBtoHSV(r, g, b, hsv);
        for (int i = 0;i < etHSV.length;i ++) {
            String text;
            switch(i) {
                case 0:
                    text = String.valueOf((int)hsv[i]);
                    break;
                default:
                    hsv[i] *= 100;
                    DecimalFormat decimalFormat = new DecimalFormat("0.0");
                    text = decimalFormat.format(hsv[i]);
                    break;
            }
            etHSV[i].setText(text);
            etHSV[i].setSelection(text.length());
        }
    }

    private void setEditTextColor(EditText editText, int color) {
        String value = String.valueOf(color);
        editText.setText(value);
        editText.setSelection(value.length());
    }

    private void resetEditTextRGB() {
        int color = getColor();
        HEX = ColorUtils.toHexEncoding(color);
        etRGB.setText(HEX.toUpperCase());
        etRGB.setSelection(HEX.length());
    }

    private int getColor() {
        int r = sbColor[0].getProgress();
        int g = sbColor[1].getProgress();
        int b = sbColor[2].getProgress();
        return Color.argb(0xFF, r, g, b);
    }
}
