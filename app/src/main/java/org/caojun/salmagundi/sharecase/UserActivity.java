package org.caojun.salmagundi.sharecase;

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
import org.caojun.salmagundi.sharecase.adapter.UserAdapter;
import org.caojun.salmagundi.sharecase.ormlite.User;
import org.caojun.salmagundi.sharecase.ormlite.UserDatabase;
import org.caojun.salmagundi.utils.TimeUtils;

import java.util.List;

/**
 * 用户列表
 * Created by CaoJun on 2017/6/14.
 */

@Route(path = Constant.ACTIVITY_USER)
public class UserActivity extends BaseActivity {
    private ListView listView;
    private UserAdapter adapter;
    private List<User> list;
    private Button btnAdd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_user);

        listView = (ListView) this.findViewById(R.id.lvUser);
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
        list = UserDatabase.getInstance(this).query();
        if (isNew) {
            adapter = new UserAdapter(this, list);
            listView.setAdapter(adapter);
        } else {
            adapter.setData(list);
            adapter.notifyDataSetChanged();
        }
    }

    private void doUpdate(int position) {
        User user = (User) adapter.getItem(position);

        ARouter.getInstance().build(Constant.ACTIVITY_USER_DETAIL).withParcelable("user", user).navigation();
    }

    private void doAdd() {
        long time = TimeUtils.getTime();
        String hostGesture = String.valueOf(time);
        ARouter.getInstance().build(Constant.ACTIVITY_USER_DETAIL).withString("hostGesture", hostGesture).navigation();
    }
}
