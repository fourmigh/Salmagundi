package org.caojun.salmagundi.sharecase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import org.caojun.salmagundi.BaseActivity;
import org.caojun.salmagundi.Constant;
import org.caojun.salmagundi.R;
import org.caojun.salmagundi.sharecase.ormlite.Order;
import org.caojun.salmagundi.sharecase.ormlite.Sharecase;
import org.caojun.salmagundi.sharecase.ormlite.SharecaseDatabase;
import org.caojun.salmagundi.sharecase.ormlite.User;
import org.caojun.salmagundi.sharecase.utils.OrderUtils;
import org.caojun.salmagundi.sharecase.utils.SharecaseUtils;
import org.caojun.salmagundi.sharecase.utils.UserUtils;

/**
 * 共享箱详情
 * Created by CaoJun on 2017/6/14.
 */

@Route(path = Constant.ACTIVITY_SHARECASE_DETAIL)
public class SharecaseDetailActivity extends BaseActivity {

    private EditText etID, etName, etRent, etDeposit, etCommission, etHost;
    private Button btnSave, btnSubmit, btnBorrow, btnUnshelve;
    private TextInputLayout tilID, tilName, tilRent, tilDeposit, tilHost;

    @Autowired
    protected User user;

    @Autowired
    protected Sharecase sharecase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_sharecase_detail);
        ARouter.getInstance().inject(this);

        etID = (EditText) findViewById(R.id.etID);
        etName = (EditText) findViewById(R.id.etName);
        etRent = (EditText) findViewById(R.id.etRent);
        etDeposit = (EditText) findViewById(R.id.etDeposit);
        etCommission = (EditText) findViewById(R.id.etCommission);
        etHost = (EditText) findViewById(R.id.etHost);
        tilID = (TextInputLayout) findViewById(R.id.tilID);
        tilName = (TextInputLayout) findViewById(R.id.tilName);
        tilRent = (TextInputLayout) findViewById(R.id.tilRent);
        tilDeposit = (TextInputLayout) findViewById(R.id.tilDeposit);
        tilHost = (TextInputLayout) findViewById(R.id.tilHost);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnBorrow = (Button) findViewById(R.id.btnBorrow);
        btnUnshelve = (Button) findViewById(R.id.btnUnshelve);

        etName.addTextChangedListener(textWatcher);
        etRent.addTextChangedListener(textWatcher);
        etDeposit.addTextChangedListener(textWatcher);
        etCommission.addTextChangedListener(textWatcher);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSave();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSubmit();
            }
        });

        btnBorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doBorrow();
            }
        });

        btnUnshelve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doUnshelve();
            }
        });
    }

    private void doSubmit() {
        doSave();
    }

    private void doBorrow() {
        if (user == null) {
            return;
        }
        //1、生成订单
        Order order = OrderUtils.loan(this, sharecase, sharecase.getIdHost(), sharecase.getName(), sharecase.getRent(), sharecase.getDeposit(), user.getId());
        if (order == null) {
            return;
        }
        //2、物品所有人/使用人，增加相关订单
        User host = UserUtils.getUser(this, sharecase.getIdHost());
        if (host == null) {
            return;
        }
        host = UserUtils.addOrderId(this, host, order);
        if (host == null) {
            return;
        }
        user = UserUtils.addOrderId(this, user, order);
        if (user == null) {
            return;
        }
        if (!UserUtils.borrow(this, user, order)) {
            return;
        }
        //3、清空共享箱
        sharecase = SharecaseUtils.recycle(this, sharecase);
        if (sharecase == null) {
            return;
        }
        finish();
    }

    private void doUnshelve() {
        if (sharecase == null || user == null || user.getId() != sharecase.getIdHost()) {
            return;
        }
        sharecase = SharecaseUtils.recycle(this, sharecase);
        if (sharecase != null) {
            finish();
        }
    }

    private void doSave() {
        String strCommission = etCommission.getText().toString();
        float commission = Float.valueOf(strCommission);
        if (sharecase == null) {
            //共享箱所有人新建共享箱
            Sharecase sharecase = SharecaseDatabase.getInstance(this).insert(new Sharecase(user.getId(), commission));
            if (sharecase != null) {
                finish();
            }
        } else {
            if (user.getId() == sharecase.getIdHost() || sharecase.getIdHost() == 0) {
                //物品所有人
                String name = etName.getText().toString();
                String strRent = etRent.getText().toString();
                float rent = Float.valueOf(strRent);
                String strDeposit = etDeposit.getText().toString();
                float deposit = Float.valueOf(strDeposit);
                sharecase.setName(name);
                sharecase.setRent(rent);
                sharecase.setDeposit(deposit);
                if (sharecase.getIdHost() == 0) {
                    sharecase.setIdHost(user.getId());
                }
            } else if (user.getType() == User.Type_Admin) {
                //共享箱所有人
                sharecase.setCommission(commission);
            } else {
                return;
            }
            sharecase = SharecaseDatabase.getInstance(this).update(sharecase);
            if (sharecase != null) {
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sharecase == null) {
            tilID.setVisibility(View.GONE);
            tilName.setVisibility(View.GONE);
            tilRent.setVisibility(View.GONE);
            tilDeposit.setVisibility(View.GONE);
            tilHost.setVisibility(View.GONE);
            etCommission.setEnabled(true);

            btnSave.setVisibility(View.VISIBLE);
            btnSubmit.setVisibility(View.GONE);
            btnBorrow.setVisibility(View.GONE);
            btnUnshelve.setVisibility(View.GONE);
        } else {
            tilID.setVisibility(View.VISIBLE);
            tilName.setVisibility(View.VISIBLE);
            tilRent.setVisibility(View.VISIBLE);
            tilDeposit.setVisibility(View.VISIBLE);
            tilHost.setVisibility(View.VISIBLE);
            if (user.getId() == sharecase.getIdHost() || sharecase.getIdHost() == 0) {
                //物品所有人
                etName.setEnabled(true);
                etRent.setEnabled(true);
                etDeposit.setEnabled(true);
                etCommission.setEnabled(user.getType() == User.Type_Admin);
            } else {
                etName.setEnabled(false);
                etRent.setEnabled(false);
                etDeposit.setEnabled(false);
                etCommission.setEnabled(false);
            }

            etID.setText(String.valueOf(sharecase.getId()));
            etName.setText(sharecase.getName());
            etRent.setText(String.format("%1$.2f", sharecase.getRent()));
            etDeposit.setText(String.format("%1$.2f", sharecase.getDeposit()));
            etCommission.setText(String.format("%1$.2f", sharecase.getCommission()));

            User host = UserUtils.getUser(this, sharecase.getIdHost());
            etHost.setText(host == null?null:host.getName());

            if (sharecase.getIdHost() == 0) {
                //空共享箱
                btnSubmit.setVisibility(View.VISIBLE);
                btnBorrow.setVisibility(View.GONE);
                btnUnshelve.setVisibility(View.GONE);

                if (user.getType() == User.Type_Admin) {
                    btnSave.setVisibility(View.VISIBLE);
                } else {
                    btnSave.setVisibility(View.GONE);
                }
            } else if (user.getId() == sharecase.getIdHost()) {
                //物品所有人
                btnSave.setVisibility(View.VISIBLE);
                btnSubmit.setVisibility(View.GONE);
                btnBorrow.setVisibility(View.GONE);
                btnUnshelve.setVisibility(View.VISIBLE);
            } else {
                //可租用物品共享箱
                btnSave.setVisibility(View.GONE);
                btnSubmit.setVisibility(View.GONE);
                btnBorrow.setVisibility(View.VISIBLE);
                btnUnshelve.setVisibility(View.GONE);
            }
        }

//        List<Order> orders = OrderDatabase.getInstance(this).query();
//        if (orders == null || orders.isEmpty()) {
//            btnOrder.setEnabled(false);
//        } else {
//            btnOrder.setEnabled(true);
//        }

        doCheckSaveButton();
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            doCheckSaveButton();
        }
    };

    private void doCheckSaveButton() {
        boolean canSave = canSave();
        btnSave.setEnabled(canSave);
        btnSubmit.setEnabled(canSave);
    }

    private boolean canSave() {
        if (user == null) {
            return false;
        }
        if (sharecase == null && user.getType() != User.Type_Admin) {
            return false;
        }
        if (sharecase != null && sharecase.getIdHost() != null) {
            //共享箱已有物品，但还未出租
            User host = UserUtils.getUser(this, sharecase.getIdHost());
            if (host != null && host.getId() != user.getId()) {
                //非物品所有人
                return false;
            }
        }
        String strCommission = etCommission.getText().toString();
        if (TextUtils.isEmpty(strCommission)) {
            return false;
        }
        try {
            Float.valueOf(strCommission);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (sharecase == null) {
            //共享箱所有人新建共享箱
            return true;
        } else {
            String name = etName.getText().toString();
            if (TextUtils.isEmpty(name)) {
                return false;
            }

            String strRent = etRent.getText().toString();
            if (!TextUtils.isEmpty(strRent)) {
                try {
                    float rent = Float.valueOf(strRent);
                    if (rent != sharecase.getRent()) {
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

            String strDeposit = etDeposit.getText().toString();
            if (!TextUtils.isEmpty(strDeposit)) {
                try {
                    float deposit = Float.valueOf(strDeposit);
                    if (deposit != sharecase.getDeposit()) {
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

            if (!TextUtils.isEmpty(sharecase.getName()) && !sharecase.getName().equals(name)) {
                return true;
            }

            float commission = Float.valueOf(strCommission);
            if (commission != sharecase.getCommission()) {
                return true;
            }

            return false;
        }
    }
}
