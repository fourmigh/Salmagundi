package org.caojun.salmagundi.typewriting;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;

import org.caojun.salmagundi.Constant;
import org.caojun.salmagundi.R;
import org.caojun.salmagundi.utils.MobileUtils;

import java.util.Arrays;

/**
 * Created by CaoJun on 2017-12-7.
 */
@Route(path = Constant.ACTIVITY_TYPEWRITING)
public class MobileInputActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_mobile_input);

        final EditText etMobile = findViewById(R.id.etMobile);
        final TextView tvInfo = findViewById(R.id.tvInfo);

        etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String[] inputs = MobileUtils.getSectionNumber(MobileInputActivity.this, editable.toString());
                if (inputs != null) {
                    tvInfo.setText(Arrays.asList(inputs).toString());
                } else {
                    tvInfo.setText(null);
                }
            }
        });
        String[] inputs = MobileUtils.getSectionNumber(this, etMobile.getText().toString());
        if (inputs != null) {
            tvInfo.setText(Arrays.asList(inputs).toString());
        }
    }
}
