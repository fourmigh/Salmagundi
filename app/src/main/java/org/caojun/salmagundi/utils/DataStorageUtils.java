package org.caojun.salmagundi.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

/**
 * 存储工具类
 * Created by CaoJun on 2016/11/3.
 */

public class DataStorageUtils {

    private static boolean isIllegal(Context context, String name, String key)
    {
        return context == null || TextUtils.isEmpty(name) || TextUtils.isEmpty(key);
    }

    public static Editor getEditor(Context context, String name, String key)
    {
        if(isIllegal(context, name, key))
        {
            return null;
        }
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return preferences.edit();
    }

    public static SharedPreferences getSharedPreferences(Context context, String name, String key)
    {
        if(isIllegal(context, name, key))
        {
            return null;
        }
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static boolean saveBoolean(Context context, String name, String key, boolean value)
    {
        Editor editor = getEditor(context, name, key);
        if(editor == null)
        {
            return false;
        }
        editor.putBoolean(key, value);
        editor.commit();
        return true;
    }

    public static Boolean loadBoolean(Context context, String name, String key, boolean defValue)
    {
        SharedPreferences preferences = getSharedPreferences(context, name, key);
        if(preferences == null)
        {
            return null;
        }
        return preferences.getBoolean(key, defValue);
    }

    public static boolean saveBooleanArray(Context context, String name, String key, boolean[] values)
    {
        Editor editor = getEditor(context, name, key);
        if(editor == null || values == null)
        {
            return false;
        }
        editor.putInt(key + ".length", values.length);
        for(int i = 0;i < values.length;i ++)
        {
            editor.putBoolean(key + "." + i, values[i]);
        }
        editor.commit();
        return true;
    }

    public static Boolean[] loadBooleanArray(Context context, String name, String key, boolean defValue)
    {
        SharedPreferences preferences = getSharedPreferences(context, name, key);
        if(preferences == null)
        {
            return null;
        }
        int length = preferences.getInt(key + ".length", 0);
        if(length < 1)
        {
            return null;
        }
        Boolean[] values = new Boolean[length];
        for(int i = 0;i < length;i ++)
        {
            values[i] = preferences.getBoolean(key + "." + i, defValue);
        }
        return values;
    }

    public static boolean saveFloat(Context context, String name, String key, float value)
    {
        Editor editor = getEditor(context, name, key);
        if(editor == null)
        {
            return false;
        }
        editor.putFloat(key, value);
        editor.commit();
        return true;
    }

    public static Float loadFloat(Context context, String name, String key, float defValue)
    {
        SharedPreferences preferences = getSharedPreferences(context, name, key);
        if(preferences == null)
        {
            return null;
        }
        return preferences.getFloat(key, defValue);
    }

    public static boolean saveFloatArray(Context context, String name, String key, float[] values)
    {
        Editor editor = getEditor(context, name, key);
        if(editor == null || values == null)
        {
            return false;
        }
        editor.putInt(key + ".length", values.length);
        for(int i = 0;i < values.length;i ++)
        {
            editor.putFloat(key + "." + i, values[i]);
        }
        editor.commit();
        return true;
    }

    public static Float[] loadFloatArray(Context context, String name, String key, float defValue)
    {
        SharedPreferences preferences = getSharedPreferences(context, name, key);
        if(preferences == null)
        {
            return null;
        }
        int length = preferences.getInt(key + ".length", 0);
        if(length < 1)
        {
            return null;
        }
        Float[] values = new Float[length];
        for(int i = 0;i < length;i ++)
        {
            values[i] = preferences.getFloat(key + "." + i, defValue);
        }
        return values;
    }

    public static boolean saveInt(Context context, String name, String key, int value)
    {
        Editor editor = getEditor(context, name, key);
        if(editor == null)
        {
            return false;
        }
        editor.putInt(key, value);
        editor.commit();
        return true;
    }

    public static Integer loadInt(Context context, String name, String key, int defValue)
    {
        SharedPreferences preferences = getSharedPreferences(context, name, key);
        if(preferences == null)
        {
            return null;
        }
        return preferences.getInt(key, defValue);
    }

    public static boolean saveIntArray(Context context, String name, String key, int[] values)
    {
        Editor editor = getEditor(context, name, key);
        if(editor == null || values == null)
        {
            return false;
        }
        editor.putInt(key + ".length", values.length);
        for(int i = 0;i < values.length;i ++)
        {
            editor.putInt(key + "." + i, values[i]);
        }
        editor.commit();
        return true;
    }

    public static boolean saveIntArray(Context context, String name, String key, Integer[] values)
    {
        Editor editor = getEditor(context, name, key);
        if(editor == null || values == null)
        {
            return false;
        }
        editor.putInt(key + ".length", values.length);
        for(int i = 0;i < values.length;i ++)
        {
            editor.putInt(key + "." + i, values[i]);
        }
        editor.commit();
        return true;
    }

    public static Integer[] loadIntArray(Context context, String name, String key, int defValue)
    {
        SharedPreferences preferences = getSharedPreferences(context, name, key);
        if(preferences == null)
        {
            return null;
        }
        int length = preferences.getInt(key + ".length", 0);
        if(length < 1)
        {
            return null;
        }
        Integer[] values = new Integer[length];
        for(int i = 0;i < length;i ++)
        {
            values[i] = preferences.getInt(key + "." + i, defValue);
        }
        return values;
    }

    public static boolean saveLong(Context context, String name, String key, long value)
    {
        Editor editor = getEditor(context, name, key);
        if(editor == null)
        {
            return false;
        }
        editor.putLong(key, value);
        editor.commit();
        return true;
    }

    public static Long loadLong(Context context, String name, String key, long defValue)
    {
        SharedPreferences preferences = getSharedPreferences(context, name, key);
        if(preferences == null)
        {
            return null;
        }
        return preferences.getLong(key, defValue);
    }

    public static boolean saveLongArray(Context context, String name, String key, long[] values)
    {
        Editor editor = getEditor(context, name, key);
        if(editor == null || values == null)
        {
            return false;
        }
        editor.putInt(key + ".length", values.length);
        for(int i = 0;i < values.length;i ++)
        {
            editor.putLong(key + "." + i, values[i]);
        }
        editor.commit();
        return true;
    }

    public static Long[] loadLongArray(Context context, String name, String key, long defValue)
    {
        SharedPreferences preferences = getSharedPreferences(context, name, key);
        if(preferences == null)
        {
            return null;
        }
        int length = preferences.getInt(key + ".length", 0);
        if(length < 1)
        {
            return null;
        }
        Long[] values = new Long[length];
        for(int i = 0;i < length;i ++)
        {
            values[i] = preferences.getLong(key + "." + i, defValue);
        }
        return values;
    }

    public static boolean saveString(Context context, String name, String key, String value)
    {
        Editor editor = getEditor(context, name, key);
        if(editor == null)
        {
            return false;
        }
        editor.putString(key, value);
        editor.commit();
        return true;
    }

    public static String loadString(Context context, String name, String key, String defValue)
    {
        SharedPreferences preferences = getSharedPreferences(context, name, key);
        if(preferences == null)
        {
            return null;
        }
        return preferences.getString(key, defValue);
    }

    public static boolean saveStringArray(Context context, String name, String key, String[] values)
    {
        Editor editor = getEditor(context, name, key);
        if(editor == null || values == null)
        {
            return false;
        }
        editor.putInt(key + ".length", values.length);
        for(int i = 0;i < values.length;i ++)
        {
            editor.putString(key + "." + i, values[i]);
        }
        editor.commit();
        return true;
    }

    public static String[] loadStringArray(Context context, String name, String key, String defValue)
    {
        SharedPreferences preferences = getSharedPreferences(context, name, key);
        if(preferences == null)
        {
            return null;
        }
        int length = preferences.getInt(key + ".length", 0);
        if(length < 1)
        {
            return null;
        }
        String[] values = new String[length];
        for(int i = 0;i < length;i ++)
        {
            values[i] = preferences.getString(key + "." + i, defValue);
        }
        return values;
    }

    public static boolean saveByte(Context context, String name, String key, byte value)
    {
        Editor editor = getEditor(context, name, key);
        if(editor == null)
        {
            return false;
        }
        editor.putInt(key, value);
        editor.commit();
        return true;
    }

    public static Byte loadByte(Context context, String name, String key, byte defValue)
    {
        SharedPreferences preferences = getSharedPreferences(context, name, key);
        if(preferences == null)
        {
            return null;
        }
        Integer value = preferences.getInt(key, defValue);
        return Byte.valueOf(value.byteValue());
    }

    public static boolean saveByteArray(Context context, String name, String key, byte[] values)
    {
        Editor editor = getEditor(context, name, key);
        if(editor == null || values == null)
        {
            return false;
        }
        editor.putInt(key + ".length", values.length);
        for(int i = 0;i < values.length;i ++)
        {
            editor.putInt(key + "." + i, values[i]);
        }
        editor.commit();
        return true;
    }

    public static byte[] loadByteArray(Context context, String name, String key, byte defValue)
    {
        SharedPreferences preferences = getSharedPreferences(context, name, key);
        if(preferences == null)
        {
            return null;
        }
        int length = preferences.getInt(key + ".length", 0);
        if(length < 1)
        {
            return null;
        }
        byte[] values = new byte[length];
        for(int i = 0;i < length;i ++)
        {
            Integer value = preferences.getInt(key + "." + i, defValue);
            values[i] = Byte.valueOf(value.byteValue());
        }
        return values;
    }
}
