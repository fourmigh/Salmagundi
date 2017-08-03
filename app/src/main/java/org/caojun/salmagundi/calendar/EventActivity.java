package org.caojun.salmagundi.calendar;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

import org.caojun.salmagundi.Constant;
import org.caojun.salmagundi.R;

/**
 * 事件详情
 * Created by CaoJun on 2017/8/3.
 */

@Route(path = Constant.ACTIVITY_CALENDAR_EVENT)
public class EventActivity extends AppCompatActivity {

    private EditText etEventTitle, etEventDescription;

    @Autowired
    protected ContentValues contentValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_event);
        ARouter.getInstance().inject(this);

        etEventTitle = (EditText) findViewById(R.id.etEventTitle);
        etEventDescription = (EditText) findViewById(R.id.etEventDescription);

        if (contentValues != null) {
            etEventTitle.setText(contentValues.getAsString(CalendarContract.EventsEntity.TITLE));
            etEventDescription.setText(contentValues.getAsString(CalendarContract.EventsEntity.DESCRIPTION));
        }
    }
}
