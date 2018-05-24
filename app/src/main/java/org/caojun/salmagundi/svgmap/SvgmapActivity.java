package org.caojun.salmagundi.svgmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.android.arouter.facade.annotation.Route;
import org.caojun.activity.BaseActivity;
import org.caojun.salmagundi.Constant;
import org.caojun.salmagundi.R;
import org.caojun.svgmap.PathItem;
import org.caojun.svgmap.SvgMapView;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

@Route(path = Constant.ACTIVITY_SVGMAP)
public class SvgmapActivity extends BaseActivity {

    public final static String Key_Map_Name = "Key_Map_Name";
    private String lastId = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svgmap);

        final TextView tvInfo = findViewById(R.id.tvInfo);
        final SvgMapView svgMapView = findViewById(R.id.svgMapView);

        spID = findViewById(R.id.spID);
        spID.setVisibility(View.GONE);

        svgMapView.setMapListener(new SvgMapView.MapListener() {

            @Override
            public void onLongClick(@NotNull PathItem item, int index) {
                doSpinnerSelect(index, true);
            }

            @Override
            public void onClick(@NotNull PathItem item, int index) {

                doSpinnerSelect(index, false);

//                if (!TextUtils.isEmpty(item.getId())) {
//                    Integer resId = getString(SvgmapActivity.this, item.getId());
//                    if (resId == null) {
//                        Toast.makeText(SvgmapActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(SvgmapActivity.this, resId, Toast.LENGTH_SHORT).show();
//                    }
//                }
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
                Integer id = getString(SvgmapActivity.this, item.getId());
                if (id == null) {
                    tvInfo.setText(item.getTitle());
                } else {
                    tvInfo.setText(id);
                }

                if (index == size - 1) {
                    try {
                        Thread.sleep(1000);
                        tvInfo.setText(null);

                        final List<PathItem> pathItems = svgMapView.getPathItems();
                        List<String> names = new ArrayList<>();
                        for (int i = 0;i < pathItems.size();i ++) {
                            PathItem pathItem = pathItems.get(i);
                            id = getString(SvgmapActivity.this, pathItem.getId());
                            if (id == null) {
                                names.add(pathItem.getTitle());
                            } else {
                                names.add(getString(id));
                            }
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(SvgmapActivity.this,
                                android.R.layout.simple_dropdown_item_1line, names);
                        spID.setAdapter(adapter);
                        spID.setVisibility(View.VISIBLE);

                        spID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (isSelectWork) {
                                    for (int i = 0;i < pathItems.size();i ++) {
                                        pathItems.get(i).setSelected(position == i);
                                    }
//                                    svgMapView.doCenter(position);
//                                    svgMapView.doAnimateCenter(position);
                                    svgMapView.invalidate();
                                }
                                isSelectWork = true;
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        String mapName = getIntent().getStringExtra(Key_Map_Name);
        if (TextUtils.isEmpty(mapName)) {
            svgMapView.setMap("cn");
        } else {
            svgMapView.setMap(mapName);
        }

    }

    private Spinner spID;
    private boolean isSelectWork = false;
    private void doSpinnerSelect(int position, boolean isSelectWork) {
        spID.setSelection(position, true);
        this.isSelectWork = isSelectWork;
    }
}
