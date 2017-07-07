package org.caojun.salmagundi.bloodpressure;

import android.content.Context;
import android.os.Environment;

import org.caojun.salmagundi.bloodpressure.ormlite.BloodPressure;
import org.caojun.salmagundi.bloodpressure.ormlite.BloodPressureDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by CaoJun on 2017/5/17.
 */

public class BloodPressureUtils {
    public static int getType(int high, int low) {
        if (high >= 180 || low >= 110) {
            return 4;//严重
        }

        if ((high >= 160 && high < 180) || (low >= 100 && low < 110)) {
            return 3;//中度
        }

        if ((high >= 140 && high < 160) || (low >= 90 && low < 100)) {
            return 2;//轻度
        }

        if ((high >= 100 && high < 140) || (low >= 60 && low < 90)) {
            return 1;//正常
        }

        if (high <= 100 || low <= 60) {
            return 0;//低血压
        }

        return 5;
    }

    private static File getFile() {
        File file = new File(Environment.getExternalStorageDirectory() + "/BloodPressure");
        return file;
    }

    public static boolean exportFromDB(Context context) {
        List<BloodPressure> list = BloodPressureDatabase.getInstance(context).query();
        if (list == null || list.isEmpty()) {
            return false;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0;i < list.size();i ++) {
            BloodPressure bloodPressure = list.get(i);
            sb.append(bloodPressure.toString());
        }
        try {
            File file = getFile();
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] bytes = sb.toString().getBytes();
            fileOutputStream.write(bytes);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
