package org.caojun.salmagundi.passwordstore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.alibaba.android.arouter.facade.annotation.Route;

import org.caojun.salmagundi.BaseActivity;
import org.caojun.salmagundi.R;
import org.caojun.salmagundi.lockpattern.GestureConstant;
import org.caojun.salmagundi.lockpattern.GestureLoginActivity;
import org.caojun.salmagundi.passwordstore.adapter.PasswordAdapter;
import org.caojun.salmagundi.passwordstore.ormlite.Password;
import org.caojun.salmagundi.passwordstore.ormlite.PasswordDatabase;

import java.util.List;

/**
 * 密码仓库列表
 * Created by CaoJun on 2017/2/15.
 */

@Route(path = "/main/passwordstore")
public class PasswordStoreActivity extends BaseActivity {

    private ListView listView;
    private PasswordAdapter adapter;
    private List<Password> list;
    private Button btnAdd;
    private boolean isLogined = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_passwordstore);

        listView = (ListView) this.findViewById(R.id.lvPasswordStore);
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
        list = PasswordDatabase.getInstance(this).query();
        if(isNew) {
            adapter = new PasswordAdapter(this, list);
            listView.setAdapter(adapter);
        } else {
            adapter.setData(list);
            adapter.notifyDataSetChanged();
        }

        if(!isLogined) {
            Intent intent = new Intent(this, GestureLoginActivity.class);
            intent.putExtra(GestureConstant.HostGesture, this.getClass().getName());
            this.startActivityForResult(intent, GestureConstant.RequestCode_GestureLogin);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GestureConstant.RequestCode_GestureLogin) {
            if (resultCode == Activity.RESULT_OK) {
                isLogined = true;
            } else {
                this.finish();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void doUpdate(int position) {
        Password password = (Password) adapter.getItem(position);

        Bundle bundle = new Bundle();
        bundle.putSerializable("password", password);
        Intent intent = new Intent(this, PasswordDetailActivity.class);
        intent.putExtras(bundle);
        this.startActivity(intent);
    }

    private void doAdd() {
        this.startActivity(new Intent(this, PasswordDetailActivity.class));
    }
}
