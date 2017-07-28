package org.caojun.bloodpressure.broadcast;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import org.caojun.bloodpressure.R;
import org.caojun.bloodpressure.activity.BloodPressureDetailActivity;
import org.caojun.bloodpressure.activity.NotificaitonSettings;

/**
 * Created by CaoJun on 2017/7/28.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences mSharedPreferences = context.getSharedPreferences(NotificaitonSettings.PREFER_NAME, Context.MODE_PRIVATE);
        boolean notificaiton_preference = mSharedPreferences.getBoolean("notificaiton_preference", false);
        if (!notificaiton_preference) {
            return;
        }

        String msg = intent.getStringExtra("msg");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setAutoCancel(true);//点击后消失
        builder.setSmallIcon(R.drawable.ic_launcher);//设置通知栏消息标题的头像
        builder.setDefaults(NotificationCompat.DEFAULT_SOUND);//设置通知铃声
        builder.setContentText(msg);//通知内容
        builder.setContentTitle(context.getString(R.string.app_name));
        //利用PendingIntent来包装我们的intent对象,使其延迟跳转
        Intent i = new Intent(context, BloodPressureDetailActivity.class);//将要跳转的界面
        i.putExtra("type", intent.getIntExtra("type", 0));
        PendingIntent intentPend = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(intentPend);
        NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}
