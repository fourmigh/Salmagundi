package org.caojun.salmagundi.passwordstore;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import org.caojun.salmagundi.BaseActivity;
import org.caojun.salmagundi.Constant;
import org.caojun.salmagundi.R;
import org.caojun.salmagundi.passwordstore.adapter.PasswordAdapter;
import org.caojun.salmagundi.passwordstore.ormlite.Password;
import org.caojun.salmagundi.passwordstore.ormlite.PasswordDatabase;
import java.util.List;

/**
 * 密码仓库列表
 * Created by CaoJun on 2017/2/15.
 */

@Route(path = Constant.ACTIVITY_PASSWORDSTORE, extras = Constant.EXTRAS_LOGIN)
public class PasswordStoreActivity extends BaseActivity {

    private ListView listView;
    private PasswordAdapter adapter;
    private List<Password> list;
    private Button btnAdd;

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
        if (isNew) {
            adapter = new PasswordAdapter(this, list);
            listView.setAdapter(adapter);
        } else {
            adapter.setData(list);
            adapter.notifyDataSetChanged();
        }
    }

    private void doUpdate(int position) {
        Password password = (Password) adapter.getItem(position);

        ARouter.getInstance().build(Constant.ACTIVITY_PASSWORDSTORE_DETAIL).withParcelable("password", password).navigation();
    }

    private void doAdd() {
        this.startActivity(new Intent(this, PasswordDetailActivity.class));
    }
}
