package org.caojun.salmagundi.sharecase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import org.caojun.salmagundi.sharecase.ormlite.Order;
import org.caojun.salmagundi.sharecase.ormlite.OrderDatabase;
import org.caojun.salmagundi.sharecase.ormlite.Sharecase;
import org.caojun.salmagundi.sharecase.ormlite.SharecaseDatabase;
import org.caojun.salmagundi.sharecase.ormlite.User;
import org.caojun.salmagundi.sharecase.utils.UserUtils;
import org.caojun.salmagundi.utils.TimeUtils;

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
    protected User user;//用户选择共享箱出租（包括共享箱所有人管理共享箱）

    @Autowired
    protected Order order;//物品使用人选择空余共享箱归还物品

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
                if (order == null && user != null) {
                    doUpdate(position);
                } else if (order != null && user != null) {
                    doSelect(position);
                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAdd();
            }
        });

        if (order == null && user != null) {
            btnAdd.setVisibility(user.getType() == User.Type_Admin ? View.VISIBLE : View.GONE);
        } else if (order != null && user != null) {
            btnAdd.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isNew = list == null || adapter == null;
        if (order != null) {
            list = SharecaseDatabase.getInstance(this).query("idHost", 0);
        } else if (user != null) {
            list = SharecaseDatabase.getInstance(this).query();
        }
        if (isNew) {
            adapter = new SharecaseAdapter(this, list);
            listView.setAdapter(adapter);
        } else {
            adapter.setData(list);
            adapter.notifyDataSetChanged();
        }
    }

    private void doSelect(int position) {
        //选中后归还物品
        long timeEnd = TimeUtils.getTime();
        order.setTimeEnd(timeEnd);
        order = OrderDatabase.getInstance(this).update(order);
        if (order == null) {
            return;
        }
//        UserUtils.restore(this, order);
        //TODO
        finish();
    }

    private void doUpdate(int position) {
        Sharecase sharecase = (Sharecase) adapter.getItem(position);

        ARouter.getInstance().build(Constant.ACTIVITY_SHARECASE_DETAIL)
                .withSerializable("user", user)
                .withParcelable("sharecase", sharecase)
                .navigation(this, SharecaseConstant.RequestCode_TransferUser);
    }

    private void doAdd() {
        ARouter.getInstance().build(Constant.ACTIVITY_SHARECASE_DETAIL).withSerializable("user", user).navigation();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SharecaseConstant.RequestCode_TransferUser && resultCode == Activity.RESULT_OK && data != null) {
            user = data.getParcelableExtra("user");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        if (user != null) {
            intent.putExtra("user", (Parcelable) user);
        }
        if (order != null) {
            intent.putExtra("order", (Parcelable) order);
        }
        setResult(Activity.RESULT_OK, intent);
        super.finish();
    }
}
