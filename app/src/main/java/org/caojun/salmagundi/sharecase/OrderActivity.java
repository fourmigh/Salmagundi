package org.caojun.salmagundi.sharecase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import org.caojun.salmagundi.BaseActivity;
import org.caojun.salmagundi.Constant;
import org.caojun.salmagundi.R;
import org.caojun.salmagundi.sharecase.adapter.OrderAdapter;
import org.caojun.salmagundi.sharecase.ormlite.Order;
import org.caojun.salmagundi.sharecase.ormlite.OrderDatabase;
import org.caojun.salmagundi.sharecase.ormlite.SerializedList;
import org.caojun.salmagundi.sharecase.ormlite.User;
import java.util.List;

/**
 * 订单列表
 * Created by CaoJun on 2017/6/14.
 */

@Route(path = Constant.ACTIVITY_ORDER)
public class OrderActivity extends BaseActivity {

    private ListView listView;
    private OrderAdapter adapter;
    private List<Order> list;

    @Autowired
    protected User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_order);
        ARouter.getInstance().inject(this);

        listView = (ListView) this.findViewById(R.id.lvOrder);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDetail(position);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (user == null) {
            return;
        }
        boolean isNew = list == null || adapter == null;
        if (user.getType() == User.Type_Admin) {
            list = OrderDatabase.getInstance(this).query();
        } else {
            SerializedList<Integer> idOrders = user.getIdOrders();
            Integer[] ids = new Integer[idOrders.size()];
            for (int i = 0;i < idOrders.size();i ++) {
                ids[i] = idOrders.get(i);
            }
            list = OrderDatabase.getInstance(this).queryIn("id", ids);
        }
        if (isNew) {
            adapter = new OrderAdapter(this, list);
            listView.setAdapter(adapter);
        } else {
            adapter.setData(list);
            adapter.notifyDataSetChanged();
        }
    }

    private void showDetail(int position) {
        Order order = list.get(position);

        ARouter.getInstance().build(Constant.ACTIVITY_ORDER_DETAIL)
                .withParcelable("order", order)
                .withInt("size", list.size())
                .withParcelable("user", user)
                .withInt("position", position)
                .navigation(this, SharecaseConstant.RequestCode_ShowDetail);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SharecaseConstant.RequestCode_ShowDetail && resultCode == Activity.RESULT_OK && data != null) {
            int position = data.getIntExtra("position", -1);
            if (position >= 0 && position < list.size()) {
                showDetail(position);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
