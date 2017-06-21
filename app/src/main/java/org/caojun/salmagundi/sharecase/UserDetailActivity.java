package org.caojun.salmagundi.sharecase;

import android.app.Activity;
import android.content.Intent;
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
import org.caojun.salmagundi.lockpattern.GestureConstant;
import org.caojun.salmagundi.sharecase.ormlite.SerializedList;
import org.caojun.salmagundi.sharecase.ormlite.Sharecase;
import org.caojun.salmagundi.sharecase.ormlite.SharecaseDatabase;
import org.caojun.salmagundi.sharecase.ormlite.User;
import org.caojun.salmagundi.sharecase.ormlite.UserDatabase;
import org.caojun.salmagundi.sharecase.utils.UserUtils;
import org.caojun.salmagundi.string.ConvertUtils;
import java.util.List;

/**
 * 用户详情
 * Created by CaoJun on 2017/6/14.
 */

@Route(path = Constant.ACTIVITY_USER_DETAIL)
public class UserDetailActivity extends BaseActivity {

    private EditText etID, etName, etType, etIncome, etExpend;
    private TextInputLayout tilID, tilIncome, tilExpend;
    private Button btnSharecase, btnOrder, btnSave;

    @Autowired
    protected User user;

    @Autowired
    protected String hostGesture;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_user_detail);
        ARouter.getInstance().inject(this);

        etID = (EditText) findViewById(R.id.etID);
        etName = (EditText) findViewById(R.id.etName);
        etType = (EditText) findViewById(R.id.etType);
        etIncome = (EditText) findViewById(R.id.etIncome);
        etExpend = (EditText) findViewById(R.id.etExpend);
        tilID = (TextInputLayout) findViewById(R.id.tilID);
        tilIncome = (TextInputLayout) findViewById(R.id.tilIncome);
        tilExpend = (TextInputLayout) findViewById(R.id.tilExpend);
        btnSharecase = (Button) findViewById(R.id.btnSharecase);
        btnOrder = (Button) findViewById(R.id.btnOrder);
        btnSave = (Button) findViewById(R.id.btnSave);

        etName.addTextChangedListener(textWatcher);

        btnSharecase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSharecase();
            }
        });

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrder();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSave();
            }
        });

        String gesture = null;
        if (user != null) {
            gesture = ConvertUtils.stringToHex(user.getGesturePassword());
        }
        ARouter.getInstance().build(Constant.ACTIVITY_GESTURE_LOGIN)
                .withString("hostGesture", hostGesture)
                .withString("gesture", gesture).navigation(this, GestureConstant.RequestCode_GestureLogin);
    }

    @Override
    protected void onResume() {
        super.onResume();

        btnSharecase.setEnabled(true);
        btnOrder.setEnabled(true);

        String[] types = getResources().getStringArray(R.array.user_type);
        if (user == null) {
            //新建用户
            tilID.setVisibility(View.GONE);
            tilIncome.setVisibility(View.GONE);
            tilExpend.setVisibility(View.GONE);
            btnSharecase.setVisibility(View.GONE);
            btnOrder.setVisibility(View.GONE);

            etType.setText(types[UserUtils.isAdmin(this)? User.Type_Admin:User.Type_User]);
        } else {
            //已有用户
            etID.setText(String.valueOf(user.getId()));
            etName.setText(user.getName());
            etType.setText(types[user.getType()]);
            etIncome.setText(String.format("%1$.2f", user.getIncome()));
            etExpend.setText(String.format("%1$.2f", user.getExpend()));

//            SerializedList<Integer> idSharecases = user.getIdSharecases();
//            List<Sharecase> sharecaseList = SharecaseDatabase.getInstance(this).query("idHost", 0);
//            if (user.getType() == User.Type_User && (idSharecases == null || idSharecases.isEmpty()) && (sharecaseList == null || sharecaseList.isEmpty())) {
//                btnSharecase.setEnabled(false);
//            }

            List<Sharecase> listSharecase = SharecaseDatabase.getInstance(this).query();
            if (user.getType() == User.Type_User && (listSharecase == null || listSharecase.isEmpty())) {
                btnSharecase.setEnabled(false);
            }

            SerializedList<Integer> idOrders = user.getIdOrders();
            if (idOrders == null || idOrders.isEmpty()) {
                btnOrder.setEnabled(false);
            }
        }
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
        btnSave.setEnabled(canSave());
    }

    private boolean canSave() {
        String name = etName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            return false;
        }
        if (user != null && name.equals(user.getName())) {
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GestureConstant.RequestCode_GestureLogin) {
            if (resultCode != Activity.RESULT_OK || data == null) {
                this.finish();
                return;
            }
            byte[] gesturePassword = data.getByteArrayExtra("gesturePassword");
            if (gesturePassword == null) {
                this.finish();
                return;
            }
            if (user == null) {
                user = new User(UserUtils.isAdmin(this)? User.Type_Admin:User.Type_User, hostGesture, gesturePassword);
            } else {
                user.setGesturePassword(gesturePassword);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void doSave() {
        String name = etName.getText().toString();
        user.setName(name);
        if (UserDatabase.getInstance(this).insert(user) > 0) {
            finish();
        }
    }

    private void showSharecase() {
        ARouter.getInstance().build(Constant.ACTIVITY_SHARECASE).withSerializable("user", user).navigation();
    }

    private void showOrder() {

    }
}
