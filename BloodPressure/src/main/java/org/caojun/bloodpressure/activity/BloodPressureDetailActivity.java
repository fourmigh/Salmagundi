package org.caojun.bloodpressure.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import org.caojun.bloodpressure.Constant;
import org.caojun.bloodpressure.R;
import org.caojun.bloodpressure.broadcast.AlarmReceiver;
import org.caojun.bloodpressure.ormlite.BloodPressure;
import org.caojun.bloodpressure.ormlite.BloodPressureDatabase;
import org.caojun.bloodpressure.utils.DataStorageUtils;
import org.caojun.bloodpressure.utils.TimeUtils;
import java.util.Calendar;

/**
 * Created by fourm on 2017/5/10.
 */

@Route(path = Constant.ACTIVITY_BLOODPRESSURE_DETAIL)
public class BloodPressureDetailActivity extends AppCompatActivity {

    private EditText etTime;
    private RadioGroup rgBloodPressure;
    private RadioButton[] rbBloodPressures;
    private LinearLayout llBloodPressure;
    private RadioGroup rgHand;
    private RadioButton rbLeft, rbRight;
    private EditText etHigh, etLow, etPulse;
    private EditText etWeight;
    private Button btnSave, btnDelete;
    private final String dateFormat = "yyyy/MM/dd HH:mm:ss";
    private final int[] IDType = {R.id.rbBloodPressure, R.id.rbMedicine, R.id.rbWeight};
    private final int[] ResId = {R.string.bp_bloodpressure_msg, R.string.bp_medicine_msg, R.string.bp_weight_msg};
    private final String[] KeySettings = {"bloodpressure_preference", "medicine_preference", "weight_preference"};

    @Autowired
    protected BloodPressure bloodPressure;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bloodpressure_detail);
        ARouter.getInstance().inject(this);

        etTime = (EditText) findViewById(R.id.etTime);
        rgBloodPressure = (RadioGroup) findViewById(R.id.rgBloodPressure);
//        rbBloodPressure = (RadioButton) findViewById(R.id.rbBloodPressure);
//        rbMedicine = (RadioButton) findViewById(R.id.rbMedicine);
//        rbWeight = (RadioButton) findViewById(R.id.rbWeight);
        rbBloodPressures = new RadioButton[IDType.length];
        for (int i = 0;i < IDType.length;i ++) {
            rbBloodPressures[i] = (RadioButton) findViewById(IDType[i]);
        }
        llBloodPressure = (LinearLayout) findViewById(R.id.llBloodPressure);
        rgHand = (RadioGroup) findViewById(R.id.rgHand);
        rbLeft = (RadioButton) findViewById(R.id.rbLeft);
        rbRight = (RadioButton) findViewById(R.id.rbRight);
        etHigh = (EditText) findViewById(R.id.etHigh);
        etLow = (EditText) findViewById(R.id.etLow);
        etPulse = (EditText) findViewById(R.id.etPulse);
        etWeight = (EditText) findViewById(R.id.etWeight);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);

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

        int type = getIntent().getIntExtra("type", getIndexType());
        setType(type);
        rbBloodPressures[type].setChecked(true);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSave();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doDelete();
            }
        });

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

        etHigh.addTextChangedListener(textWatcher);
        etLow.addTextChangedListener(textWatcher);
        etPulse.addTextChangedListener(textWatcher);
        etWeight.addTextChangedListener(textWatcher);

        doCheckSaveButton();
        doCheckDeleteButton();
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

    private void doDelete() {
        if(bloodPressure == null) {
            return;
        }
        if (BloodPressureDatabase.getInstance(this).delete(bloodPressure)) {
            finish();
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
                    BloodPressureDatabase.getInstance(this).insert(time, high, low, pulse, isLeft);
                    break;
                case BloodPressure.Type_Medicine:
                    BloodPressureDatabase.getInstance(this).insert(time);
                    break;
                case BloodPressure.Type_Weight:
                    float weight = Float.parseFloat(etWeight.getText().toString());
                    BloodPressureDatabase.getInstance(this).insert(time, weight);
                    DataStorageUtils.saveFloat(this, Constant.BMI_NAME, Constant.BMI_KEY_WEIGHT, weight);
                    break;
            }
        } else {
            switch (type) {
                case BloodPressure.Type_BloodPressure:
                    int high = Integer.parseInt(etHigh.getText().toString());
                    int low = Integer.parseInt(etLow.getText().toString());
                    int pulse = Integer.parseInt(etPulse.getText().toString());
                    boolean isLeft = rbLeft.isChecked();
                    bloodPressure.setHigh(high);
                    bloodPressure.setLow(low);
                    bloodPressure.setPulse(pulse);
                    bloodPressure.setLeft(isLeft);
                    BloodPressureDatabase.getInstance(this).update(bloodPressure);
                    break;
                case BloodPressure.Type_Weight:
                    float weight = Float.parseFloat(etWeight.getText().toString());
                    bloodPressure.setWeight(weight);
                    BloodPressureDatabase.getInstance(this).update(bloodPressure);
                    DataStorageUtils.saveFloat(this, Constant.BMI_NAME, Constant.BMI_KEY_WEIGHT, weight);
                    break;
            }
        }
        //设置提醒
        setAlarm(type);
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
                    return false;
                case BloodPressure.Type_Medicine:
                    return false;
                case BloodPressure.Type_Weight:
                    String text = etWeight.getText().toString();
                    float weight = TextUtils.isEmpty(text)?0:Float.parseFloat(text);
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

    private void doCheckDeleteButton() {
        btnDelete.setVisibility(bloodPressure == null?View.GONE:View.VISIBLE);
    }

    private void setAlarm(int type) {
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("msg", getString(ResId[type]));
        intent.putExtra("type", type);
        PendingIntent sender = PendingIntent.getBroadcast(this, type, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.HOUR, getAlarmTime(type));
//        calendar.add(Calendar.SECOND, 10);

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
    }

    private int getAlarmTime(int type) {
        SharedPreferences mSharedPreferences = getSharedPreferences(NotificaitonSettings.PREFER_NAME, Context.MODE_PRIVATE);
        int time = mSharedPreferences.getInt(KeySettings[type], 24);
        return time;
    }
}
