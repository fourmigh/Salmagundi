package org.caojun.bloodpressure.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioGroup;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import org.caojun.bloodpressure.Constant;
import org.caojun.bloodpressure.R;
import org.caojun.bloodpressure.adapter.BloodPressureAdapter;
import org.caojun.bloodpressure.ormlite.BloodPressure;
import org.caojun.bloodpressure.ormlite.BloodPressureDatabase;
import java.util.ArrayList;
import java.util.List;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by fourm on 2017/5/9.
 */

@Route(path = Constant.ACTIVITY_BLOODPRESSURE)
public class BloodPressureActivity extends AppCompatActivity {
    private StickyListHeadersListView listView;
    private BloodPressureAdapter adapter;
    private List<BloodPressure> list, listType;
    private Button btnAdd;
    private RadioGroup rgType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_bloodpressure);

        listView = (StickyListHeadersListView) this.findViewById(R.id.lvBloodPressure);
        btnAdd = (Button) this.findViewById(R.id.btnAdd);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                doUpdate(position);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAdd();
            }
        });

        rgType = (RadioGroup) findViewById(R.id.rgType);
        rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                showList(checkedId);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isNew = list == null || adapter == null;
        list = BloodPressureDatabase.getInstance(this).query();
        if(isNew) {
            adapter = new BloodPressureAdapter(this, list);
            listView.setAdapter(adapter);
        } else {
            adapter.setData(list);
            adapter.notifyDataSetChanged();
        }
        listView.setSelection(adapter.getCount() - 1);
        showList(rgType.getCheckedRadioButtonId());
    }

    private void doUpdate(int position) {
        BloodPressure bloodPressure = (BloodPressure) adapter.getItem(position);
        ARouter.getInstance().build(Constant.ACTIVITY_BLOODPRESSURE_DETAIL).withParcelable("bloodPressure", bloodPressure).navigation();
    }

    private void doAdd() {
        ARouter.getInstance().build(Constant.ACTIVITY_BLOODPRESSURE_DETAIL).navigation();
    }

    private void showList(int checkedId) {
        int type = -1;
        switch (checkedId) {
            case R.id.rbAll://全部
                break;
            case R.id.rbBloodPressure://血压
                type = BloodPressure.Type_BloodPressure;
                break;
            case R.id.rbMedicine://服药
                type = BloodPressure.Type_Medicine;
                break;
            case R.id.rbWeight://体重
                type = BloodPressure.Type_Weight;
                break;
            default:
                return;
        }
        if (listType == null) {
            listType = new ArrayList<>();
        }
        if (type >= 0) {
            listType.clear();
            for (int i = 0; i < list.size(); i++) {
                BloodPressure bloodPressure = list.get(i);
                if (bloodPressure.getType() == type) {
                    listType.add(bloodPressure);
                }
            }
            adapter.setData(listType);
        } else {
            adapter.setData(list);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menuToday) {
            ARouter.getInstance().build(Constant.ACTIVITY_TODAY).navigation();
            return true;
        }
        if (id == R.id.menuBMI) {
            ARouter.getInstance().build(Constant.ACTIVITY_BMI).navigation();
            return true;
        }
        if (id == R.id.menuNotificaiton) {
            ARouter.getInstance().build(Constant.ACTIVITY_NOTIFICATION_SETTINGS).navigation();
            return true;
        }
        if (id == R.id.menuData) {
            ARouter.getInstance().build(Constant.ACTIVITY_DATA).navigation();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
