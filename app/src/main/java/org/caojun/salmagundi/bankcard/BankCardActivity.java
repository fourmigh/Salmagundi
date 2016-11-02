package org.caojun.salmagundi.bankcard;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.caojun.salmagundi.BaseActivity;
import org.caojun.salmagundi.R;
import org.caojun.salmagundi.bankcard.adapter.RecordAdapter;
import org.caojun.salmagundi.bankcard.nfc.PBOC;
import org.caojun.salmagundi.bankcard.nfc.TradeType;
import org.caojun.salmagundi.bankcard.pboc.Card;
import org.caojun.salmagundi.bankcard.pboc.Record;

import java.math.BigDecimal;
import java.util.List;

/**
 * NFC读取银行卡信息
 * Created by CaoJun on 2016/11/2.
 */

public class BankCardActivity extends BaseActivity {

    private static final int RequestCode_OpenNFC = 1, RequestCode_CloseNFC = 2;
    private ListView lvRecord;
    private TextView tvCardInfo;
    private ToggleButton tbNFC;

    private PendingIntent mPendingIntent;
    private NfcAdapter mAdapter;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bankcard);
        lvRecord = (ListView) this.findViewById(R.id.lvRecord);
        tvCardInfo = (TextView) this.findViewById(R.id.tvCardInfo);
        tbNFC = (ToggleButton) this.findViewById(R.id.tbNFC);

        tbNFC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int requestCode = RequestCode_OpenNFC;
                if(tbNFC.isChecked())
                {
                    requestCode = RequestCode_CloseNFC;
                }
                startActivityForResult(new Intent(android.provider.Settings.ACTION_NFC_SETTINGS), requestCode);
            }
        });

        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        // 做一个IntentFilter过滤你想要的action 这里过滤的是ndef
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        // 如果你对action的定义有更高的要求，比如data的要求，你可以使用如下的代码来定义intentFilter
        try {
            ndef.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            e.printStackTrace();
        }
        // 生成intentFilter
        mFilters = new IntentFilter[] { ndef, };

        // 做一个tech-list。可以看到是二维数据，每一个一维数组之间的关系是或，但是一个一维数组之内的各个项就是与的关系了
        mTechLists = new String[][] { new String[] { NfcF.class.getName() },
                new String[] { NfcA.class.getName() },
                new String[] { NfcB.class.getName() },
                new String[] { NfcV.class.getName() } };

        mAdapter = NfcAdapter.getDefaultAdapter(this);

        setNFCStatus(hasNfc());
    }

    @Override
    protected void onResume() {
        if (mAdapter != null) {
            mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters,
                    mTechLists);
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (mAdapter != null) {
            mAdapter.disableForegroundDispatch(this);
        }
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RequestCode_OpenNFC:
            case RequestCode_CloseNFC:
                setNFCStatus(hasNfc());
                return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setNFCStatus(boolean opened) {
        tbNFC.setChecked(opened);
    }

    public boolean hasNfc() {
        return mAdapter != null && mAdapter.isEnabled();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        String action = intent.getAction();
        if (TextUtils.isEmpty(action)) {
            return;
        }

        try {
            PBOC pboc = new PBOC();
            Card card = pboc.readRecordsCard(intent, TradeType.NFCBAL, new BigDecimal(0));
            List<Record> list = card.getRecords();
            tvCardInfo.setText(card.toString());
            RecordAdapter adapter = new RecordAdapter(this, list);
            lvRecord.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
