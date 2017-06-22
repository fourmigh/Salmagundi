package org.caojun.salmagundi.sharecase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import org.caojun.salmagundi.BaseActivity;
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
public class OrderDetailActivity extends BaseActivity {

    private EditText etID, etName, etSharecaseID, etHostID, etUserID, etRent, etDeposit, etCommission, etStart, etEnd;
    private Button btnRevious, btnNext;

    @Autowired
    protected int position;

    @Autowired
    protected int size;

    @Autowired
    protected Order order;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_order_detail);
        ARouter.getInstance().inject(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        etID = (EditText) findViewById(R.id.etID);
        etName = (EditText) findViewById(R.id.etName);
        etSharecaseID = (EditText) findViewById(R.id.etSharecaseID);
        etHostID = (EditText) findViewById(R.id.etHostID);
        etUserID = (EditText) findViewById(R.id.etUserID);
        etRent = (EditText) findViewById(R.id.etRent);
        etDeposit = (EditText) findViewById(R.id.etDeposit);
        etCommission = (EditText) findViewById(R.id.etCommission);
        etStart = (EditText) findViewById(R.id.etStart);
        etEnd = (EditText) findViewById(R.id.etEnd);
        btnRevious = (Button) findViewById(R.id.btnRevious);
        btnNext = (Button) findViewById(R.id.btnNext);

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

        btnRevious.setEnabled(position > 0);
        btnNext.setEnabled(position < size - 1);

        if (position < 0) {
            position = 0;
            return;
        }
        if (position >= size) {
            position = size - 1;
            return;
        }

        etID.setText(String.valueOf(order.getId()));
        etName.setText(order.getName());
        etSharecaseID.setText(String.valueOf(order.getIdSharecase()));
        User host = UserUtils.getUser(this, order.getIdHost());
        if (host != null) {
            etHostID.setText(String.format("%s(%s)", host.getName(), host.getId()));
        }
        User user = UserUtils.getUser(this, order.getIdUser());
        if (user != null) {
            etUserID.setText(String.format("%s(%s)", user.getName(), user.getId()));
        }
        etRent.setText(String.format("%1$.2f", order.getRent()));
        etDeposit.setText(String.format("%1$.2f", order.getDeposit()));
        etCommission.setText(String.format("%1$.2f", order.getCommission()));
        etStart.setText(TimeUtils.getTime("yyyy/MM/dd HH:mm:ss", order.getTimeStart()));
        if (order.getTimeEnd() > 0) {
            etEnd.setText(TimeUtils.getTime("yyyy/MM/dd HH:mm:ss", order.getTimeEnd()));
        }
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
}
