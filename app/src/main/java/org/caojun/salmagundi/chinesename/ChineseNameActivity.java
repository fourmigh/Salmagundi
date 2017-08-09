package org.caojun.salmagundi.chinesename;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.alibaba.android.arouter.facade.annotation.Route;
import org.caojun.salmagundi.Constant;
import org.caojun.salmagundi.R;

/**
 * Created by CaoJun on 2017/8/4.
 */

@Route(path = Constant.ACTIVITY_CHINESENAME)
public class ChineseNameActivity extends AppCompatActivity {

    private Button btnSurname, btnName;
    private EditText etSurname, etName;
    private RadioGroup rgSurname, rgName;
    private RadioButton[] rbSurnames, rbNames;
    private WebView webView;
    private Button btnGenerate;
    private boolean isSurnameChecked, isNameChecked;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_chinesename);

        btnSurname = (Button) findViewById(R.id.btnSurname);
        btnName = (Button) findViewById(R.id.btnName);
        etSurname = (EditText) findViewById(R.id.etSurname);
        etName = (EditText) findViewById(R.id.etName);
        rgSurname = (RadioGroup) findViewById(R.id.rgSurname);
        rgName = (RadioGroup) findViewById(R.id.rgName);
        webView = (WebView) findViewById(R.id.webView);
        btnGenerate = (Button) findViewById(R.id.btnGenerate);

        String[] surnameType = getResources().getStringArray(R.array.surname_type);
        rbSurnames = new RadioButton[surnameType.length];
        for (int i = 0;i < surnameType.length;i ++) {
            rbSurnames[i] = new RadioButton(this);
            rbSurnames[i].setText(surnameType[i]);
            rgSurname.addView(rbSurnames[i]);
        }

        String[] nameType = getResources().getStringArray(R.array.name_type);
        rbNames = new RadioButton[nameType.length];
        for (int i = 0;i < nameType.length;i ++) {
            rbNames[i] = new RadioButton(this);
            rbNames[i].setText(nameType[i]);
            rgName.addView(rbNames[i]);
        }

        rgSurname.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                isSurnameChecked = true;
                btnGenerate.setEnabled(isSurnameChecked & isNameChecked);
                int index = group.indexOfChild(group.findViewById(group.getCheckedRadioButtonId()));
                etSurname.setEnabled(index == group.getChildCount() - 1);
            }
        });

        rgName.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                isNameChecked = true;
                btnGenerate.setEnabled(isSurnameChecked & isNameChecked);
                int index = group.indexOfChild(group.findViewById(group.getCheckedRadioButtonId()));
                etName.setEnabled(index == group.getChildCount() - 1);
            }
        });

        btnSurname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExplain(etSurname.getText().toString());
            }
        });

        btnSurname.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showFullName();
                return true;
            }
        });

        btnName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExplain(etName.getText().toString());
            }
        });

        btnName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showExplain(etName.getText().toString(), true);
                return true;
            }
        });

        etSurname.setEnabled(false);
        etName.setEnabled(false);
        btnGenerate.setEnabled(isSurnameChecked & isNameChecked);
        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doGenerate();
            }
        });
    }

    private void doGenerate() {
        int surnameType = rgSurname.indexOfChild(rgSurname.findViewById(rgSurname.getCheckedRadioButtonId()));
        if (surnameType != ChineseNameUtils.Type_Surname_Custom) {
            String surname = ChineseNameUtils.getSurname(this, surnameType);
            etSurname.setText(surname);
        }
        int nameType = rgName.indexOfChild(rgName.findViewById(rgName.getCheckedRadioButtonId()));
        if (nameType != ChineseNameUtils.Type_Name_Custom) {
            String name = ChineseNameUtils.getName(nameType);
            etName.setText(name);
        }
    }

    private void showExplain(String text) {
        showExplain(text, false);
    }

    private void showExplain(String text, boolean isFullName) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        String url = "http://hanyu.baidu.com/zici/s?wd=" + text;
        if (isFullName) {
            url = "https://www.baidu.com/baidu?wd=" + text;
        }
        webView.loadUrl(url);
    }

    private void showFullName() {
        String surname = etSurname.getText().toString();
        if (TextUtils.isEmpty(surname)) {
            return;
        }
        String name = etName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            return;
        }
        showExplain(surname + name, true);
    }
}
