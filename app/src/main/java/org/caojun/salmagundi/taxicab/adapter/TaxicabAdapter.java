package org.caojun.salmagundi.taxicab.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import org.caojun.salmagundi.R;
import org.caojun.salmagundi.taxicab.ormlite.Taxicab;
import java.util.List;

/**
 * Created by fourm on 2017/5/3.
 */

public class TaxicabAdapter extends BaseAdapter {

    private Context context;
    private List<Taxicab> list;

    public TaxicabAdapter(Context context, List<Taxicab> list) {
        this.context = context;
        setData(list);
    }

    public void setData(List<Taxicab> list) {
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
    public Object getItem(int i) {
        if(list == null || list.isEmpty()) {
            return null;
        }
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_taxicab, null);
            holder = new ViewHolder();
            holder.tvAB = (TextView) view.findViewById(R.id.tvAB);
            holder.tvA = (TextView) view.findViewById(R.id.tvA);
            holder.tvB = (TextView) view.findViewById(R.id.tvB);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Taxicab taxicab = (Taxicab) getItem(i);
        holder.tvAB.setText(taxicab.getTaxicab().toString());
        holder.tvA.setText(taxicab.getA().toString());
        holder.tvB.setText(taxicab.getB().toString());

        return view;
    }

    private class ViewHolder {
        TextView tvAB, tvA, tvB;
    }
}
