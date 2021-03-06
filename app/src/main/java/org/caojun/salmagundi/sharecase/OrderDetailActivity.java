package org.caojun.salmagundi.sharecase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import org.caojun.salmagundi.Constant;
import org.caojun.salmagundi.R;
import org.caojun.salmagundi.sharecase.ormlite.Order;
import org.caojun.salmagundi.sharecase.ormlite.User;
import org.caojun.salmagundi.sharecase.utils.UserUtils;
import org.caojun.salmagundi.utils.TimeUtils;

/**
 * 订单详情
 * Created by CaoJun on 2017/6/14.
 */

@Route(path = Constant.ACTIVITY_ORDER_DETAIL)
public class OrderDetailActivity extends Activity {

    private EditText etID, etName, etSharecaseID, etHost, etUser, etRent, etDeposit, etCommission, etStart, etEnd, etDays;
    private Button btnRevious, btnNext, btnRestore;

    @Autowired
    protected int position;

    @Autowired
    protected int size;

    @Autowired
    protected Order order;

    @Autowired
    protected User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_order_detail);
        ARouter.getInstance().inject(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        etID = (EditText) findViewById(R.id.etID);
        etName = (EditText) findViewById(R.id.etName);
        etSharecaseID = (EditText) findViewById(R.id.etSharecaseID);
        etHost = (EditText) findViewById(R.id.etHost);
        etUser = (EditText) findViewById(R.id.etUser);
        etRent = (EditText) findViewById(R.id.etRent);
        etDeposit = (EditText) findViewById(R.id.etDeposit);
        etCommission = (EditText) findViewById(R.id.etCommission);
        etStart = (EditText) findViewById(R.id.etStart);
        etEnd = (EditText) findViewById(R.id.etEnd);
        etDays = (EditText) findViewById(R.id.etDays);
        btnRevious = (Button) findViewById(R.id.btnRevious);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnRestore = (Button) findViewById(R.id.btnRestore);

        btnRevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRevious();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doNext();
            }
        });

        btnRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRestore();
            }
        });

        btnRevious.setEnabled(position > 0);
        btnNext.setEnabled(position < size - 1);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (order.getIdUser() == user.getId() && order.getTimeEnd() == 0) {
            btnRestore.setVisibility(View.VISIBLE);
        } else {
            btnRestore.setVisibility(View.GONE);
        }

        etID.setText(String.valueOf(order.getId()));
        etName.setText(order.getName());
        etSharecaseID.setText(String.valueOf(order.getIdSharecase()));
        User host = UserUtils.getUser(this, order.getIdHost());
        if (host != null) {
            etHost.setText(String.format("%s(%s)", host.getName(), host.getId()));
        }
        User user = UserUtils.getUser(this, order.getIdUser());
        if (user != null) {
            etUser.setText(String.format("%s(%s)", user.getName(), user.getId()));
        }
        etRent.setText(String.format("%1$.2f", order.getRent()));
        etDeposit.setText(String.format("%1$.2f", order.getDeposit()));
        etCommission.setText(String.valueOf(order.getCommission()));
        etStart.setText(TimeUtils.getTime("yyyy/MM/dd HH:mm:ss", order.getTimeStart()));
        if (order.getTimeEnd() > 0) {
            etEnd.setText(TimeUtils.getTime("yyyy/MM/dd HH:mm:ss", order.getTimeEnd()));
        }
        if (order.getDays() >= 0) {
            etDays.setText(String.valueOf(order.getDays()));
        } else {
            long days = TimeUtils.getDays(TimeUtils.getTime(), order.getTimeStart());
            etDays.setText(String.valueOf(days));
        }
    }

    private void doRestore() {
        ARouter.getInstance().build(Constant.ACTIVITY_SHARECASE)
                .withParcelable("user", user)
                .withParcelable("order", order)
                .navigation(this, SharecaseConstant.RequestCode_Restore);
    }

    private void doRevious() {
        position --;
        showDetial();
    }

    private void doNext() {
        position ++;
        showDetial();
    }

    private void showDetial() {
        Intent intent = new Intent();
        intent.putExtra("position", position);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == SharecaseConstant.RequestCode_Restore && data != null) {
            user = data.getParcelableExtra("user");
            order = data.getParcelableExtra("order");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("user", (Parcelable) user);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
