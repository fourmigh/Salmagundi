package org.caojun.salmagundi.string;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

import org.caojun.salmagundi.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CaoJun on 2017/8/18.
 */

@Route(path = Constant.ACTIVITY_STRINGSORT_DEMO)
public class StringSortDemo extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int[] ids = {1,2,3,4,5,6,7,8};
        String[] strings = {"ab12cd", "abcd", "查良镛", "曹珺", "abc曹珺", "123曹珺", "曹abc珺", "查"};

//        ArrayList<Integer> ids = new ArrayList<>();
//        ArrayList<String> strings = new ArrayList<>();
//        for (int i = 0;i < idss.length;i ++) {
//            ids.add(idss[i]);
//            strings.add(stringss[i]);
//        }

        ARouter.getInstance().build(Constant.ACTIVITY_STRINGSORT)
                .withObject("ids", ids)
                .withObject("strings", strings)
                .navigation(this, 0);
    }
}
