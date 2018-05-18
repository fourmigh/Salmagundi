package org.caojun.salmagundi.svgmap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import org.caojun.salmagundi.Constant;
import org.caojun.salmagundi.R;
import org.caojun.svgmap.PathItem;
import org.caojun.svgmap.SvgMapView;
import org.jetbrains.annotations.NotNull;

@Route(path = Constant.ACTIVITY_SVGMAP)
public class SvgmapActivity extends Activity {

    public final static String Key_Map_Name = "Key_Map_Name";
    private String lastId = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svgmap);

        final TextView tvInfo = findViewById(R.id.tvInfo);
        final SvgMapView svgMapView = findViewById(R.id.svgMapView);
        svgMapView.setMapListener(new SvgMapView.MapListener() {
            @Override
            public void onClick(@NotNull PathItem item) {
                if (!TextUtils.isEmpty(item.getTitle())) {
                    Toast.makeText(SvgmapActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(item.getId()) || !svgMapView.hasMap(item.getId())) {
                    return;
                }
                if (!lastId.equals(item.getId())) {
                    lastId = item.getId();
                    return;
                }

                Intent intent = new Intent(SvgmapActivity.this, SvgmapActivity.class);
                intent.putExtra(Key_Map_Name, item.getId());
                startActivity(intent);
            }

            @Override
            public void onShow(@NotNull PathItem item, int index, int size) {
                tvInfo.setText(item.getTitle());
                if (index == size - 1) {
                    try {
                        Thread.sleep(2000);
                        tvInfo.setText(null);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
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
