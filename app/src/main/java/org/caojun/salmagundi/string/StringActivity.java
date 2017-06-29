package org.caojun.salmagundi.string;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.alibaba.android.arouter.facade.annotation.Route;

import org.caojun.salmagundi.BaseActivity;
import org.caojun.salmagundi.Constant;
import org.caojun.salmagundi.R;

/**
 * 字符串转换
 * Created by CaoJun on 2016/12/28.
 */

@Route(path = Constant.ACTIVITY_STRING)
public class StringActivity extends BaseActivity {

    private Spinner spTranslateType;
    private EditText etInput, etOutput;
    private Button btnOK;
    private ImageButton ibExchange;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_string);

        spTranslateType = (Spinner) this.findViewById(R.id.spTranslateType);
        etInput = (EditText) this.findViewById(R.id.etInput);
        etOutput = (EditText) this.findViewById(R.id.etOutput);
        btnOK = (Button) this.findViewById(R.id.btnOK);
        ibExchange = (ImageButton) this.findViewById(R.id.ibExchange);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.translate_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTranslateType.setAdapter(adapter);

        btnOK.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                doTranslate();
            }
        });
        ibExchange.setOnClickListener(new View.OnClickListener(){
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
                strOutput = ConvertUtils.toUpperCase(strInput);
                break;
            case 1://大写 -> 小写
                strOutput = ConvertUtils.toLowerCase(strInput);
                break;
            case 2://汉字 -> 拼音
                strOutput = ConvertUtils.toPinyinString(strInput);
                break;
            case 3://十进制 -> 二进制
                strOutput = ConvertUtils.decimalToBinary(strInput);
                break;
            case 4://二进制 -> 十进制
                strOutput = ConvertUtils.binaryToDecimal(strInput);
                break;
            case 5://十进制 -> 八进制
                strOutput = ConvertUtils.decimalToOctal(strInput);
                break;
            case 6://八进制 -> 十进制
                strOutput = ConvertUtils.octalToDecimal(strInput);
                break;
            case 7://十进制 -> 十六进制
                strOutput = ConvertUtils.decimalToHex(strInput);
                break;
            case 8://十六进制 -> 十进制
                strOutput = ConvertUtils.hexToDecimal(strInput);
                break;
            case 9://手机号 -> 隐藏
                strOutput = ConvertUtils.maskMobile(strInput);
                break;
            case 10://银行卡号 -> 隐藏
                strOutput = ConvertUtils.maskBankCardNo(strInput);
                break;
            case 11://银行卡号 -> 格式化
                strOutput = ConvertUtils.formatBankCardNo(strInput);
                break;
            case 12://身份证号 -> 隐藏
                strOutput = ConvertUtils.maskPersonIdentifier(strInput);
                break;
            case 13://字符串 -> MD5
                strOutput = ConvertUtils.toMD5(strInput);
                break;
            case 14://字符串 -> SHA
                strOutput = ConvertUtils.toSHA(strInput);
                break;
            case 15://字符串 -> Base64
                strOutput = ConvertUtils.toBase64(strInput);
                break;
            case 16://Base64 -> 字符串
                strOutput = ConvertUtils.base64ToString(strInput);
                break;
            case 17://字符串 -> ASCII码
                strOutput = ConvertUtils.toASCII(strInput);
                break;
            case 18://ASCII码 -> 字符串
                strOutput = ConvertUtils.asciiToString(strInput);
                break;
            case 19://字符串 -> 十六进制
                strOutput = ConvertUtils.stringToHex(strInput);
                break;
            case 20://十六进制 -> 字符串
                strOutput = ConvertUtils.hexToString(strInput);
                break;
            case 21://正序 -> 反序
                strOutput = ConvertUtils.reverse(strInput);
                break;
        }
        etOutput.setText(strOutput);
    }
}
