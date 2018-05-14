package org.caojun.salmagundi.svgmap;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.alibaba.android.arouter.facade.annotation.Route;
import org.caojun.salmagundi.Constant;
import org.caojun.salmagundi.R;

@Route(path = Constant.ACTIVITY_SVGMAP)
public class SvgmapActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svgmap);
    }
}
