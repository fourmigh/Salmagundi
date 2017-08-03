package org.caojun.salmagundi.calendar;

import android.app.Activity;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;

import org.caojun.salmagundi.Constant;

/**
 * 事件详情
 * Created by CaoJun on 2017/8/3.
 */

@Route(path = Constant.ACTIVITY_CALENDAR_EVENT)
public class EventActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
