package org.caojun.salmagundi.secure;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
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
    private Spinner spTranslateType;
    private EditText etInput, etOutput;
    private Button btnOK;
    private ImageButton ibExchange;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_secure);

        spTranslateType = (Spinner) this.findViewById(R.id.spTranslateType);
        etInput = (EditText) this.findViewById(R.id.etInput);
        etOutput = (EditText) this.findViewById(R.id.etOutput);
        btnOK = (Button) this.findViewById(R.id.btnOK);
        ibExchange = (ImageButton) this.findViewById(R.id.ibExchange);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.translate_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTranslateType.setAdapter(adapter);

        btnOK.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v) {
                doTranslate();
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

    private void doTranslate()
    {
        String strInput = etInput.getText().toString();
        String strOutput = null;
        switch(spTranslateType.getSelectedItemPosition())
        {
            case 0://小写 -> 大写
                strOutput = StringConvertUtils.toUpperCase(strInput);
                break;
            case 1://大写 -> 小写
                strOutput = StringConvertUtils.toLowerCase(strInput);
                break;
            case 2://汉字 -> 拼音
                strOutput = StringConvertUtils.toPinyinString(strInput);
                break;
            case 3://字符串 -> ASCII码
                strOutput = StringConvertUtils.toASCII(strInput);
                break;
            case 4://十进制 -> 二进制
                strOutput = StringConvertUtils.toBinary(strInput);
                break;
            case 5://二进制 -> 十进制
                strOutput = StringConvertUtils.binaryTo(strInput);
                break;
            case 6://十进制 -> 八进制
                strOutput = StringConvertUtils.toOctal(strInput);
                break;
            case 7://八进制 -> 十进制
                strOutput = StringConvertUtils.octalTo(strInput);
                break;
            case 8://十进制 -> 十六进制
                strOutput = StringConvertUtils.toHex(strInput);
                break;
            case 9://十六进制 -> 十进制
                strOutput = StringConvertUtils.hexTo(strInput);
                break;
            case 10://字符串 -> MD5
                strOutput = StringConvertUtils.toMD5(strInput);
                break;
            case 11://手机号 -> 隐藏
                strOutput = StringConvertUtils.maskMobile(strInput);
                break;
            case 12://银行卡号 -> 隐藏
                strOutput = StringConvertUtils.maskBankCardNo(strInput);
                break;
            case 13://银行卡号 -> 格式化
                strOutput = StringConvertUtils.formatBankCardNo(strInput);
                break;
        }
        etOutput.setText(strOutput);
    }
}
