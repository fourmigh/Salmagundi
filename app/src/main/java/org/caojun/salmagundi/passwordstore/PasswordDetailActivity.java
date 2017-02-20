package org.caojun.salmagundi.passwordstore;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import org.caojun.salmagundi.BaseActivity;
import org.caojun.salmagundi.R;
import org.caojun.salmagundi.passwordstore.greendao.Password;
import org.caojun.salmagundi.passwordstore.greendao.PasswordDatabase;

/**
 * 密码仓库详情
 * Created by CaoJun on 2017/2/15.
 */

public class PasswordDetailActivity extends BaseActivity {

    private Spinner spType;
    private EditText etCompany, etUrl, etLength, etAccount, etPassword;
    private Button btnDelete, btnSave;
    private Password password;
//    private PasswordDatabase passwordDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_passwordstore_detail);

        spType = (Spinner) this.findViewById(R.id.spType);
        etCompany = (EditText) this.findViewById(R.id.etCompany);
        etUrl = (EditText) this.findViewById(R.id.etUrl);
        etLength = (EditText) this.findViewById(R.id.etLength);
        etAccount = (EditText) this.findViewById(R.id.etAccount);
        etPassword = (EditText) this.findViewById(R.id.etPassword);
        btnDelete = (Button) this.findViewById(R.id.btnDelete);
        btnSave = (Button) this.findViewById(R.id.btnSave);

        etCompany.addTextChangedListener(textWatcher);
        etUrl.addTextChangedListener(textWatcher);
        etLength.addTextChangedListener(textWatcher);
        etAccount.addTextChangedListener(textWatcher);
        etPassword.addTextChangedListener(textWatcher);

        password = (Password) getIntent().getSerializableExtra("password");

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.password_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spType.setAdapter(adapter);
        int index = password == null?0:password.getType();
        spType.setSelection(index, true);
        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

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

        if(password != null) {
            etCompany.setText(password.getCompany());
            etUrl.setText(password.getUrl());
            etLength.setText(String.valueOf(password.getLength()));
            etAccount.setText(password.getAccount());
            etPassword.setText(password.getPassword());
        }

        doCheckDeleteButton();
        doCheckSaveButton();

//        passwordDatabase = new PasswordDatabase(this);
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
        String psd = etPassword.getText().toString();
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
        String psd = etPassword.getText().toString();
        if(TextUtils.isEmpty(psd)) {
            return false;
        }
        if(password != null && !psd.equals(password.getPassword())) {
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
