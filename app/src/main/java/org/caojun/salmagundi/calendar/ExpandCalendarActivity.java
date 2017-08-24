package org.caojun.salmagundi.calendar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.alibaba.android.arouter.facade.annotation.Route;
import org.caojun.library.listener.OnDayClickListener;
import org.caojun.library.expandcalendar.view.ExpandCalendarView;
import org.caojun.library.model.CalendarDay;
import org.caojun.salmagundi.Constant;
import org.caojun.salmagundi.R;
import org.jetbrains.annotations.NotNull;

/**
 * Created by CaoJun on 2017/8/23.
 */

@Route(path = Constant.ACTIVITY_CALENDAR_EXPAND)
public class ExpandCalendarActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_expand);

        final TextView textView = findViewById(R.id.textView);

        ExpandCalendarView expandCalendarView = findViewById(R.id.expandCalendarView);
        expandCalendarView.setData(new CalendarDay(2017, 1, 1), new CalendarDay(2017, 12, 31));
        expandCalendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(@NotNull CalendarDay calendarDay) {
                textView.setText(calendarDay.getDayString());
            }
        });
        expandCalendarView.setSelectDay(new CalendarDay(2017, 8, 23));
    }
}
