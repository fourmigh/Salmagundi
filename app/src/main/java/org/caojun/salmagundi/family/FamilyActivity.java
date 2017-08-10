package org.caojun.salmagundi.family;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.alibaba.android.arouter.facade.annotation.Route;
import org.caojun.salmagundi.Constant;

/**
 * Created by CaoJun on 2017/8/10.
 */

@Route(path = Constant.ACTIVITY_FAMILY)
public class FamilyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
