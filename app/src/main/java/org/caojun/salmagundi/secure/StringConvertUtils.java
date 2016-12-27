package org.caojun.salmagundi.secure;

import android.text.TextUtils;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

import org.apache.commons.lang.StringUtils;

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

    public static String toASCII(String text) {
        if(TextUtils.isEmpty(text))
        {
            return text;
        }
        StringBuffer sb = new StringBuffer();
        byte[] bytes = text.getBytes();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toHexString(bytes[i] & 0xff));
        }
        return sb.toString();
    }

    /**
     * 十进制转二进制
     * @param text
     * @return
     */
    public static String toBinary(String text)
    {
        if(TextUtils.isEmpty(text) || !StringUtils.isNumeric(text))
        {
            return text;
        }
        int number = Integer.parseInt(text);
        return Integer.toBinaryString(number);
    }

    /**
     * 十进制转八进制
     * @param text
     * @return
     */
    public static String toOctal(String text)
    {
        if(TextUtils.isEmpty(text) || !StringUtils.isNumeric(text))
        {
            return text;
        }
        int number = Integer.parseInt(text);
        return Integer.toOctalString(number);
    }

    /**
     * 十进制转十六进制
     * @param text
     * @return
     */
    public static String toHex(String text)
    {
        if(TextUtils.isEmpty(text) || !StringUtils.isNumeric(text))
        {
            return text;
        }
        int number = Integer.parseInt(text);
        return Integer.toHexString(number);
    }

    /**
     * 其他进制转十进制
     * @param text
     * @param radix
     * @return
     */
    public static String toDecimal(String text, int radix)
    {
        if(TextUtils.isEmpty(text))
        {
            return text;
        }
        try {
            int number = Integer.valueOf(text, radix);
            return String.valueOf(number);
        } catch (Exception e) {
            e.printStackTrace();
            return text;
        }
    }

    /**
     * 二进制转十进制
     * @param text
     * @return
     */
    public static String binaryTo(String text)
    {
        return toDecimal(text, 2);
    }

    /**
     * 八进制转十进制
     * @param text
     * @return
     */
    public static String octalTo(String text)
    {
        return toDecimal(text, 8);
    }

    /**
     * 十六进制转十进制
     * @param text
     * @return
     */
    public static String hexTo(String text)
    {
        return toDecimal(text, 16);
    }
}
