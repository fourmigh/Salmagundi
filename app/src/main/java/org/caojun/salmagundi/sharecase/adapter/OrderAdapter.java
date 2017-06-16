package org.caojun.salmagundi.sharecase.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.caojun.salmagundi.R;
import org.caojun.salmagundi.sharecase.ormlite.Order;

import java.util.List;

/**
 * Created by CaoJun on 2017/6/14.
 */

/**
 * 订单
 */
public class OrderAdapter extends BaseAdapter {
    private Context context;
    private List<Order> list;

    public OrderAdapter(Context context, List<Order> list) {
        this.context = context;
        setData(list);
    }

    public void setData(List<Order> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        if(list == null || list.isEmpty()) {
            return 0;
        }
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        if(list == null || list.isEmpty()) {
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
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_leftright, null);
            holder = new ViewHolder();
            holder.tvLeft = (TextView) convertView.findViewById(R.id.tvLeft);
            holder.tvRight = (TextView) convertView.findViewById(R.id.tvRight);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Order order = (Order) getItem(position);
        //TODO

        return convertView;
    }

    private class ViewHolder {
        TextView tvLeft, tvRight;
    }
}
