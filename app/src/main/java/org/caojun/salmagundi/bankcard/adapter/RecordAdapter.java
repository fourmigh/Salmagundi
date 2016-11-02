package org.caojun.salmagundi.bankcard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import org.caojun.salmagundi.R;
import org.caojun.salmagundi.bankcard.pboc.Record;
import java.util.List;

/**
 * Created by CaoJun on 2016/9/7.
 */
public class RecordAdapter extends BaseAdapter {

    private Context context;
    private List<Record> list;

    public RecordAdapter(Context context, List<Record> list)
    {
        this.context = context;
        setData(list);
    }

    public void setData(List<Record> list)
    {
        this.list = list;
    }

    @Override
    public int getCount() {
        if(list == null || list.isEmpty())
        {
            return 0;
        }
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        if(list == null || list.isEmpty())
        {
            return null;
        }
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_record, null);
            holder = new ViewHolder();
            holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            holder.tvMoney = (TextView) convertView.findViewById(R.id.tvMoney);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        Record record = (Record) getItem(position);
        holder.tvTime.setText(record.getDate() + " " + record.getTime());
        holder.tvMoney.setText(record.getTranMoney());
        return convertView;
    }

    private class ViewHolder
    {
        TextView tvTime, tvMoney;
    }
}
