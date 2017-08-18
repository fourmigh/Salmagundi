package org.caojun.salmagundi.string;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.luhuiguo.chinese.ChineseUtils;
import com.socks.library.KLog;

import net.sourceforge.pinyin4j.PinyinHelper;

import org.caojun.salmagundi.Constant;
import org.caojun.salmagundi.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CaoJun on 2017/8/17.
 */

@Route(path = Constant.ACTIVITY_STRINGSORT)
public class StringSortActivity extends Activity {

    private class SortString {
        private int id;
        private String text;
        private String sort;
    }

    @Autowired
    protected int[] ids;

    @Autowired
    protected String[] strings;

    private List<SortString> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_stringsort);
        ARouter.getInstance().inject(this);

        initSortString();
    }

    private void initSortString() {
        if (ids == null || strings == null || ids.length != strings.length) {
            return;
        }
        list = new ArrayList<>();
        for (int i = 0;i < ids.length;i ++) {
            String sort = ChineseUtils.toPinyin(strings[i]);
//            String sort = Pinyin.toPinyin(strings[i], "");
            KLog.d("pinyin", i + " : " + sort);
            for (int j = 0;j < strings[i].length();j ++) {
                String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(strings[i].charAt(j));
                if (pinyin != null) {
                    for (int k = 0; k < pinyin.length; k++) {
                        KLog.d("pinyin4j", j + " : " + k + " : " + pinyin[k]);
                    }
                }
            }
        }
    }
}
