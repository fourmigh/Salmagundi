package org.caojun.salmagundi.calendar.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by CaoJun on 2017/8/3.
 */

public class CalendarUtils {

    public static final int ReequestCode_ReadCalendar = 1;
    public static final int ReequestCode_WriteCalendar = 2;

    //Reminders

    public static Cursor getAccounts(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            Cursor cursor = activity.getContentResolver().query(CalendarContract.Calendars.CONTENT_URI, null, null, null, null);
            return cursor;
        }
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CALENDAR}, ReequestCode_ReadCalendar);
        return null;
    }

    public static Cursor getEvents(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            Cursor cursor = activity.getContentResolver().query(CalendarContract.Events.CONTENT_URI, null, null, null, null);
            return cursor;
        }
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CALENDAR}, ReequestCode_ReadCalendar);
        return null;
    }

    public static Cursor getEvents(Activity activity, String accountID) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            Cursor cursor = activity.getContentResolver().query(CalendarContract.Events.CONTENT_URI, null, CalendarContract.EventsEntity.CALENDAR_ID + "=?", new String[]{accountID}, null);
            return cursor;
        }
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CALENDAR}, ReequestCode_ReadCalendar);
        return null;
    }
}
