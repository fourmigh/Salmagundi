package org.caojun.salmagundi.bloodpressure;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import org.caojun.salmagundi.BaseActivity;
import org.caojun.salmagundi.R;
import org.caojun.salmagundi.bloodpressure.ormlite.BloodPressure;
import org.caojun.salmagundi.bloodpressure.ormlite.BloodPressureDatabase;
import org.caojun.salmagundi.utils.TimeUtils;

/**
 * Created by fourm on 2017/5/10.
 */

public class BloodPressureDetailActivity extends BaseActivity {

    private EditText etTime;
    private RadioGroup rgBloodPressure;
    private RadioButton rbBloodPressure, rbMedicine, rbWeight;

    private LinearLayout llBloodPressure;
    private RadioGroup rgHand;
    private RadioButton rbLeft, rbRight;
    private EditText etHigh, etLow, etPulse;
    private Spinner spDevice;
    private EditText etWeight;
    private Button btnSave;

    private BloodPressure bloodPressure;
    private ArrayAdapter<CharSequence> adapter;

    private final String dateFormat = "yyyy/MM/dd HH:mm";
    private final int[] IDType = {R.id.rbBloodPressure, R.id.rbMedicine, R.id.rbWeight};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bloodpressure_detail);

        etTime = (EditText) findViewById(R.id.etTime);
        rgBloodPressure = (RadioGroup) findViewById(R.id.rgBloodPressure);
        rbBloodPressure = (RadioButton) findViewById(R.id.rbBloodPressure);
        rbMedicine = (RadioButton) findViewById(R.id.rbMedicine);
        rbWeight = (RadioButton) findViewById(R.id.rbWeight);
        llBloodPressure = (LinearLayout) findViewById(R.id.llBloodPressure);
        rgHand = (RadioGroup) findViewById(R.id.rgHand);
        rbLeft = (RadioButton) findViewById(R.id.rbLeft);
        rbRight = (RadioButton) findViewById(R.id.rbRight);
        etHigh = (EditText) findViewById(R.id.etHigh);
        etLow = (EditText) findViewById(R.id.etLow);
        etPulse = (EditText) findViewById(R.id.etPulse);
        spDevice = (Spinner) findViewById(R.id.spDevice);
        etWeight = (EditText) findViewById(R.id.etWeight);
        btnSave = (Button) findViewById(R.id.btnSave);

        bloodPressure = (BloodPressure) getIntent().getSerializableExtra("bloodPressure");

        adapter = ArrayAdapter.createFromResource(this, R.array.bloodpressure_device, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDevice.setAdapter(adapter);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSave();
            }
        });

        if (bloodPressure != null) {
            for (int i = 0;i < IDType.length;i ++) {
                findViewById(IDType[i]).setEnabled(false);
            }
            String time = TimeUtils.getTime(dateFormat, bloodPressure.getTime());
            etTime.setText(time);
            ((RadioButton) findViewById(IDType[bloodPressure.getType()])).setChecked(true);
            switch (bloodPressure.getType()) {
                case BloodPressure.Type_BloodPressure:
                    etHigh.setText(String.valueOf(bloodPressure.getHigh()));
                    etLow.setText(String.valueOf(bloodPressure.getLow()));
                    etPulse.setText(String.valueOf(bloodPressure.getPulse()));
                    if (bloodPressure.isLeft()) {
                        rbLeft.setChecked(true);
                    } else {
                        rbRight.setChecked(true);
                    }
                    spDevice.setSelection(bloodPressure.getDevice());
                    break;
                case BloodPressure.Type_Medicine:
                    break;
                case BloodPressure.Type_Weight:
                    etWeight.setText(String.valueOf(bloodPressure.getWeight()));
                    break;
            }
        } else {
            long t = TimeUtils.getTime();
            etTime.setTag(String.valueOf(t));
            String time = TimeUtils.getTime(dateFormat, t);
            etTime.setText(time);
        }
        doCheckSaveButton();

        rgBloodPressure.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                setType(getIndexType());
                doCheckSaveButton();
            }
        });

        rgHand.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                doCheckSaveButton();
            }
        });

        spDevice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                doCheckSaveButton();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                doCheckSaveButton();
            }
        });

        setType(getIndexType());

        etHigh.addTextChangedListener(textWatcher);
        etLow.addTextChangedListener(textWatcher);
        etPulse.addTextChangedListener(textWatcher);
        etWeight.addTextChangedListener(textWatcher);
    }

    private TextWatcher textWatcher = new TextWatcher() {
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
    };

    private int getIndexType() {
        for (int i = 0;i < IDType.length; i ++) {
            if (rgBloodPressure.getCheckedRadioButtonId() == IDType[i]) {
                return i;
            }
        }
        rbBloodPressure.setChecked(true);
        return BloodPressure.Type_BloodPressure;
    }

    private void setType(int type) {
        switch (type) {
            case BloodPressure.Type_BloodPressure:
                llBloodPressure.setVisibility(View.VISIBLE);
                etWeight.setVisibility(View.GONE);
                break;
            case BloodPressure.Type_Medicine:
                llBloodPressure.setVisibility(View.GONE);
                etWeight.setVisibility(View.GONE);
                break;
            case BloodPressure.Type_Weight:
                llBloodPressure.setVisibility(View.GONE);
                etWeight.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void doSave() {
        int type = getIndexType();
        if (bloodPressure == null) {
            long time = Long.parseLong((String) etTime.getTag());
            switch (type) {
                case BloodPressure.Type_BloodPressure:
                    int high = Integer.parseInt(etHigh.getText().toString());
                    int low = Integer.parseInt(etLow.getText().toString());
                    int pulse = Integer.parseInt(etPulse.getText().toString());
                    boolean isLeft = rbLeft.isChecked();
                    int device = spDevice.getSelectedItemPosition();
                    BloodPressureDatabase.getInstance(this).insert(time, high, low, pulse, isLeft, device);
                    break;
                case BloodPressure.Type_Medicine:
                    BloodPressureDatabase.getInstance(this).insert(time);
                    break;
                case BloodPressure.Type_Weight:
                    float weight = Float.parseFloat(etWeight.getText().toString());
                    BloodPressureDatabase.getInstance(this).insert(time, weight);
                    break;
            }
        } else {
            switch (type) {
                case BloodPressure.Type_BloodPressure:
                    int high = Integer.parseInt(etHigh.getText().toString());
                    int low = Integer.parseInt(etLow.getText().toString());
                    int pulse = Integer.parseInt(etPulse.getText().toString());
                    boolean isLeft = rbLeft.isChecked();
                    int device = spDevice.getSelectedItemPosition();
                    bloodPressure.setHigh(high);
                    bloodPressure.setLow(low);
                    bloodPressure.setPulse(pulse);
                    bloodPressure.setLeft(isLeft);
                    bloodPressure.setDevice(device);
                    BloodPressureDatabase.getInstance(this).update(bloodPressure);
                    break;
                case BloodPressure.Type_Weight:
                    float weight = Float.parseFloat(etWeight.getText().toString());
                    bloodPressure.setWeight(weight);
                    BloodPressureDatabase.getInstance(this).update(bloodPressure);
                    break;
            }
        }
        finish();
    }

    private boolean canSave() {
        if (bloodPressure == null) {
            //添加
            switch (getIndexType()) {
                case BloodPressure.Type_BloodPressure:
                    String high = etHigh.getText().toString();
                    if (TextUtils.isEmpty(high)) {
                        return false;
                    }
                    String low = etLow.getText().toString();
                    if (TextUtils.isEmpty(low)) {
                        return false;
                    }
                    try {
                        if (Integer.parseInt(high) <= Integer.parseInt(low)) {
                            return false;
                        }
                    } catch (NumberFormatException e) {
                        return false;
                    }
                    String pulse = etPulse.getText().toString();
                    if (TextUtils.isEmpty(pulse)) {
                        return false;
                    }
                    try {
                        Integer.parseInt(pulse);
                    } catch (NumberFormatException e) {
                        return false;
                    }
                    break;
                case BloodPressure.Type_Medicine:
                    return true;
                case BloodPressure.Type_Weight:
                    String weight = etWeight.getText().toString();
                    return !TextUtils.isEmpty(weight);
                default:
                    return false;
            }
        } else {
            //修改
            switch (getIndexType()) {
                case BloodPressure.Type_BloodPressure:
                    int high = Integer.parseInt(etHigh.getText().toString());
                    if (high != bloodPressure.getHigh()) {
                        return true;
                    }
                    int low = Integer.parseInt(etLow.getText().toString());
                    if (low != bloodPressure.getLow()) {
                        return true;
                    }
                    int pulse = Integer.parseInt(etPulse.getText().toString());
                    if (pulse != bloodPressure.getPulse()) {
                        return true;
                    }
                    boolean isLeft = rbLeft.isChecked();
                    if (isLeft != bloodPressure.isLeft()) {
                        return true;
                    }
                    int device = spDevice.getSelectedItemPosition();
                    if (device != bloodPressure.getDevice()) {
                        return true;
                    }
                    return false;
                case BloodPressure.Type_Medicine:
                    return false;
                case BloodPressure.Type_Weight:
                    float weight = Float.parseFloat(etWeight.getText().toString());
                    return bloodPressure.getWeight() != weight;
                default:
                    return false;
            }
        }
        return true;
    }

    private void doCheckSaveButton() {
        btnSave.setEnabled(canSave());
    }
}
