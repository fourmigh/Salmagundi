package org.caojun.salmagundi.today;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.socks.library.KLog;

import org.caojun.salmagundi.BaseActivity;
import org.caojun.salmagundi.Constant;

/**
 * Created by CaoJun on 2017/7/10.
 */

@Route(path = Constant.ACTIVITY_TODAY)
public class TodayActivity extends BaseActivity {

    private GetBroadcastReceiver mGetBroadcastReceiver;
    private Intent mIntent;

    public class GetBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra("result");
            KLog.json("onReceive", result);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGetBroadcastReceiver = new GetBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(TodayConstant.BroadcastAction);
        registerReceiver(mGetBroadcastReceiver, intentFilter);

        startGetService();
    }

    @Override
    protected void onDestroy() {
        stopService(mIntent);
        unregisterReceiver(mGetBroadcastReceiver);
        super.onDestroy();
    }

    private void startGetService() {
        mIntent = new Intent(this, GetService.class);
        mIntent.putExtra("month", "7");
        mIntent.putExtra("day", "10");
        startService(mIntent);
    }
}
