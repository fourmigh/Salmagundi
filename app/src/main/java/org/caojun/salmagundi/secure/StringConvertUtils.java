package org.caojun.salmagundi.secure;

import android.text.TextUtils;

/**
 * 字符串转换
 * Created by CaoJun on 2016/12/27.
 */

public class StringConvertUtils {

    public static String toUpperCase(String text)
    {
        if(TextUtils.isEmpty(text))
        {
            return text;
        }
        return text.toUpperCase();
    }

    public static String toLowerCase(String text)
    {
        if(TextUtils.isEmpty(text))
        {
            return text;
        }
        return text.toLowerCase();
    }
}
