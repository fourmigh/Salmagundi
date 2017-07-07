package org.caojun.bloodpressure;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.alibaba.android.arouter.launcher.ARouter;
import org.caojun.bloodpressure.utils.BloodPressureUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btnImport = (Button) findViewById(R.id.btnImport);
        final Button btnExport = (Button) findViewById(R.id.btnExport);
        final Button btnSkip = (Button) findViewById(R.id.btnSkip);
        final Button btnBMI = (Button) findViewById(R.id.btnBMI);

        btnImport.setEnabled(BloodPressureUtils.fileExists());
        btnExport.setEnabled(BloodPressureUtils.listExisits(this));

        btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BloodPressureUtils.importToDB(MainActivity.this)) {
                    gotoNext();
                }
            }
        });

        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BloodPressureUtils.exportFromDB(MainActivity.this)) {
                    gotoNext();
                }
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoNext();
            }
        });

        btnBMI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(Constant.ACTIVITY_BMI).navigation();
            }
        });

        if (!btnImport.isEnabled() && !btnExport.isEnabled()) {
            gotoNext();
        }
    }

    private void gotoNext() {
        ARouter.getInstance().build(Constant.ACTIVITY_BLOODPRESSURE).navigation();
        finish();
    }
}
