package org.caojun.salmagundi.sharecase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import org.caojun.salmagundi.BaseActivity;
import org.caojun.salmagundi.Constant;
import org.caojun.salmagundi.R;
import org.caojun.salmagundi.sharecase.adapter.SharecaseAdapter;
import org.caojun.salmagundi.sharecase.ormlite.Sharecase;
import org.caojun.salmagundi.sharecase.ormlite.SharecaseDatabase;
import org.caojun.salmagundi.sharecase.ormlite.User;
import org.caojun.salmagundi.sharecase.utils.SharecaseUtils;

import java.util.List;

/**
 * 共享箱模拟
 * Created by CaoJun on 2017/6/14.
 */

@Route(path = Constant.ACTIVITY_SHARECASE)
public class SharecaseActivity extends BaseActivity {

    private ListView listView;
    private SharecaseAdapter adapter;
    private List<Sharecase> list;
    private Button btnAdd;

    @Autowired
    protected User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_sharecase);
        ARouter.getInstance().inject(this);

        listView = (ListView) this.findViewById(R.id.lvSharecase);
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
        list = SharecaseDatabase.getInstance(this).query();
        if (isNew) {
            adapter = new SharecaseAdapter(this, list);
            listView.setAdapter(adapter);
        } else {
            adapter.setData(list);
            adapter.notifyDataSetChanged();
        }
    }

    private void doUpdate(int position) {
        Sharecase sharecase = (Sharecase) adapter.getItem(position);

        ARouter.getInstance().build(Constant.ACTIVITY_SHARECASE_DETAIL)
                .withSerializable("user", user)
                .withParcelable("sharecase", sharecase)
                .navigation();
    }

    private void doAdd() {

        ARouter.getInstance().build(Constant.ACTIVITY_SHARECASE_DETAIL).withSerializable("user", user).navigation();
    }
}
