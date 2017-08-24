package org.caojun.salmagundi.calendar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.alibaba.android.arouter.facade.annotation.Route;
import org.caojun.library.listener.OnDayClickListener;
import org.caojun.library.model.CalendarDay;
import org.caojun.library.monthswitchpager.view.MonthSwitchView;
import org.caojun.salmagundi.Constant;
import org.caojun.salmagundi.R;
import org.jetbrains.annotations.NotNull;

/**
 * Created by CaoJun on 2017/8/23.
 */

@Route(path = Constant.ACTIVITY_CALENDAR_MONTH)
public class MonthCalendarActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_month);

        final TextView textView = findViewById(R.id.textView);

        MonthSwitchView monthSwitchView = findViewById(R.id.monthSwitchView);
        monthSwitchView.setData(new CalendarDay(2017, 1, 1), new CalendarDay(2017, 12, 31));
        monthSwitchView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(@NotNull CalendarDay calendarDay) {
                textView.setText(calendarDay.getDayString());
            }
        });
        monthSwitchView.setSelectDay(new CalendarDay(2017, 8, 23));
    }
}
