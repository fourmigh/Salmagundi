package org.caojun.bloodpressure.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;

import org.caojun.bloodpressure.Constant;
import org.caojun.bloodpressure.R;
import org.caojun.bloodpressure.utils.BMIUtils;
import org.caojun.bloodpressure.utils.DataStorageUtils;

import java.text.DecimalFormat;

/**
 * 计算BMI
 * Created by CaoJun on 2017/7/31.
 */

@Route(path = Constant.ACTIVITY_BMI_CAL)
public class BMICalActivity extends AppCompatActivity {

    private final int[] MaxHW = {225, 1405};//姚明身高226厘米，体重140.6公斤
    private final int[] ResIdBMI = {R.id.tvChina, R.id.tvAsia, R.id.tvWHO};
    private final int[] ResIdTvHW = {R.id.tvHeight, R.id.tvWeight};
    private final int[] ResIdSbHW = {R.id.sbHeight, R.id.sbWeight};
    private final int[] ResIdMinusHW = {R.id.ivMinusHeight, R.id.ivMinusWeight};
    private final int[] ResIdPlusHW = {R.id.ivPlusHeight, R.id.ivPlusWeight};

    private TextView[] tvBMI;
    private TextView[] tvHW;
    private SeekBar[] sbHW;
    private ImageView[] ivMinusHW, ivPlusHW;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_bmi_cal);

        tvBMI = new TextView[ResIdBMI.length];
        for (int i = 0;i < ResIdBMI.length;i ++) {
            tvBMI[i] = (TextView) findViewById(ResIdBMI[i]);
        }
        tvHW = new TextView[ResIdTvHW.length];
        for (int i = 0;i < ResIdTvHW.length;i ++) {
            tvHW[i] = (TextView) findViewById(ResIdTvHW[i]);
        }
        sbHW = new SeekBar[ResIdSbHW.length];
        for (int i = 0;i < ResIdSbHW.length;i ++) {
            sbHW[i] = (SeekBar) findViewById(ResIdSbHW[i]);
            sbHW[i].setMax(MaxHW[i]);
            sbHW[i].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    freshData();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }
        ivMinusHW = new ImageView[ResIdMinusHW.length];
        for (int i = 0;i < ResIdMinusHW.length;i ++) {
            ivMinusHW[i] = (ImageView) findViewById(ResIdMinusHW[i]);
            final int index = i;
            ivMinusHW[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int progress = sbHW[index].getProgress();
                    progress --;
                    if (progress < 0) {
                        progress = 0;
                    }
                    sbHW[index].setProgress(progress);
                }
            });
        }
        ivPlusHW = new ImageView[ResIdPlusHW.length];
        for (int i = 0;i < ResIdPlusHW.length;i ++) {
            ivPlusHW[i] = (ImageView) findViewById(ResIdPlusHW[i]);
            final int index = i;
            ivPlusHW[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int progress = sbHW[index].getProgress();
                    progress ++;
                    if (progress > MaxHW[index]) {
                        progress = MaxHW[index];
                    }
                    sbHW[index].setProgress(progress);
                }
            });
        }

        int height = DataStorageUtils.loadInt(this, Constant.BMI_NAME, Constant.BMI_KEY_HEIGHT, 170) - 1;
        float weight = DataStorageUtils.loadFloat(this, Constant.BMI_NAME, Constant.BMI_KEY_WEIGHT, 70) - 0.1f;

        sbHW[0].setProgress(height);
        sbHW[1].setProgress((int)(weight * 10));//体重有一位小数，乘以10去除小数

        freshData();
    }

    private void freshData() {
        int height = sbHW[0].getProgress() + 1;
        float weight = (float) (sbHW[1].getProgress() + 1) / 10;

        tvHW[0].setText(getString(R.string.bp_height_unit, String.valueOf(height)));
        tvHW[1].setText(getString(R.string.bp_weight_unit, String.valueOf(weight)));

        String[] types = getResources().getStringArray(R.array.bmi_type);
        float bmi = BMIUtils.getBMI(height, weight);
        for (int i = 0;i < ResIdBMI.length;i ++) {
            byte b = 0;
            DecimalFormat decimalFormat = new DecimalFormat("0.0");
            switch (i) {
                case Constant.BMI_CHINA:
                    b = BMIUtils.getChina(bmi);
                    break;
                case Constant.BMI_ASIA:
                    b = BMIUtils.getAsia(bmi);
                    break;
                case Constant.BMI_WHO:
                    b = BMIUtils.getWHO(bmi);
                    break;
            }
            String strBMI = getString(R.string.bp_bmi_unit, types[b], decimalFormat.format(bmi));
            tvBMI[i].setText(strBMI);
        }
    }
}
