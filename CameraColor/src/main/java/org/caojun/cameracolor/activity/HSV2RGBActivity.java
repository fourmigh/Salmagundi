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
import java.math.BigDecimal;

/**
 * HSV转RGB
 * Created by CaoJun on 2017/7/21.
 */

@Route(path = Constant.ACTIVITY_HSV2RGB)
public class HSV2RGBActivity extends AppCompatActivity {
    private EditText etRGB;
    private ImageView[] ivMinus, ivPlus;
    private EditText[] etColor;
    private SeekBar[] sbColor;
    private final static int[] IDMinus = {R.id.ivMinusH, R.id.ivMinusS, R.id.ivMinusV};
    private final static int[] IDPlus = {R.id.ivPlusH, R.id.ivPlusS, R.id.ivPlusV};
    private final static int[] MaxHSV = {359, 100, 100};
    private final static int[] IDETColor = {R.id.etH, R.id.etS, R.id.etV};
    private final static int[] IDSBColor = {R.id.sbH, R.id.sbS, R.id.sbV};

    private ImageView ivColor;
    private EditText[] etRGBs;
    private final static int[] IDETRGB = {R.id.etR, R.id.etG, R.id.etB};

    @Autowired
    protected String HEX;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hsv2rgb);
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
                        float color = Float.parseFloat(etColor[index].getText().toString());
                        color --;
                        if (color < 0) {
                            color = 0;
                        }
                        refreshData(index, color, false);
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
                        float color = Float.parseFloat(etColor[index].getText().toString());
                        color ++;
                        if (color > MaxHSV[index]) {
                            color = MaxHSV[index];
                        }
                        refreshData(index, color, false);
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
                    if (isCalculating) {
                        return;
                    }
                    String text = s.toString();
                    if (TextUtils.isEmpty(text)) {
                        sbColor[index].setProgress(0);
                        return;
                    }
                    float color = Float.parseFloat(text);
                    if (color > MaxHSV[index]) {
                        color = MaxHSV[index];
                    } else if (color < 0) {
                        color = 0;
                    }
                    refreshData(index, color, true);
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
                    if (!fromUser) {
                        return;
                    }
                    refreshData(index, progress, false);
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
        etRGBs = new EditText[IDETRGB.length];
        for (int i = 0;i < IDETRGB.length;i ++) {
            etRGBs[i] = (EditText) findViewById(IDETRGB[i]);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(HEX)) {
            setEditTextColor(etRGB, HEX);

            int color = Integer.parseInt(HEX, 16);
            float hsv[] = {0, 0, 0};
            ColorUtils.RGBtoHSV(Color.red(color), Color.green(color), Color.blue(color), hsv);
            for (int i = 0;i < hsv.length;i ++) {
                if (i > 0) {
                    hsv[i] *= 100;
                    BigDecimal b = new BigDecimal(hsv[i]);
                    //四舍五入，保留一位小数
                    hsv[i] = b.setScale(1,  BigDecimal.ROUND_HALF_UP).floatValue();
                } else {
                    BigDecimal b = new BigDecimal(hsv[i]);
                    hsv[i] = (int) b.setScale(0,  BigDecimal.ROUND_HALF_UP).floatValue();
                }
                refreshData(i, hsv[i], false);
            }

            setRGB();

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

    private void setRGB() {
        int rgb = Integer.parseInt(HEX, 16);
        int[] color = {Color.red(rgb), Color.green(rgb), Color.blue(rgb)};
        for (int i = 0;i < etRGBs.length;i ++) {
            String text = String.valueOf(color[i]);
            setEditTextColor(etRGBs[i], text);
        }
    }

    private void setEditTextColor(EditText editText, String value) {
        editText.setText(value);
        editText.setSelection(value.length());
    }

    private void resetEditTextRGB() {
        int color = getColor();
        HEX = ColorUtils.toHexEncoding(color).toUpperCase();
        setEditTextColor(etRGB, HEX);
    }

    private int getColor() {
        String strH = etColor[0].getText().toString();
        float h = TextUtils.isEmpty(strH)?0:Float.parseFloat(strH);
        String strS = etColor[1].getText().toString();
        float s = TextUtils.isEmpty(strS)?0:Float.parseFloat(strS);
        String strV = etColor[2].getText().toString();
        float v = TextUtils.isEmpty(strV)?0:Float.parseFloat(strV);
        float hsv[] = {h, s, v};
        return ColorUtils.HSVtoRGB(hsv);
    }

    private boolean isCalculating = false;
    private void refreshData(int index, float value, boolean isEditText) {
        isCalculating = true;
        if (!isEditText) {
            if (index == 0 && value == (int) value) {
                setEditTextColor(etColor[index], String.valueOf((int) value));
            } else {
                setEditTextColor(etColor[index], String.valueOf(value));
            }
        }
        resetEditTextRGB();
        setRGB();
        resetImageView();
        sbColor[index].setProgress((int)Math.rint((double)value));
        isCalculating = false;
    }
}
