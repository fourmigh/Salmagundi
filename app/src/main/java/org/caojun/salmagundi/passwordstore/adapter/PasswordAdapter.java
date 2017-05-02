package org.caojun.salmagundi.passwordstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import org.caojun.salmagundi.R;
import org.caojun.salmagundi.passwordstore.ormlite.Password;
import java.util.List;

/**
 * 密码仓库Adapter
 * Created by CaoJun on 2017/2/15.
 */

public class PasswordAdapter extends BaseAdapter {

    private Context context;
    private List<Password> list;

    public PasswordAdapter(Context context, List<Password> list) {
        this.context = context;
        setData(list);
    }

    public void setData(List<Password> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_passowrdstore, null);
            holder = new ViewHolder();
            holder.tvCompany = (TextView) convertView.findViewById(R.id.tvCompany);
            holder.tvUrl = (TextView) convertView.findViewById(R.id.tvUrl);
            holder.tvAccount = (TextView) convertView.findViewById(R.id.tvAccount);
            holder.tvPassword = (TextView) convertView.findViewById(R.id.tvPassword);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Password password = (Password) getItem(position);
        holder.tvCompany.setText(password.getCompany());
        holder.tvUrl.setText(password.getUrl());
        holder.tvAccount.setText(password.getAccount());

        byte length = password.getRealLength();
        StringBuffer sb = new StringBuffer(length);
        for(byte i = 0;i < length;i ++) {
            sb.append("*");
        }
        holder.tvPassword.setText(sb.toString());
        return convertView;
    }

    private class ViewHolder {
        TextView tvCompany, tvUrl, tvAccount, tvPassword;
    }
}
