package org.caojun.salmagundi.iflytek;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

import org.caojun.salmagundi.Constant;
import org.caojun.salmagundi.R;

/**
 * 讯飞语音
 * Created by CaoJun on 2017/7/26.
 */

@Route(path = Constant.ACTIVITY_FLYTEK)
public class FlytekActivity extends AppCompatActivity {

    private final int[] IDButtons = {R.id.btnEnglish, R.id.btnFrench, R.id.btnRussian, R.id.btnSpanish, R.id.btnHindi, R.id.btnVietnamese};
    private final int[] IDEditTexts = {R.id.etEnglish, R.id.etFrench, R.id.etRussian, R.id.etSpanish, R.id.etHindi, R.id.etVietnamese};
    private final String[] VoiceNames = {"aiscatherine", "mariane", "allabent", "gabriela", "abha", "xiaoyun"};
    private Button[] buttons;
    private EditText[] editTexts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_flytek);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        buttons = new Button[IDButtons.length];
        for (int i = 0;i < IDButtons.length;i ++) {
            buttons[i] = (Button) findViewById(IDButtons[i]);
        }
        editTexts = new EditText[IDEditTexts.length];
        for (int i = 0;i < IDEditTexts.length;i ++) {
            editTexts[i] = (EditText) findViewById(IDEditTexts[i]);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            ARouter.getInstance().build(Constant.ACTIVITY_FLYTEK_TTSSETTINGS).navigation();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
