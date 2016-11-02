package org.caojun.salmagundi.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

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

    public static int getRandom(int min, int max)
    {
        Random random = new Random();
        return random.nextInt(max)%(max-min+1) + min;
    }

    /**
     * 字节流转换2进制字符串
     * @param bytes
     * @return
     */
    public static String byteArrayToHexString(byte[] bytes) {
        final char[] hexArray = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
                '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * 2进制字符串转换字节流
     * @param s
     * @return
     */
    public static byte[] hexStringToByteArray(String s) {
        if(TextUtils.isEmpty(s))
        {
            return null;
        }
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
                    .digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * 整数和字符串的转换(将数字用指定的字符补齐到指定长度)
     * @param num						要操作的数字
     * @param len						补齐后的长度
     * @param leftPadding				true左边补齐，false 右边补齐
     * @param padding					作为补充的字符
     * @return							返回补齐后的字符串
     */
    public static String int2str(int num, int len, boolean leftPadding, char padding){
        StringBuilder buf = new StringBuilder();
        if (leftPadding){
            String n = "" + num;
            for (int i = 0; i < len - n.length(); i++){
                buf.append(padding);
            }
            buf.append(n);
        }else{
            String n = "" + num;
            buf.append(n);
            for (int i = 0; i < len - n.length(); i++){
                buf.append(padding);
            }
        }
        return buf.toString();
    }

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
    public static SimpleDateFormat sdf2 = new SimpleDateFormat("HHmmss");
    /**
     * 获得交易日期
     * @return
     */
    public static String getTradeDate(){
        return sdf.format(new Date());
    }

    /**
     * 获得交易时间
     * @return
     */
    public static String getTradeTime(){
        return sdf2.format(new Date());
    }
}
