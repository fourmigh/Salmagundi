package org.caojun.salmagundi.calendar;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import org.caojun.salmagundi.Constant;
import org.caojun.salmagundi.R;
import org.caojun.salmagundi.calendar.utils.CalendarUtils;

/**
 * Created by CaoJun on 2017/8/3.
 */

@Route(path = Constant.ACTIVITY_CALENDAR)
public class CalendarActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        final Button btnAccountList = (Button) findViewById(R.id.btnAccountList);
        final Button btnEventList = (Button) findViewById(R.id.btnEventList);

        btnAccountList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(Constant.ACTIVITY_CALENDAR_ACCOUNTLIST).navigation();
            }
        });

        btnEventList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(Constant.ACTIVITY_CALENDAR_EVENTLIST).navigation();
            }
        });

        Cursor cursor = CalendarUtils.getAccounts(this);
        if (cursor == null || cursor.getCount() < 1) {
            btnEventList.setEnabled(false);
        }
    }
}
