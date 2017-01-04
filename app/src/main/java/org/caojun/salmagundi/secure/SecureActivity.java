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
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.socks.library.KLog;

import org.caojun.salmagundi.BaseActivity;
import org.caojun.salmagundi.R;
import org.caojun.salmagundi.string.ConvertUtils;

import java.util.HashMap;

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
        spInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                doChangeCharacterFormat(spInput, etInput);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spOutput.setAdapter(adapter);
        spOutput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                doChangeCharacterFormat(spOutput, etOutput);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

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

    private void doChangeCharacterFormat(Spinner spinner, EditText editText) {
        if (spinner == null || editText == null) {
            return;
        }
    }

    private void doExchange()
    {
        String strInput = etInput.getText().toString();
        String strOutput = etOutput.getText().toString();
        etInput.setText(strOutput);
        etOutput.setText(strInput);
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
//        switch(spInput.getSelectedItemPosition()) {
//            case 1://十六进制
//                strInput = ConvertUtils.hex2string(strInput);
//                break;
//            case 2://Base64
//                strInput = ConvertUtils.base64To(strInput);
//                break;
//        }

        String strOutput = null;
        switch(spSecureType.getSelectedItemPosition())
        {
            case 0://AES加密
                strOutput = AES.encrypt(strKey, strInput);
                break;
            case 1://AES解密
                strOutput = AES.decrypt(strKey, strInput);
                break;
            case 2://DES加密
                break;
            case 3://DES解密
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
        if(TextUtils.isEmpty(strOutput)) {
            return;
        }
//        switch(spOutput.getSelectedItemPosition()) {
//            case 1://十六进制
//                strOutput = ConvertUtils.string2hex(strOutput);
//                break;
//            case 2://Base64
//                strOutput = ConvertUtils.toBase64(strOutput);
//                break;
//        }
        etOutput.setText(strOutput);
    }
}
