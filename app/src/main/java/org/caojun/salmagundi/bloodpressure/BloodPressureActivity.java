package org.caojun.salmagundi.bloodpressure;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioGroup;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import org.caojun.salmagundi.Constant;
import org.caojun.salmagundi.R;
import org.caojun.salmagundi.bloodpressure.adapter.BloodPressureAdapter;
import org.caojun.salmagundi.bloodpressure.ormlite.BloodPressure;
import org.caojun.salmagundi.bloodpressure.ormlite.BloodPressureDatabase;
import java.util.ArrayList;
import java.util.List;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by fourm on 2017/5/9.
 */

@Route(path = Constant.ACTIVITY_BLOODPRESSURE)
public class BloodPressureActivity extends Activity {
    private StickyListHeadersListView listView;
    private BloodPressureAdapter adapter;
    private List<BloodPressure> list, listType;
    private Button btnAdd, btnExport;
    private RadioGroup rgType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_bloodpressure);

        listView = (StickyListHeadersListView) this.findViewById(R.id.lvBloodPressure);
        btnAdd = (Button) this.findViewById(R.id.btnAdd);
        btnExport = (Button) findViewById(R.id.btnExport);

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

        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BloodPressureUtils.exportFromDB(BloodPressureActivity.this)) {
                    btnExport.setEnabled(false);
                }
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

//        Bundle bundle = new Bundle();
//        bundle.putSerializable("bloodPressure", bloodPressure);
//        Intent intent = new Intent(this, BloodPressureDetailActivity.class);
//        intent.putExtras(bundle);
//        this.startActivity(intent);
        ARouter.getInstance().build(Constant.ACTIVITY_BLOODPRESSURE_DETAIL).withParcelable("bloodPressure", bloodPressure).navigation();
    }

    private void doAdd() {
//        this.startActivity(new Intent(this, BloodPressureDetailActivity.class));
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
}
