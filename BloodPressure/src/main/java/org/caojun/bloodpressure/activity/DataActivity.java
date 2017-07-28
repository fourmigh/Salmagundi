package org.caojun.bloodpressure.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

import org.caojun.bloodpressure.Constant;
import org.caojun.bloodpressure.R;
import org.caojun.bloodpressure.utils.BloodPressureUtils;

/**
 * 数据导入、导出
 * Created by CaoJun on 2017/7/28.
 */

@Route(path = Constant.ACTIVITY_DATA)
public class DataActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        final Button btnImport = (Button) findViewById(R.id.btnImport);
        final Button btnExport = (Button) findViewById(R.id.btnExport);

        btnImport.setEnabled(BloodPressureUtils.fileExists());
        btnExport.setEnabled(BloodPressureUtils.listExisits(this));

        btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BloodPressureUtils.importToDB(DataActivity.this)) {
                    gotoNext();
                }
            }
        });

        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BloodPressureUtils.exportFromDB(DataActivity.this)) {
                    gotoNext();
                }
            }
        });
    }

    private void gotoNext() {
        ARouter.getInstance().build(Constant.ACTIVITY_BLOODPRESSURE).navigation();
        finish();
    }
}
