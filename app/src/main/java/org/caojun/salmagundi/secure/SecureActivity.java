package org.caojun.salmagundi.secure;

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
import android.widget.Spinner;
import org.caojun.salmagundi.BaseActivity;
import org.caojun.salmagundi.R;
import org.caojun.salmagundi.string.ConvertUtils;

/**
 * 加密相关类
 * Created by CaoJun on 2016/12/26.
 */

public class SecureActivity extends BaseActivity {
    private Spinner spSecureType, spInput, spOutput;
    private EditText etInput, etOutput, etKey, etPublicKey, etPrivateKey;
    private TextInputLayout tilKey, tilPublicKey, tilPrivateKey;
    private Button btnOK;
    private ImageButton ibExchange;

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
        tilPublicKey = (TextInputLayout) this.findViewById(R.id.tilPublicKey);
        tilPrivateKey = (TextInputLayout) this.findViewById(R.id.tilPrivateKey);

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
            tilPublicKey.setVisibility(View.VISIBLE);
            tilPrivateKey.setVisibility(View.VISIBLE);
        }
        else {
            tilKey.setVisibility(View.VISIBLE);
            tilPublicKey.setVisibility(View.GONE);
            tilPrivateKey.setVisibility(View.GONE);
        }
    }

    private void doSecure()
    {
        String strInput = etInput.getText().toString();
        String strKey = etKey.getText().toString();
        if(TextUtils.isEmpty(strInput) || TextUtils.isEmpty(strKey)) {
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
        switch(spSecureType.getSelectedItemPosition())
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
                break;
            case 5://3DES解密
                break;
            case 6://RSA公钥加密
                break;
            case 7://RSA公钥解密
                break;
            case 8://RSA私钥加密
                break;
            case 9://RSA私钥解密
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
