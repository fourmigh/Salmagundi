package org.caojun.salmagundi.utils;

import android.text.TextUtils;

/**
 * 各种转换工具类
 * Created by CaoJun on 2016/10/31.
 */

public class FormatUtils {

    public static String int2Hex(int i) {
        String hex = Integer.toHexString(i);
        if(!TextUtils.isEmpty(hex) && hex.length() == 1)
        {
            return "0" + hex;
        }
        return hex;
    }
}
