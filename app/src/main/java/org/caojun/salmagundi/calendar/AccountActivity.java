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
 * 账号详情
 * Created by CaoJun on 2017/8/3.
 */

@Route(path = Constant.ACTIVITY_CALENDAR_ACCOUNT)
public class AccountActivity extends AppCompatActivity {

    private EditText etAccountName, etAccountType;

    @Autowired
    protected ContentValues contentValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_account);
        ARouter.getInstance().inject(this);

        etAccountName = (EditText) findViewById(R.id.etAccountName);
        etAccountType = (EditText) findViewById(R.id.etAccountType);

        if (contentValues != null) {
            etAccountName.setText(contentValues.getAsString(CalendarContract.Calendars.NAME));
            etAccountType.setText(contentValues.getAsString(CalendarContract.Calendars.ACCOUNT_TYPE));
        }
    }
}
