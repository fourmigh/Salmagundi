package org.caojun.bloodpressure.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

import org.caojun.bloodpressure.Constant;
import org.caojun.bloodpressure.R;
import org.caojun.bloodpressure.adapter.BloodPressureAdapter;
import org.caojun.bloodpressure.ormlite.BloodPressure;
import org.caojun.bloodpressure.ormlite.BloodPressureDatabase;
import org.caojun.bloodpressure.utils.DataStorageUtils;
import org.caojun.library.listener.OnDayClickListener;
import org.caojun.library.model.CalendarDay;
import org.caojun.library.monthswitchpager.view.MonthSwitchView;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by CaoJun on 2017/8/24.
 */

/**
 * 日历样式首页
 */
@Route(path = Constant.ACTIVITY_CALENDAR)
public class CalendarActivity extends AppCompatActivity {

    private MonthSwitchView monthSwitchView;
    private ListView listView;
    private BloodPressureAdapter adapter;
    private List<BloodPressure> list, listDay;
    private Button btnAdd;
    private CalendarDay calendarDay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        listView = findViewById(R.id.lvBloodPressure);
        btnAdd = findViewById(R.id.btnAdd);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                doUpdate(position);
            }
        });

        monthSwitchView = findViewById(R.id.monthSwitchView);
        monthSwitchView.setData(new CalendarDay(2017, 6, 28), new CalendarDay(2117, 12, 31));
        monthSwitchView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(@NotNull CalendarDay calendarDay) {
                resetListView(calendarDay);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAdd();
            }
        });

        calendarDay = new CalendarDay();
        listDay = new ArrayList<>();
    }

    private void resetListView(CalendarDay calendarDay) {
        boolean isNew = list == null || adapter == null;

        listDay.clear();
        for(BloodPressure bloodPressure:list) {
            CalendarDay day = new CalendarDay(bloodPressure.getTime());
//            if (day == calendarDay) {
            if (day.getYear() == calendarDay.getYear() && day.getMonth() == calendarDay.getMonth() && day.getDay() == calendarDay.getDay()) {
                listDay.add(bloodPressure);
            }
        }

        if(isNew) {
            adapter = new BloodPressureAdapter(this, listDay);
            listView.setAdapter(adapter);
        } else {
            adapter.setData(listDay);
            adapter.notifyDataSetChanged();
        }
    }

    private void doUpdate(int position) {
        BloodPressure bloodPressure = (BloodPressure) adapter.getItem(position);
        ARouter.getInstance().build(Constant.ACTIVITY_BLOODPRESSURE_DETAIL).withParcelable("bloodPressure", bloodPressure).navigation();
    }

    private void doAdd() {
        ARouter.getInstance().build(Constant.ACTIVITY_BLOODPRESSURE_DETAIL).navigation();
    }

    @Override
    protected void onResume() {
        super.onResume();

        list = BloodPressureDatabase.getInstance(this).query();

        monthSwitchView.setSelectDay(calendarDay);
        resetListView(calendarDay);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.calendar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.menuList) {
            DataStorageUtils.saveBoolean(this, Constant.QUIT_FROM_CALENDAR_NAME, Constant.QUIT_FROM_CALENDAR, false);
            ARouter.getInstance().build(Constant.ACTIVITY_BLOODPRESSURE).navigation();
            finish();
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.menuToday) {
            ARouter.getInstance().build(Constant.ACTIVITY_TODAY).navigation();
            return true;
        }
        if (id == R.id.menuBMI) {
            ARouter.getInstance().build(Constant.ACTIVITY_BMI).navigation();
            return true;
        }
//        if (id == R.id.menuNotificaiton) {
//            ARouter.getInstance().build(Constant.ACTIVITY_NOTIFICATION_SETTINGS).navigation();
//            return true;
//        }
        if (id == R.id.menuData) {
            ARouter.getInstance().build(Constant.ACTIVITY_DATA).navigation();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DataStorageUtils.saveBoolean(this, Constant.QUIT_FROM_CALENDAR_NAME, Constant.QUIT_FROM_CALENDAR, true);
        super.onBackPressed();
    }
}
