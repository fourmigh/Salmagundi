package org.caojun.salmagundi.calendar.adapter;

import android.content.Context;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import org.caojun.salmagundi.R;

/**
 * Created by CaoJun on 2017/8/3.
 */

public class AccountAdapter extends BaseAdapter {

    private Context context;
    private Cursor cursor;

    public AccountAdapter(Context context, Cursor cursor) {
        this.context = context;
        setData(cursor);
    }

    public void setData(Cursor cursor) {
        this.cursor = cursor;
    }

    @Override
    public int getCount() {
        if(cursor == null || cursor.getCount() < 1) {
            return 0;
        }
        return cursor.getCount();
    }

    @Override
    public Object getItem(int i) {
        if(cursor == null || cursor.getCount() < 1) {
            return null;
        }
        cursor.moveToPosition(i);
        return cursor;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_calendar_account, null);
            holder = new ViewHolder();
            holder.tvAccountName = (TextView) view.findViewById(R.id.tvAccountName);
            holder.tvAccountType = (TextView) view.findViewById(R.id.tvAccountType);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Cursor cursor = (Cursor) getItem(i);
        holder.tvAccountName.setText(cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.NAME)));
        holder.tvAccountType.setText(cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.ACCOUNT_TYPE)));

        return view;
    }

    private class ViewHolder {
        TextView tvAccountName, tvAccountType;
    }
}
