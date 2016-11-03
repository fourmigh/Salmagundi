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

    public boolean saveBoolean(Context context, String name, String key, boolean value)
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

    public Boolean loadBoolean(Context context, String name, String key, boolean defValue)
    {
        SharedPreferences preferences = getSharedPreferences(context, name, key);
        if(preferences == null)
        {
            return null;
        }
        return preferences.getBoolean(key, defValue);
    }

    public boolean saveBooleanArray(Context context, String name, String key, boolean values[])
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

    public Boolean[] loadBooleanArray(Context context, String name, String key, boolean defValue)
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

    public boolean saveFloat(Context context, String name, String key, float value)
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

    public Float loadFloat(Context context, String name, String key, float defValue)
    {
        SharedPreferences preferences = getSharedPreferences(context, name, key);
        if(preferences == null)
        {
            return null;
        }
        return preferences.getFloat(key, defValue);
    }

    public boolean saveFloatArray(Context context, String name, String key, float values[])
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

    public Float[] loadFloatArray(Context context, String name, String key, float defValue)
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

    public boolean saveInt(Context context, String name, String key, int value)
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

    public Integer loadInt(Context context, String name, String key, int defValue)
    {
        SharedPreferences preferences = getSharedPreferences(context, name, key);
        if(preferences == null)
        {
            return null;
        }
        return preferences.getInt(key, defValue);
    }

    public boolean saveIntArray(Context context, String name, String key, int values[])
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

    public Integer[] loadIntArray(Context context, String name, String key, int defValue)
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

    public boolean saveLong(Context context, String name, String key, long value)
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

    public Long loadLong(Context context, String name, String key, long defValue)
    {
        SharedPreferences preferences = getSharedPreferences(context, name, key);
        if(preferences == null)
        {
            return null;
        }
        return preferences.getLong(key, defValue);
    }

    public boolean saveLongArray(Context context, String name, String key, long values[])
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

    public Long[] loadLongArray(Context context, String name, String key, long defValue)
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

    public boolean saveString(Context context, String name, String key, String value)
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

    public String loadString(Context context, String name, String key, String defValue)
    {
        SharedPreferences preferences = getSharedPreferences(context, name, key);
        if(preferences == null)
        {
            return null;
        }
        return preferences.getString(key, defValue);
    }

    public boolean saveStringArray(Context context, String name, String key, String values[])
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

    public String[] loadStringArray(Context context, String name, String key, String defValue)
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
}
