package org.caojun.bloodpressure.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alibaba.android.arouter.facade.annotation.Route;

import org.caojun.bloodpressure.Constant;
import org.caojun.bloodpressure.R;
import org.caojun.bloodpressure.utils.DataStorageUtils;

/**
 * Created by CaoJun on 2017/7/7.
 */

/**
 * 设置身高计算BMI
 */
@Route(path = Constant.ACTIVITY_BMI)
public class BMIActivity extends AppCompatActivity {

    private EditText etHeight;
    private Button btnSave;
    private RadioGroup rgStandard;
    private RadioButton[] radioButtons;
    private final int[] IDStandard = {R.id.rbChina, R.id.rbAsia, R.id.rbWHO};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_bmi);

        etHeight = (EditText) findViewById(R.id.etHeight);
        btnSave = (Button) findViewById(R.id.btnSave);
        rgStandard = (RadioGroup) findViewById(R.id.rgStandard);
        radioButtons = new RadioButton[IDStandard.length];
        for (int i = 0;i < IDStandard.length;i ++) {
            radioButtons[i] = (RadioButton) findViewById(IDStandard[i]);
        }

        etHeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                doCheckSaveButton();
            }
        });

        rgStandard.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setStandard(getIndexStandard());
                doCheckSaveButton();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSave();
            }
        });

        byte standard = DataStorageUtils.loadByte(this, Constant.BMI_NAME, Constant.BMI_KEY_STANDARD, Constant.BMI_CHINA);
        ((RadioButton) findViewById(IDStandard[standard])).setChecked(true);

        int height = DataStorageUtils.loadInt(this, Constant.BMI_NAME, Constant.BMI_KEY_HEIGHT, 0);
        if (height > 0) {
            etHeight.setText(String.valueOf(height));
            etHeight.setSelection(etHeight.getText().toString().length());
        }

        setStandard(getIndexStandard());
        doCheckSaveButton();
    }

    private void doSave() {
        byte standard = getIndexStandard();
        int height = Integer.parseInt(etHeight.getText().toString());
        if (DataStorageUtils.saveByte(this, Constant.BMI_NAME, Constant.BMI_KEY_STANDARD, standard) && DataStorageUtils.saveInt(this, Constant.BMI_NAME, Constant.BMI_KEY_HEIGHT, height)) {
            finish();
        }
    }

    private void setStandard(int standard) {
        radioButtons[standard].setChecked(true);
    }

    private byte getIndexStandard() {
        for (byte i = 0;i < IDStandard.length; i ++) {
            if (rgStandard.getCheckedRadioButtonId() == IDStandard[i]) {
                return i;
            }
        }
        return Constant.BMI_CHINA;
    }

    private void doCheckSaveButton() {
        btnSave.setEnabled(canSave());
    }

    private boolean canSave() {
        String height = etHeight.getText().toString();
        if (TextUtils.isEmpty(height)) {
            return false;
        }
        try {
            int newHeight = Integer.parseInt(height);
            int oldHeight = DataStorageUtils.loadInt(this, Constant.BMI_NAME, Constant.BMI_KEY_HEIGHT, 0);

            int newStandard = getIndexStandard();
            int oldStandard = DataStorageUtils.loadByte(this, Constant.BMI_NAME, Constant.BMI_KEY_STANDARD, Constant.BMI_CHINA);

            if (newHeight == oldHeight && newStandard == oldStandard) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
