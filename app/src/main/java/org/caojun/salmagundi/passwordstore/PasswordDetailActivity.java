package org.caojun.salmagundi.passwordstore;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.maksim88.passwordedittext.PasswordEditText;
import org.caojun.salmagundi.Constant;
import org.caojun.salmagundi.R;
import org.caojun.salmagundi.passwordstore.ormlite.Password;
import org.caojun.salmagundi.passwordstore.ormlite.PasswordDatabase;
import org.caojun.salmagundi.passwordstore.ormlite.PasswordUtils;

/**
 * 密码仓库详情
 * Created by CaoJun on 2017/2/15.
 */

@Route(path = Constant.ACTIVITY_PASSWORDSTORE_DETAIL)
public class PasswordDetailActivity extends Activity {

    private Spinner spType;
    private EditText etCompany, etUrl, etLength, etAccount;//, etPassword;
    private Button btnDelete, btnSave;
    private PasswordEditText pePassword;

    @Autowired
    protected Password password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_passwordstore_detail);
        ARouter.getInstance().inject(this);

        spType = (Spinner) this.findViewById(R.id.spType);
        etCompany = (EditText) this.findViewById(R.id.etCompany);
        etUrl = (EditText) this.findViewById(R.id.etUrl);
        etLength = (EditText) this.findViewById(R.id.etLength);
        etAccount = (EditText) this.findViewById(R.id.etAccount);
//        etPassword = (EditText) this.findViewById(R.id.etPassword);
        pePassword = (PasswordEditText) this.findViewById(R.id.pePassword);
        btnDelete = (Button) this.findViewById(R.id.btnDelete);
        btnSave = (Button) this.findViewById(R.id.btnSave);

        etCompany.addTextChangedListener(textWatcher);
        etUrl.addTextChangedListener(textWatcher);
        etLength.addTextChangedListener(textWatcher);
        etAccount.addTextChangedListener(textWatcher);
//        etPassword.addTextChangedListener(textWatcher);
        pePassword.addTextChangedListener(textWatcher);

//        password = (Password) getIntent().getSerializableExtra("password");

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.password_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spType.setAdapter(adapter);
        int index = password == null?0:password.getType();
        spType.setSelection(index, true);
        setPasswordType(index);
        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setPasswordType(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doDelete();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSave();
            }
        });

//        etLength.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(!hasFocus) {
//                    String length = etLength.getText().toString();
//                    if(!TextUtils.isEmpty(length)) {
//                        int l = Integer.parseInt(length);
//                        setPasswordLength(l);
//                    }
//                }
//            }
//        });
        pePassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    setPasswordType(spType.getSelectedItemPosition());
                    String length = etLength.getText().toString();
                    if(!TextUtils.isEmpty(length)) {
                        int l = Integer.parseInt(length);
                        setPasswordLength(l);
                    }
                }
            }
        });

        if(password != null) {
            etCompany.setText(password.getCompany());
            etUrl.setText(password.getUrl());
            etLength.setText(String.valueOf(password.getLength()));
            etAccount.setText(password.getAccount());
            pePassword.setText(PasswordUtils.getDecodePassword(password));
        }

        doCheckDeleteButton();
        doCheckSaveButton();
    }

    private void setPasswordType(int type) {
        switch(type) {
            case 0://数字
                pePassword.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                break;
            case 1://字母+数字+符号
                pePassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
            default:
                pePassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                break;
        }
    }

    private void setPasswordLength(int length) {
        pePassword.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
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

    private void doDelete() {
        if(password == null) {
            return;
        }
        PasswordDatabase.getInstance(this).delete(password.getId());
        finish();
    }

    private void doSave() {
        String company = etCompany.getText().toString();
        String url = etUrl.getText().toString();
        byte type = (byte) spType.getSelectedItemPosition();
        byte length = (byte) Integer.parseInt(etLength.getText().toString());
        String account = etAccount.getText().toString();
        String psd = pePassword.getText().toString();
        if(isNew()) {
            PasswordDatabase.getInstance(this).insert(company, url, type, length, account, psd);
        } else {
            PasswordDatabase.getInstance(this).update(password.getId(), company, url, type, length, account, psd);
        }
        finish();
    }

    private boolean isNew() {
        return password == null;
    }

    private boolean canSave() {
        int type = spType.getSelectedItemPosition();
        if(password != null && type != password.getType()) {
            return true;
        }
        String company = etCompany.getText().toString();
        if(TextUtils.isEmpty(company)) {
            return false;
        }
        if(password != null && !company.equals(password.getCompany())) {
            return true;
        }
        String url = etUrl.getText().toString();
        if(TextUtils.isEmpty(url)) {
            return false;
        }
        if(password != null && !url.equals(password.getUrl())) {
            return true;
        }
        String length = etLength.getText().toString();
        if(TextUtils.isEmpty(length)) {
            return false;
        }
        if(password != null && !length.equals(String.valueOf(password.getLength()))) {
            return true;
        }
        String account = etAccount.getText().toString();
        if(TextUtils.isEmpty(account)) {
            return false;
        }
        if(password != null && !account.equals(password.getAccount())) {
            return true;
        }
        String psd = pePassword.getText().toString();
        if(TextUtils.isEmpty(psd)) {
            return false;
        }
        if(password != null && !psd.equals(PasswordUtils.getDecodePassword(password))) {
            return true;
        }
        return isNew();
    }

    private void doCheckDeleteButton() {
        btnDelete.setEnabled(!isNew());
    }

    private void doCheckSaveButton() {
        btnSave.setEnabled(canSave());
    }
}
