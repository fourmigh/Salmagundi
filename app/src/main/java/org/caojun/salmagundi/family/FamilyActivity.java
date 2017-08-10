package org.caojun.salmagundi.family;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.socks.library.KLog;

import org.caojun.salmagundi.Constant;
import org.caojun.salmagundi.family.data.Person;
import org.caojun.salmagundi.family.data.Relation;
import org.caojun.salmagundi.family.utils.RelationUtils;

/**
 * Created by CaoJun on 2017/8/10.
 */

@Route(path = Constant.ACTIVITY_FAMILY)
public class FamilyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        KLog.d("father", Person.Father.getName());
        Relation r = RelationUtils.claim(Person.Father, true);
        KLog.d("father true", r.getName());
        r = RelationUtils.claim(Person.Father, false);
        KLog.d("father false", r.getName());
    }
}
