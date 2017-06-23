package org.caojun.salmagundi.utils;

import android.text.TextUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间工具类
 * Created by fourm on 2017/5/10.
 */

public class TimeUtils {

    private static final String LocalTimeZone = "GMT+8";

    private static SimpleDateFormat getSimpleDateFormat(String dateFormat) {
        return getSimpleDateFormat(dateFormat, LocalTimeZone);
    }

    private static SimpleDateFormat getSimpleDateFormat(String dateFormat, String timeZone) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(dateFormat);
            df.setTimeZone(TimeZone.getTimeZone(timeZone));
            return df;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getTime(String dateFormat, String timeZone, long time) {
        if (TextUtils.isEmpty(dateFormat)) {
            return null;
        }
        SimpleDateFormat df = getSimpleDateFormat(dateFormat, timeZone);
        if (df == null) {
            return null;
        }
        Date date = new Date(time);
        return df.format(date);
    }

    public static String getTime(String dateFormat, long time) {
        return getTime(dateFormat, LocalTimeZone, time);
    }

    public static String getTime(String dateFormat) {
        return getTime(dateFormat, LocalTimeZone);
    }

    public static String getTime(String dateFormat, String timeZone) {
        return getTime(dateFormat, timeZone, getTime());
    }

    public static long getTime() {
        Date date = new Date();
        return date.getTime();
    }

    public static long getDays(long timeEnd, long timeStart) {
        long time = timeEnd - timeStart;
        return time / (1000 * 60 * 60 * 24);
    }
}
