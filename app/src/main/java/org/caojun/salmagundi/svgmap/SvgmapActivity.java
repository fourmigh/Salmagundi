package org.caojun.salmagundi.svgmap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import org.caojun.salmagundi.Constant;
import org.caojun.salmagundi.R;

@Route(path = Constant.ACTIVITY_SVGMAP)
public class SvgmapActivity extends Activity {

    public final static String Key_Map_Name = "Key_Map_Name";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svgmap);

        final SvgMapView svgMapView = findViewById(R.id.svgMapView);
        svgMapView.setOnClickListener(new SvgMapView.OnClickListener() {
            @Override
            public void onClick(String title, String mapName) {
                if (!TextUtils.isEmpty(title)) {
                    Toast.makeText(SvgmapActivity.this, title, Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(mapName)) {
                    return;
                }
                Intent intent = new Intent(SvgmapActivity.this, SvgmapActivity.class);
                intent.putExtra(Key_Map_Name, mapName);
                startActivity(intent);
            }
        });

        String mapName = getIntent().getStringExtra(Key_Map_Name);
        if (TextUtils.isEmpty(mapName)) {
            svgMapView.setMap("world");
        } else {
            svgMapView.setMap(mapName);
        }


    }
}
