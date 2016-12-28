package org.caojun.salmagundi.string;

import android.text.TextUtils;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import org.apache.commons.lang.StringUtils;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串转换工具类
 * Created by CaoJun on 2016/12/28.
 */

public class ConvertUtils {

    /**
     * 字符串转大写 @param text @return
     */
    public static String toUpperCase(String text) {
        if (TextUtils.isEmpty(text)) return text;
        return text.toUpperCase();
    }

    /**
     * 字符串转小写 @param text @return
     */
    public static String toLowerCase(String text) {
        if (TextUtils.isEmpty(text)) return text;
        return text.toLowerCase();
    }

    /**
     * 汉字转拼音 @param c @return
     */
    public static String[] toPinyinStringArray(char c) {
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
     * 汉字转拼音 @param c @param first 只返回第一个 @return
     */
    public static String toPinyinString(char c, boolean first) {
        String[] pinyin = toPinyinStringArray(c);
        if (pinyin == null) return null;
        if (pinyin.length == 1) return pinyin[0];
        StringBuffer sb = new StringBuffer();
        sb.append(pinyin[0]);
        if (first) return sb.toString();
        sb.append("(");
        for (int i = 1; i < pinyin.length; i++) {
            sb.append(pinyin[i]);
            if (i < pinyin.length - 1) sb.append(",");
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * 汉字转拼音 @param text @return
     */
    public static String toPinyinString(String text) {
        if (TextUtils.isEmpty(text)) return text;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            String pinyin = toPinyinString(c, true);
            if (TextUtils.isEmpty(pinyin)) sb.append(c);
            else sb.append(pinyin);
            if (i < text.length() - 1) sb.append(" ");
        }
        return sb.toString();
    }

    /**
     * 字符串转ASCII @param text @return
     */
    public static String toASCII(String text) {
        if (TextUtils.isEmpty(text)) return text;
        StringBuffer sb = new StringBuffer();
        byte[] bytes = text.getBytes();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toHexString(bytes[i] & 0xff));
        }
        return sb.toString();
    }

    /**
     * 十进制转二进制
     *
     * @param text
     * @return
     */
    public static String toBinary(String text) {
        if (TextUtils.isEmpty(text) || !StringUtils.isNumeric(text)) {
            return text;
        }
        int number = Integer.parseInt(text);
        return Integer.toBinaryString(number);
    }

    /**
     * 十进制转八进制
     *
     * @param text
     * @return
     */
    public static String toOctal(String text) {
        if (TextUtils.isEmpty(text) || !StringUtils.isNumeric(text)) {
            return text;
        }
        int number = Integer.parseInt(text);
        return Integer.toOctalString(number);
    }

    /**
     * 十进制转十六进制
     *
     * @param text
     * @return
     */
    public static String toHex(String text) {
        if (TextUtils.isEmpty(text) || !StringUtils.isNumeric(text)) {
            return text;
        }
        int number = Integer.parseInt(text);
        return Integer.toHexString(number);
    }

    /**
     * 其他进制转十进制
     *
     * @param text
     * @param radix
     * @return
     */
    public static String toDecimal(String text, int radix) {
        if (TextUtils.isEmpty(text)) {
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
     *
     * @param text
     * @return
     */
    public static String binaryTo(String text) {
        return toDecimal(text, 2);
    }

    /**
     * 八进制转十进制
     *
     * @param text
     * @return
     */
    public static String octalTo(String text) {
        return toDecimal(text, 8);
    }

    /**
     * 十六进制转十进制
     *
     * @param text
     * @return
     */
    public static String hexTo(String text) {
        return toDecimal(text, 16);
    }

    /**
     * 字符串转MD5
     *
     * @param text
     * @return
     */
    public static String toMD5(String text) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(text.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            //signum 该数的正负号（-1 表示负，0 表示零，1 表示正）
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return text;
        }
    }

    public static boolean isMobileNO(String mobiles) {
        try {
            Pattern p = Pattern.compile("^1[3|4|5|7|8][0-9]\\d{8}$");
            Matcher m = p.matcher(mobiles);
            return m.matches();
        } catch (Exception e) {
            return false;
        }
    }

    public static String maskMobile(String mobile) {
        if (!isMobileNO(mobile)) {
            return mobile;
        }
        return mobile.substring(0, 3) + "****" + mobile.substring(mobile.length() - 4);
    }

    public static boolean isBankCardNo(String cardNo) {
        if (TextUtils.isEmpty(cardNo) || cardNo.length() < 15 || cardNo.length() > 19) {
            return false;
        }
        int sum = 0;
        boolean odd = false;
        for (int i = cardNo.length() - 1; i >= 0; i--) {
            int c = cardNo.charAt(i) - '0';
            if (odd) {
                int v = c * 2;
                sum += (v > 9) ? (v - 9) : v;
            } else {
                sum += c;
            }
            odd = !odd;
        }
        return sum % 10 == 0;
    }

    public static String maskBankCardNo(String cardNo) {
        if (!isBankCardNo(cardNo)) {
            return cardNo;
        }
        return cardNo.substring(0,4) + " **** **** " + cardNo.substring(cardNo.length() - 4);
    }

    public static String formatBankCardNo(String cardNo)
    {
        if(TextUtils.isEmpty(cardNo)) {
            return cardNo;
        }
        StringBuffer sb = new StringBuffer();
        int index = 0;
        while (index < cardNo.length()) {
            char c = cardNo.charAt(index);
            if(index > 0 && index % 4 == 0) {
                sb.append(' ');
            }
            sb.append(c);
            index ++;
        }
        return sb.toString();
    }

    public static boolean isPersonIdentifier(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        String reg15 = "^\\d{6}\\d{2}(0[1-9]|1[012])(0[1-9]|[12]\\d|3[01])\\d{3}$";
        String reg18 = "^\\d{6}(18|19|20)\\d{2}(0[1-9]|1[012])(0[1-9]|[12]\\d|3[01])\\d{3}(\\d|X|x)$";
        Pattern p15 = Pattern.compile(reg15);
        Pattern p18 = Pattern.compile(reg18);
        if (p15.matcher(str).matches() || p18.matcher(str).matches()) {
            return true;
        }
        return false;
    }

    public static String maskPersonIdentifier(String id) {
        if (!isPersonIdentifier(id)) {
            return id;
        }
        StringBuffer mask = new StringBuffer();
        for(int i = 0;i < id.length() - 6;i ++)
        {
            mask.append("*");
        }
        return id.substring(0, 3) + mask.toString()
                + id.substring(id.length() - 3);
    }
}
