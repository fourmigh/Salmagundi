package org.caojun.salmagundi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.client.android.CaptureActivity;

import org.caojun.salmagundi.bankcard.BankCardActivity;
import org.caojun.salmagundi.color.Color;
import org.caojun.salmagundi.color.ColorActivity;
import org.caojun.salmagundi.color.ColorUtils;
import org.caojun.salmagundi.qrcode.QRCodeActivity;
import org.caojun.salmagundi.rxjava.RxJavaActivity;
import org.caojun.salmagundi.secure.SecureActivity;
import org.caojun.salmagundi.string.StringActivity;
import org.caojun.salmagundi.utils.DataStorageUtils;

public class MainActivity extends AppCompatActivity {
    private TextView tvInfo;
    private Bitmap bmGradient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvInfo = (TextView) this.findViewById(R.id.tvInfo);
        GridView gridView = (GridView) findViewById(R.id.gvApp);
        final AppAdapter adapter = new AppAdapter(this);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, (Class) adapter.getItem(position));
                MainActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        createColorIcon();
        tvInfo.setText(getSystemInfo());
    }

    private String getSystemInfo() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        StringBuffer sb = new StringBuffer();
        sb.append("widthPixels: " + metrics.widthPixels);
        sb.append("\n" + "heightPixels: " + metrics.heightPixels);
        sb.append("\n" + "density: " + metrics.density);
        sb.append("\n" + "densityDpi: " + metrics.densityDpi);
        sb.append("\n" + "scaledDensity: " + metrics.scaledDensity);
        sb.append("\n" + "xdpi: " + metrics.xdpi);
        sb.append("\n" + "ydpi: " + metrics.ydpi);
        sb.append("\n" + "DPI: " + this.getString(R.string.dpi));
        return sb.toString();
    }

    /**
     * 更新渐变色按钮图标
     */
    private void createColorIcon() {
        Color[] color = ColorUtils.getSavedColors(this);
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.icon_color);
        int density = (int) getResources().getDisplayMetrics().density;
        Color[] colors = ColorUtils.getGradientColor(color[0], color[1], bitmap.getWidth() * density);
        bmGradient = ColorUtils.createGradientImage(colors, bitmap.getWidth() * density, bitmap.getHeight() * density); /*        if(bmGradient != null) { Drawable drawable = new BitmapDrawable(bmGradient); drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); btnColor.setCompoundDrawables(null, drawable, null, null); }*/
        Integer[] intColorStart = new Integer[]{color[0].getAlpha(), color[0].getRed(), color[0].getGreen(), color[0].getBlue()};
        Integer[] intColorEnd = new Integer[]{color[1].getAlpha(), color[1].getRed(), color[1].getGreen(), color[1].getBlue()};
        DataStorageUtils.saveIntArray(this, "GradientColor", "colorStart", intColorStart);
        DataStorageUtils.saveIntArray(this, "GradientColor", "colorEnd", intColorEnd);
    }

    private class AppAdapter extends BaseAdapter {
        public AppAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return mThumbIds.length;
        }

        public Object getItem(int position) {
            return mActivitys[position];
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_app, null);
                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                holder.textView = (TextView) convertView.findViewById(R.id.textView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.textView.setText(mTextIds[position]);
            if (position == 1 && bmGradient != null) {
                holder.imageView.setImageBitmap(bmGradient);
            } else {
                holder.imageView.setImageResource(mThumbIds[position]);
            }
            return convertView;
        }

        private Context mContext;

        private Integer[] mThumbIds = {
                R.drawable.icon_qrcode, R.drawable.icon_color, R.drawable.icon_bankcard, R.drawable.launcher_icon, R.drawable.icon_string, R.drawable.icon_secure, R.drawable.icon_rxjava};
        private Integer[] mTextIds = {
                R.string.qrcode_title, R.string.color_title, R.string.bankcard_title, R.string.zxing_title, R.string.string_title, R.string.secure_title, R.string.rxjava_title
        };
        private Class[] mActivitys = {
                QRCodeActivity.class, ColorActivity.class, BankCardActivity.class, CaptureActivity.class, StringActivity.class, SecureActivity.class, RxJavaActivity.class
        };

        private class ViewHolder {
            private ImageView imageView;
            private TextView textView;
        }
    }
}
