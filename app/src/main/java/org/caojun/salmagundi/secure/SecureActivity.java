package org.caojun.salmagundi.secure;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import org.caojun.salmagundi.BaseActivity;
import org.caojun.salmagundi.R;
import org.caojun.salmagundi.secure.data.RSAKey;
import org.caojun.salmagundi.secure.data.RSAKeyDatabase;
import org.caojun.salmagundi.string.ConvertUtils;

/**
 * 加密相关类
 * Created by CaoJun on 2016/12/26.
 */

public class SecureActivity extends BaseActivity {
    private Spinner spSecureType, spInput, spOutput, spRSAKey;
    private EditText etInput, etOutput, etKey, etPublicKey, etPrivateKey;
    private TextInputLayout tilKey;
    private LinearLayout llRSAKey;
    private Button btnOK, btnRSAKey;
    private ImageButton ibExchange;
    private byte[] keyPrivate, keyPublic;
    private RSAKeyDatabase rsaKeyDatabase;
    private Cursor cKeyPair;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_secure);

        spSecureType = (Spinner) this.findViewById(R.id.spSecureType);
        spInput = (Spinner) this.findViewById(R.id.spInput);
        spOutput = (Spinner) this.findViewById(R.id.spOutput);
        etInput = (EditText) this.findViewById(R.id.etInput);
        etOutput = (EditText) this.findViewById(R.id.etOutput);
        etKey = (EditText) this.findViewById(R.id.etKey);
        etPublicKey = (EditText) this.findViewById(R.id.etPublicKey);
        etPrivateKey = (EditText) this.findViewById(R.id.etPrivateKey);
        btnOK = (Button) this.findViewById(R.id.btnOK);
        ibExchange = (ImageButton) this.findViewById(R.id.ibExchange);
        tilKey = (TextInputLayout) this.findViewById(R.id.tilKey);
        llRSAKey = (LinearLayout) this.findViewById(R.id.llRSAKey);
        spRSAKey = (Spinner) this.findViewById(R.id.spRSAKey);
        btnRSAKey = (Button) this.findViewById(R.id.btnRSAKey);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.secure_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSecureType.setAdapter(adapter);
        spSecureType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                doChangeKey();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                doChangeKey();
            }
        });

        adapter = ArrayAdapter.createFromResource(this, R.array.character_format, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spInput.setAdapter(adapter);
        spOutput.setAdapter(adapter);

        btnOK.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v) {
                doSecure();
            }
        });
        ibExchange.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                doExchange();
            }
        });
        btnRSAKey.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                genKeyPair();
            }
        });

        queryKeyPair();
    }

    /**
     * 生成公私钥对
     */
    private void genKeyPair()
    {
        byte[][] key = RSA.genKeyPair(1024);
        if(key == null) {
            return;
        }
        keyPublic = key[0];
        keyPrivate = key[1];
        if(keyPrivate == null || keyPublic == null) {
            return;
        }
        if(rsaKeyDatabase == null) {
            rsaKeyDatabase = new RSAKeyDatabase(this);
        }
        saveKeyPair();
    }

    /**
     * 存储公私钥对
     */
    private void saveKeyPair()
    {
        if(keyPrivate == null || keyPublic == null || rsaKeyDatabase == null)
        {
            return;
        }
        ContentValues values = new ContentValues();
        values.put(RSAKey.Private_Key, keyPrivate);
        values.put(RSAKey.Public_Key, keyPublic);
        if(rsaKeyDatabase.insert(values) > 0)
        {
            queryKeyPair();
        }
    }

    /**
     * 读取公私钥对
     */
    private void queryKeyPair()
    {
        if(rsaKeyDatabase == null) {
            rsaKeyDatabase = new RSAKeyDatabase(this);
        }
        cKeyPair = rsaKeyDatabase.query();
        if(cKeyPair == null || cKeyPair.getCount() < 1)
        {
            spRSAKey.setVisibility(View.GONE);
        }
        else
        {
            String[] keys = new String[cKeyPair.getCount()];
            for(int i = 0;i < keys.length;i ++)
            {
                keys[i] = getString(R.string.key_pair) + "-" + i;
            }
            ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, keys);
            spRSAKey.setAdapter(adapter);

            if(cKeyPair.moveToFirst()) {

                doChangeRSAKey(0);

                spRSAKey.setVisibility(View.VISIBLE);

                spRSAKey.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        doChangeRSAKey(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }
    }

    private void doChangeRSAKey(int position) {
        if(cKeyPair == null || !cKeyPair.moveToPosition(position)) {
            return;
        }
        keyPublic = cKeyPair.getBlob(cKeyPair.getColumnIndex(RSAKey.Public_Key));
        keyPrivate = cKeyPair.getBlob(cKeyPair.getColumnIndex(RSAKey.Private_Key));
        String strKeyPublic = ConvertUtils.toBase64(keyPublic);
        String strKeyPrivate = ConvertUtils.toBase64(keyPrivate);
        etPublicKey.setText(strKeyPublic);
        etPrivateKey.setText(strKeyPrivate);
    }

    private void doExchange()
    {
        String strInput = etInput.getText().toString();
        String strOutput = etOutput.getText().toString();
        etInput.setText(strOutput);
        etOutput.setText(strInput);

        int indexInput = spInput.getSelectedItemPosition();
        int indexOutput = spOutput.getSelectedItemPosition();
        spInput.setSelection(indexOutput);
        spOutput.setSelection(indexInput);

        int indexSecureType = spSecureType.getSelectedItemPosition();
        if(indexSecureType % 2 == 0) {
            indexSecureType ++;
        }
        else {
            indexSecureType --;
        }
        spSecureType.setSelection(indexSecureType);
    }

    private void doChangeKey() {
        boolean isRSA = spSecureType.getSelectedItemPosition() > 5;
        if (isRSA) {
            tilKey.setVisibility(View.GONE);
            llRSAKey.setVisibility(View.VISIBLE);
        }
        else {
            tilKey.setVisibility(View.VISIBLE);
            llRSAKey.setVisibility(View.GONE);
        }
    }

    private void doSecure()
    {
        int indexSecureType = spSecureType.getSelectedItemPosition();
        String strKey = etKey.getText().toString();
        if(indexSecureType < 6) {
            if (TextUtils.isEmpty(strKey)) {
                return;
            }
        }
        else {
            if(keyPrivate == null || keyPublic == null) {
                return;
            }
        }
        String strInput = etInput.getText().toString();
        if (TextUtils.isEmpty(strInput)) {
            return;
        }
        byte[] input = null;
        switch(spInput.getSelectedItemPosition()) {
            case 0://无格式
                input = strInput.getBytes();
                break;
            case 1://十六进制
                input = ConvertUtils.hexToBytes(strInput);
                break;
            case 2://Base64
                input = ConvertUtils.base64ToBytes(strInput);
                break;
        }

        byte[] output = null;
        switch(indexSecureType)
        {
            case 0://AES加密
                output = AES.encrypt(strKey, input);
                break;
            case 1://AES解密
                output = AES.decrypt(strKey, input);
                break;
            case 2://DES加密
                output = DES.encrypt(strKey, input);
                break;
            case 3://DES解密
                output = DES.decrypt(strKey, input);
                break;
            case 4://3DES加密
                output = DESede.encrypt(strKey, input);
                break;
            case 5://3DES解密
                output = DESede.decrypt(strKey, input);
                break;
            case 6://RSA公钥加密
                output = RSA.encrypt(keyPublic, true, input);
                break;
            case 7://RSA私钥解密
                output = RSA.decrypt(keyPrivate, false, input);
                break;
            case 8://RSA私钥加密
                output = RSA.encrypt(keyPrivate, false, input);
                break;
            case 9://RSA公钥解密
                output = RSA.decrypt(keyPublic, true, input);
                break;
        }
        String strOutput = null;
        if(output != null) {
            switch(spOutput.getSelectedItemPosition()) {
                case 0://无格式
                    strOutput = new String(output);
                    break;
                case 1://十六进制
                    strOutput = ConvertUtils.stringToHex(output);
                    break;
                case 2://Base64
                    strOutput = ConvertUtils.toBase64(output);
                    break;
            }
        }
        etOutput.setText(strOutput);
    }
}
