package org.caojun.salmagundi.taxicab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import org.caojun.salmagundi.BaseActivity;
import org.caojun.salmagundi.R;
import org.caojun.salmagundi.taxicab.adapter.TaxicabAdapter;
import org.caojun.salmagundi.taxicab.ormlite.Taxicab;

import java.math.BigInteger;
import java.util.List;

/**
 * 的士数
 *
 * 第n个的士数(Taxicab number)，一般写作Ta(n)或Taxicab(n)，定义为最小的数能以n个不同的方法表示成两个正立方数之和。
 *
 * 第n个的士数（cabtaxi number），表示为Cabtaxi(n)，定义为能以n种方法写成两个或正或负或零的立方数之和的正整数中最小者。
 * 它的名字来自的士数的颠倒。对任何的n，这样的数均存在，因为的士数对所有的n都存在。
 * Created by fourm on 2017/5/2.
 */

public class TaxicabActivity extends BaseActivity {

    private ListView lvTaxicab;
    private EditText etMax;
    private CheckBox cbTaCa;
    private Button btnOK;
    private TaxicabAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_taxicab);

        lvTaxicab = (ListView) this.findViewById(R.id.lvTaxicab);
        etMax = (EditText) this.findViewById(R.id.etMax);
        cbTaCa = (CheckBox) this.findViewById(R.id.cbTaCa);
        btnOK = (Button) this.findViewById(R.id.btnOK);

        etMax.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String strMax = editable.toString();
                checkMax(strMax);
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doOK();
            }
        });
    }

    private void doOK() {
        String strMax = etMax.getText().toString();
        BigInteger max = checkMax(strMax);
        if (max == null) {
            return;
        }
        List<Taxicab> list = TaxicabUtils.getList(this, max, cbTaCa.isChecked());
        if (adapter == null) {
            adapter = new TaxicabAdapter(this, list);
            lvTaxicab.setAdapter(adapter);
        } else {
            adapter.setData(list);
            adapter.notifyDataSetChanged();
        }
    }

    private void runDoOK() {

    }

    private BigInteger checkMax(String strMax) {
        if (TextUtils.isEmpty(strMax)) {
            btnOK.setEnabled(false);
            return null;
        }
        BigInteger max = new BigInteger(strMax);
        if (max.compareTo(BigInteger.ZERO) == 0) {
            btnOK.setEnabled(false);
            return null;
        }
        btnOK.setEnabled(true);
        return max;
    }
}
