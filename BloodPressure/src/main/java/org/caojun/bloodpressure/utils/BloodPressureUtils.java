package org.caojun.bloodpressure.utils;

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
}
