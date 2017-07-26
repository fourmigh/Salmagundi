package org.caojun.salmagundi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.zxing.client.android.CaptureActivity;

import org.caojun.salmagundi.ai.AIActivity;
import org.caojun.salmagundi.bankcard.BankCardActivity;
import org.caojun.salmagundi.bloodpressure.BloodPressureActivity;
import org.caojun.salmagundi.cameracolor.CameraColorActivity;
import org.caojun.salmagundi.clicaptcha.CaptchaActivity;
import org.caojun.salmagundi.clicaptcha.CaptchaDemo;
import org.caojun.salmagundi.color.Color;
import org.caojun.salmagundi.color.ColorActivity;
import org.caojun.salmagundi.color.ColorUtils;
import org.caojun.salmagundi.iflytek.FlytekActivity;
import org.caojun.salmagundi.lockpattern.CreateGestureActivity;
import org.caojun.salmagundi.lockpattern.GestureConstant;
import org.caojun.salmagundi.passwordstore.PasswordStoreActivity;
import org.caojun.salmagundi.qrcode.QRCodeActivity;
import org.caojun.salmagundi.rxjava.RxJavaActivity;
import org.caojun.salmagundi.secure.SecureActivity;
import org.caojun.salmagundi.sharecase.UserActivity;
import org.caojun.salmagundi.string.ConvertUtils;
import org.caojun.salmagundi.string.StringActivity;
import org.caojun.salmagundi.sysinfo.SysinfoActivity;
import org.caojun.salmagundi.taxicab.TaxicabActivity;
import org.caojun.salmagundi.textart.TextArtActivity;
import org.caojun.salmagundi.today.TodayActivity;
import org.caojun.salmagundi.today.TodayConstant;
import org.caojun.salmagundi.utils.DataStorageUtils;

public class MainActivity extends Activity {
    private Bitmap bmGradient;
    private final Integer[] mThumbIds = {
            R.drawable.icon_bp,
            R.drawable.icon_qrcode,
            R.drawable.icon_color,
            R.drawable.icon_bankcard,
            R.drawable.launcher_icon,
            R.drawable.icon_string,
            R.drawable.icon_secure,
            R.drawable.icon_rxjava,
            R.drawable.icon_ai,
            R.drawable.icon_textart,
            R.drawable.icon_passwordstore,
            R.drawable.icon_taxicab,
            R.drawable.icon_cameracolor,
            R.drawable.icon_sysinfo,
            R.drawable.icon_sharecase,
            R.drawable.icon_captcha,
            R.drawable.icon_today,
            R.drawable.icon_flytek
    };
    private final Integer[] mTextIds = {
            R.string.bp_title,
            R.string.qrcode_title,
            R.string.color_title,
            R.string.bankcard_title,
            R.string.zxing_title,
            R.string.string_title,
            R.string.secure_title,
            R.string.rxjava_title,
            R.string.ai_title,
            R.string.textart_title,
            R.string.passwordstore_title,
            R.string.taxicab_title,
            R.string.cc_title,
            R.string.si_title,
            R.string.sc_title,
            R.string.captcha_title,
            R.string.th_title,
            R.string.ft_title
    };
    private final Class[] mActivitys = {
            BloodPressureActivity.class,
            QRCodeActivity.class,
            ColorActivity.class,
            BankCardActivity.class,
            CaptureActivity.class,
            StringActivity.class,
            SecureActivity.class,
            RxJavaActivity.class,
            AIActivity.class,
            TextArtActivity.class,
            PasswordStoreActivity.class,
            TaxicabActivity.class,
            CameraColorActivity.class,
            SysinfoActivity.class,
            UserActivity.class,
            CaptchaDemo.class,
            TodayActivity.class,
            FlytekActivity.class
    };
    private final String[] mARouterPaths = {
            Constant.ACTIVITY_BLOODPRESSURE,
            Constant.ACTIVITY_QRCODE,
            Constant.ACTIVITY_COLOR,
            Constant.ACTIVITY_NFC,
            null,
            Constant.ACTIVITY_STRING,
            Constant.ACTIVITY_SECURE,
            Constant.ACTIVITY_RXJAVA,
            Constant.ACTIVITY_AI,
            Constant.ACTIVITY_TEXTART,
            Constant.ACTIVITY_PASSWORDSTORE,
            Constant.ACTIVITY_TAXICAB,
            Constant.ACTIVITY_CAMERACOLOR,
            Constant.ACTIVITY_SYSINFO,
            Constant.ACTIVITY_USER,
            Constant.ACTIVITY_CAPTCHA_DEMO,
            Constant.ACTIVITY_TODAY,
            Constant.ACTIVITY_FLYTEK
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridView gridView = (GridView) findViewById(R.id.gvApp);
        final AppAdapter adapter = new AppAdapter(this);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mActivitys[position].equals(PasswordStoreActivity.class)) {
                    String hostGesture = PasswordStoreActivity.class.getName();
                    byte[] gesturePassword = DataStorageUtils.loadByteArray(getApplicationContext(), GestureConstant.DataGesture, hostGesture, Byte.MIN_VALUE);
                    String gesture = ConvertUtils.stringToHex(gesturePassword);
                    ARouter.getInstance().build(mARouterPaths[position])
                            .withString("hostGesture", hostGesture)
                            .withString("gesture", gesture)
                            .navigation();
                } else if (TextUtils.isEmpty(mARouterPaths[position])) {
                    Intent intent = new Intent(MainActivity.this, mActivitys[position]);
                    MainActivity.this.startActivity(intent);
                } else {
                    ARouter.getInstance().build(mARouterPaths[position]).navigation();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        createColorIcon();
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
            return position;
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

        private class ViewHolder {
            private ImageView imageView;
            private TextView textView;
        }
    }
}
