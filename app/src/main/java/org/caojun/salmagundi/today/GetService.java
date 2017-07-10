package org.caojun.salmagundi.today;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by CaoJun on 2017/7/10.
 */

public class GetService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        runGet(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    private void runGet(final Intent intent) {
        new Thread() {
            @Override
            public void run() {
                String month = intent.getStringExtra("month");
                String day = intent.getStringExtra("day");
                doGet(month, day);
            }
        }.start();
    }

    private void doGet(String month, String day) {
        String url = TodayConstant.URL + "?appkey=" + TodayConstant.APPKEY + "&month=" + month + "&day=" + day;
        try {
            HttpURLConnection connection = (HttpURLConnection)(new URL(url)).openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream is = connection.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = -1;
                while ((len = is.read(buffer)) != -1) {
                    bos.write(buffer, 0, len);
                }
                String result = new String(bos.toByteArray());
                is.close();

                Intent intent = new Intent(TodayConstant.BroadcastAction);
                intent.putExtra("result", result);
                sendBroadcast(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
