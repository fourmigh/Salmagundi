package org.caojun.salmagundi.calendar;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import org.caojun.salmagundi.Constant;
import org.caojun.salmagundi.R;
import org.caojun.salmagundi.calendar.adapter.AccountAdapter;
import org.caojun.salmagundi.calendar.utils.CalendarUtils;

/**
 * 账号列表
 * Created by CaoJun on 2017/8/3.
 */

@Route(path = Constant.ACTIVITY_CALENDAR_ACCOUNTLIST)
public class AccountListActivity extends AppCompatActivity {

    private final int RequestCode_AddAccount = 1;
    private final int RequestCode_EditAccount = 2;

    private Button btnAdd;
    private ListView listView;
    private AccountAdapter adapter;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_add);

        listView = (ListView) findViewById(R.id.listView);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(Constant.ACTIVITY_CALENDAR_ACCOUNT).navigation(AccountListActivity.this, RequestCode_AddAccount);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isNew = cursor == null || adapter == null;
        cursor = CalendarUtils.getAccounts(this);
        if (cursor == null || cursor.getCount() < 1) {
            btnAdd.callOnClick();
            return;
        }
        if(isNew) {
            adapter = new AccountAdapter(this, cursor);
            listView.setAdapter(adapter);
        } else {
            adapter.setData(cursor);
            adapter.notifyDataSetChanged();
        }
        listView.setSelection(adapter.getCount() - 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode_AddAccount && resultCode != Activity.RESULT_OK) {
            cursor = CalendarUtils.getAccounts(this);
            if (cursor == null || cursor.getCount() < 1) {
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
