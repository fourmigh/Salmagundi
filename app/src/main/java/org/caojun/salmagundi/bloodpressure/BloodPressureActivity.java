package org.caojun.salmagundi.bloodpressure;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import org.caojun.salmagundi.BaseActivity;
import org.caojun.salmagundi.R;
import org.caojun.salmagundi.bloodpressure.adapter.BloodPressureAdapter;
import org.caojun.salmagundi.bloodpressure.ormlite.BloodPressure;
import org.caojun.salmagundi.bloodpressure.ormlite.BloodPressureDatabase;
import org.caojun.salmagundi.lockpattern.GestureConstant;
import org.caojun.salmagundi.lockpattern.GestureLoginActivity;
import org.caojun.salmagundi.passwordstore.PasswordDetailActivity;
import org.caojun.salmagundi.passwordstore.adapter.PasswordAdapter;
import org.caojun.salmagundi.passwordstore.ormlite.Password;
import org.caojun.salmagundi.passwordstore.ormlite.PasswordDatabase;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by fourm on 2017/5/9.
 */

public class BloodPressureActivity extends BaseActivity {
    private StickyListHeadersListView listView;
    private BloodPressureAdapter adapter;
    private List<BloodPressure> list;
    private Button btnAdd;

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
    }

    private void doUpdate(int position) {
        BloodPressure bloodPressure = (BloodPressure) adapter.getItem(position);

        Bundle bundle = new Bundle();
        bundle.putSerializable("bloodPressure", bloodPressure);
        Intent intent = new Intent(this, BloodPressureDetailActivity.class);
        intent.putExtras(bundle);
        this.startActivity(intent);
    }

    private void doAdd() {
        this.startActivity(new Intent(this, BloodPressureDetailActivity.class));
    }
}
