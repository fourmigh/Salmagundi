package org.caojun.salmagundi.string;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import org.caojun.salmagundi.Constant;
import org.caojun.salmagundi.R;
import org.caojun.salmagundi.string.utils.PinyinUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by CaoJun on 2017/8/17.
 */

@Route(path = Constant.ACTIVITY_STRINGSORT)
public class StringSortActivity extends Activity {

    private class SortString {
        private int id;
        private String text;
        private String sort;

        public SortString(int id, String text, String sort) {
            this.id = id;
            this.text = text;
            this.sort = sort;
        }
    }

    private class SortStringComparator implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            SortString s1 = (SortString) o1;
            SortString s2 = (SortString) o2;
            return s1.sort.compareTo(s2.sort);
        }
    }

    private class SortAdapter extends BaseAdapter implements StickyListHeadersAdapter {

        private Context context;
        private SortString[] list;

        public SortAdapter(Context context, SortString[] list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public View getHeaderView(int position, View convertView, ViewGroup parent) {
            HeaderViewHolder holder;
            if (convertView == null) {
                holder = new HeaderViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_sort_header, parent, false);
                holder.tvHeader = (TextView) convertView.findViewById(R.id.tvHeader);
                convertView.setTag(holder);
            } else {
                holder = (HeaderViewHolder) convertView.getTag();
            }
            SortString ss = (SortString) getItem(position);
            String header = ss.sort.substring(0, 1).toUpperCase();
            holder.tvHeader.setText(header);
            return convertView;
        }

        @Override
        public long getHeaderId(int position) {
            SortString ss = (SortString) getItem(position);
            String header = ss.sort.substring(0, 1).toUpperCase();
            char c = header.charAt(0);
            return c;
        }

        @Override
        public int getCount() {
            if(list == null) {
                return 0;
            }
            return list.length;
        }

        @Override
        public Object getItem(int position) {
            if(list == null) {
                return 0;
            }
            return list[position];
        }

        @Override
        public long getItemId(int position) {
            if(list == null) {
                return Long.MIN_VALUE;
            }
            return list[position].id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_sort, null);
                holder = new ViewHolder();
                holder.tvText = (TextView) convertView.findViewById(R.id.tvText);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            SortString ss = (SortString) getItem(position);
            holder.tvText.setText(ss.text);

            return convertView;
        }

        private class HeaderViewHolder {
            TextView tvHeader;
        }

        private class ViewHolder {
            TextView tvText;
        }
    }

    @Autowired
    protected int[] ids;

    @Autowired
    protected String[] strings;

    private SortString[] sorts;//排好序的SortString对象
    private StickyListHeadersListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_stringsort);
        ARouter.getInstance().inject(this);

        listView = (StickyListHeadersListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SortString ss = sorts[position];
                Intent intent = new Intent();
                intent.putExtra("id", ss.id);
                intent.putExtra("text", ss.text);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        initSortString();

        listView.setAdapter(new SortAdapter(this, sorts));
    }

    private void initSortString() {
        if (ids == null || strings == null || ids.length != strings.length) {
            return;
        }
        List<SortString> list = new ArrayList<>();
        for (int i = 0;i < ids.length;i ++) {
            String[] pinyin = PinyinUtils.toPinyin1st(strings[i]);
            for (int j = 0;j < pinyin.length;j ++) {
                SortString ss = new SortString(ids[i], strings[i], pinyin[j]);
                list.add(ss);
            }
        }

        sorts = new SortString[list.size()];
        list.toArray(sorts);
        Arrays.sort(sorts, new SortStringComparator());
    }


}
