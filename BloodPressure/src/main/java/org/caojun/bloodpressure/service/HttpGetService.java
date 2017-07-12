package org.caojun.bloodpressure.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import org.caojun.bloodpressure.Constant;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by CaoJun on 2017/7/10.
 */

/**
 * 通用的HttpGet服务，以广播形式返回结果
 */
public class HttpGetService extends Service {

    public static final String BroadcastAction = "org.caojun.httpget.RECEIVER";
    public static final String RESULT = "RESULT";
    public static final String URL = "URL";

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
                String url = intent.getStringExtra(URL);
                doGet(url);
            }
        }.start();
    }

    private void doGet(String url) {
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

                Intent intent = new Intent(BroadcastAction);
                intent.putExtra(RESULT, result);
                sendBroadcast(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
