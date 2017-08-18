package org.caojun.salmagundi.string.utils;

import android.text.TextUtils;

import com.socks.library.KLog;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by CaoJun on 2017/8/18.
 */

public class PinyinUtils {

    /**
     * 只判断首字拼音多音字
     * @param text
     * @return
     */
    public static String[] toPinyin1st(String text) {
        if (TextUtils.isEmpty(text)) {
            return null;
        }
        char c = text.charAt(0);
        String[] py = PinyinHelper.toHanyuPinyinStringArray(c);
        String[] pinyin;
        if (py == null) {
            //非汉字
            pinyin = new String[1];
            pinyin[0] = text;
        } else {
            pinyin = new String[py.length];
            for (int i = 0;i < py.length;i ++) {
                pinyin[i] = py[i] + text;
            }
        }
        return pinyin;
    }

    /**
     * 列出所有拼音的排列组合
     * @param text
     * @return
     */
    public static String[] toPinyin(String text) {
        if (TextUtils.isEmpty(text)) {
            return null;
        }
        String[][] pinyin = new String[text.length()][];
        for (int i = 0;i < text.length();i ++) {
            char c = text.charAt(i);
            String[] py = PinyinHelper.toHanyuPinyinStringArray(c);
            if (py == null) {
                //非汉字
                pinyin[i] = new String[1];
                pinyin[i][0] = String.valueOf(c);
            } else {
                pinyin[i] = py;
            }
        }
        return doCombination(pinyin);
    }

    /**
     * 两个数组做排列组合
     * @param a
     * @param b
     * @return
     */
    private static String[] doCombination(String[] a, String[] b) {
        List<String> list = new ArrayList<>();
        for (int i = 0;i < a.length;i ++) {
            for (int j = 0;j < b.length;j ++) {
                list.add(a[i] + b[j]);
            }
        }
        String[] strings = new String[list.size()];
        list.toArray(strings);
        return strings;
    }

    private static String[] doCombination(String[][] data) {
        String[] strings;
        if (data.length < 2) {
            return data[0];
        } else {
            strings = doCombination(data[0], data[1]);
            if (data.length == 2) {
                return strings;
            }
            for (int i = 2;i < data.length;i ++) {
                strings = doCombination(strings, data[i]);
            }
            return strings;
        }
    }
}
