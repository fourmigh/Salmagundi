package org.caojun.salmagundi.secure;

import android.text.TextUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

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

    public static String[] toPinyinStringArray(char c)
    {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);
        format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
        try {
            return PinyinHelper.toHanyuPinyinStringArray(c, format);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 汉字转拼音
     * @param c
     * @param first 只返回第一个
     * @return
     */
    public static String toPinyinString(char c, boolean first)
    {
        String[] pinyin = toPinyinStringArray(c);
        if(pinyin == null)
        {
            return null;
        }
        if(pinyin.length == 1)
        {
            return pinyin[0];
        }
        StringBuffer sb = new StringBuffer();
        sb.append(pinyin[0]);
        if(first)
        {
            return sb.toString();
        }
        sb.append("(");
        for(int i = 1;i < pinyin.length;i ++)
        {
            sb.append(pinyin[i]);
            if(i < pinyin.length - 1)
            {
                sb.append(",");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    public static String toPinyinString(String text)
    {
        if(TextUtils.isEmpty(text))
        {
            return text;
        }
        StringBuffer sb = new StringBuffer();
        for(int i = 0;i < text.length();i ++)
        {
            char c = text.charAt(i);
            String pinyin = toPinyinString(c, true);
            if(TextUtils.isEmpty(pinyin))
            {
                sb.append(c);
            }
            else
            {
                sb.append(pinyin);
            }
            if(i < text.length() - 1)
            {
                sb.append(" ");
            }
        }
        return sb.toString();
    }
}
