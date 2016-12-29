package org.caojun.salmagundi.secure;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
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

/**
 * 加密相关类
 * Created by CaoJun on 2016/12/26.
 */

public class SecureActivity extends BaseActivity {
    private Spinner spSecureType;
    private EditText etInput, etOutput, etKey, etPublicKey, etPrivateKey;
    private TextInputLayout tilKey, tilPublicKey, tilPrivateKey;
    private Button btnOK;
    private ImageButton ibExchange;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_secure);

        spSecureType = (Spinner) this.findViewById(R.id.spSecureType);
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
        String strOutput = null;
        switch(spSecureType.getSelectedItemPosition())
        {
            case 0://DES加密
                break;
            case 1://DES解密
                break;
            case 2://3DES加密
                break;
            case 3://3DES解密
                break;
            case 4://AES加密
                break;
            case 5://AES解密
                break;
        }
        etOutput.setText(strOutput);
    }
}
