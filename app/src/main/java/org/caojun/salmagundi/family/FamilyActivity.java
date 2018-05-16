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

//        KLog.d("Father", Person.Father.getName());
//        Relation r = RelationUtils.claim(Person.Father, true);
//        KLog.d("Father true", r.getName());
//        r = RelationUtils.claim(Person.Father, false);
//        KLog.d("Father false", r.getName());
//        r = RelationUtils.claim(Person.Mother, true);
//        KLog.d("Mother true", r.getName());
//        r = RelationUtils.claim(Person.Mother, false);
//        KLog.d("Mother false", r.getName());
//        r = RelationUtils.claim(Person.Son, true);
//        KLog.d("Son true", r.getName());
//        r = RelationUtils.claim(Person.Son, false);
//        KLog.d("Son false", r.getName());
//        r = RelationUtils.claim(Person.Daughter, true);
//        KLog.d("Daughter true", r.getName());
//        r = RelationUtils.claim(Person.Daughter, false);
//        KLog.d("Daughter false", r.getName());
//        r = RelationUtils.claim(Person.EBrother, true);
//        KLog.d("EBrother true", r.getName());
//        Relation r = RelationUtils.claim(Person.EBrother, false);
//        KLog.d("EBrother false", r.getName());
//        Relation r = RelationUtils.claim(Person.YBrother, true);
//        KLog.d("YBrother true", r.getName());
//        Relation r = RelationUtils.claim(Person.YBrother, false);
//        KLog.d("YBrother false", r.getName());
//        Relation r = RelationUtils.claim(Person.ESister, true);
//        KLog.d("ESister true", r.getName());
//        Relation r = RelationUtils.claim(Person.ESister, false);
//        KLog.d("ESister false", r.getName());
        Relation r = RelationUtils.claim(Person.YSister, true);
        KLog.d("YSister true", r.getName());
        r = RelationUtils.claim(Person.YSister, false);
        KLog.d("YSister false", r.getName());
        r = RelationUtils.claim(Person.Husband, true);
        KLog.d("Husband true", r.getName());
        r = RelationUtils.claim(Person.Husband, false);
        KLog.d("Husband false", r.getName());
        r = RelationUtils.claim(Person.Wife, true);
        KLog.d("Wife true", r.getName());
        r = RelationUtils.claim(Person.Wife, false);
        KLog.d("Wife false", r.getName());
    }
}
