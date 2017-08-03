package org.caojun.salmagundi.calendar;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.socks.library.KLog;

import org.caojun.salmagundi.Constant;
import org.caojun.salmagundi.R;
import org.caojun.salmagundi.calendar.utils.CalendarUtils;

/**
 * 账号列表
 * Created by CaoJun on 2017/8/3.
 */

@Route(path = Constant.ACTIVITY_CALENDAR_ACCOUNTLIST)
public class AccountListActivity extends Activity {

    private final int RequestCode_AddAccount = 1;
    private final int RequestCode_EditAccount = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_add);

        final Button btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(Constant.ACTIVITY_CALENDAR_ACCOUNT).navigation(AccountListActivity.this, RequestCode_AddAccount);
            }
        });

        Cursor cursor = CalendarUtils.getAccounts(this);
        if (cursor == null || cursor.getCount() < 1) {
            btnAdd.callOnClick();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode_AddAccount && resultCode != Activity.RESULT_OK) {
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
