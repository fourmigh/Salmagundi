package org.caojun.salmagundi.calendar.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by CaoJun on 2017/8/3.
 */

public class CalendarUtils {
    private static String calanderURL = "content://com.android.calendar/calendars";
    private static String calanderEventURL = "content://com.android.calendar/events";
    private static String calanderRemiderURL = "content://com.android.calendar/reminders";

    public static Cursor getAccounts(Context context) {
        Cursor cursor = context.getContentResolver().query(Uri.parse(calanderURL), null, null, null, null);
        return cursor;
    }

    public static Cursor getEvents(Context context) {
        Cursor cursor = context.getContentResolver().query(Uri.parse(calanderEventURL), null, null, null, null);
        return cursor;
    }
}
