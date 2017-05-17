package org.caojun.salmagundi.bloodpressure.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.caojun.salmagundi.R;
import org.caojun.salmagundi.bloodpressure.BloodPressureUtils;
import org.caojun.salmagundi.bloodpressure.ormlite.BloodPressure;
import org.caojun.salmagundi.utils.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by fourm on 2017/5/9.
 */

public class BloodPressureAdapter extends BaseAdapter {

    private Context context;
    private List<BloodPressure> list;
    private final String dateFormat = "yyyy/MM/dd HH:mm:ss";
    private String[] type;

    public BloodPressureAdapter(Context context, List<BloodPressure> list) {
        this.context = context;
        setData(list);
        type = context.getResources().getStringArray(R.array.bloodpressure_type);
    }

    public void setData(List<BloodPressure> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_bloodpressure, null);
            holder = new ViewHolder();
            holder.tvTime = (TextView) view.findViewById(R.id.tvTime);
            holder.tvRemark = (TextView) view.findViewById(R.id.tvRemark);
            holder.tvHigh = (TextView) view.findViewById(R.id.tvHigh);
            holder.tvLow = (TextView) view.findViewById(R.id.tvLow);
            holder.tvPulse = (TextView) view.findViewById(R.id.tvPulse);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        BloodPressure bloodPressure = (BloodPressure) getItem(i);
        String time = TimeUtils.getTime(dateFormat, bloodPressure.getTime());
        holder.tvRemark.setText(null);
        holder.tvHigh.setText(null);
        holder.tvLow.setText(null);
        holder.tvPulse.setText(null);
        switch (bloodPressure.getType()) {
            case BloodPressure.Type_BloodPressure:
                holder.tvRemark.setText(type[BloodPressureUtils.getType(bloodPressure.getHigh(), bloodPressure.getLow())]);
                holder.tvHigh.setText(String.valueOf(bloodPressure.getHigh()));
                holder.tvLow.setText(String.valueOf(bloodPressure.getLow()));
                holder.tvPulse.setText(String.valueOf(bloodPressure.getPulse()));
                break;
            case BloodPressure.Type_Medicine:
                holder.tvRemark.setText(R.string.bp_medicine);
                break;
            case BloodPressure.Type_Weight:
                holder.tvRemark.setText(context.getString(R.string.bp_weight_unit, String.valueOf(bloodPressure.getWeight())));
                break;
        }
        holder.tvTime.setText(time);

        return view;
    }

    private class ViewHolder {
        TextView tvTime, tvRemark, tvHigh, tvLow, tvPulse;
    }
}
