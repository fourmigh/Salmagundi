package org.caojun.salmagundi.calendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

import org.caojun.salmagundi.Constant;
import org.caojun.salmagundi.R;

/**
 * 账号列表
 * Created by CaoJun on 2017/8/3.
 */

@Route(path = Constant.ACTIVITY_CALENDAR_ACCOUNTLIST)
public class AccountListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_add);
    }

//    public void onClickAdd(View view) {
//        ARouter.getInstance().build(Constant.ACTIVITY_CALENDAR_ACCOUNT).navigation();
//    }
}
