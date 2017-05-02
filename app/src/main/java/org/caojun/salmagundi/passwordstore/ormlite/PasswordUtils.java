package org.caojun.salmagundi.passwordstore.ormlite;

import org.caojun.salmagundi.secure.DESede;
import org.caojun.salmagundi.string.ConvertUtils;

/**
 * Created by fourm on 2017/5/2.
 */

public class PasswordUtils {
    /**
     * 获取加密密钥
     * @return
     */
    private static String getKey(String company, String url, byte type, byte length, String account) {
        String key = company + url + type + account;
        StringBuffer k = new StringBuffer();
        for (int i = 0;i < key.length();i ++) {
            char c = key.charAt(i);
            if ((c >= 0 && c <= 9) || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                k.append(c);
            }
        }
        StringBuffer sb = new StringBuffer(24);
        int index = 0;
        while (sb.length() != 24) {
            if (index >= k.length()) {
                index %= k.length();
            }
            char c = k.charAt(index);
            sb.append(c);
            index += length;
        }
        return sb.toString();
    }

    /**
     * 加密密码
     */
    public static String getEncodePassword(String company, String url, byte type, byte length, String account, String password) {
        byte[] input = password.getBytes();
        byte[] output = DESede.encrypt(getKey(company, url, type, length, account), input);
        return ConvertUtils.toBase64(output);
    }

    /**
     * 获取明文密码
     */
    public static String getDecodePassword(Password psd) {
        byte[] input = ConvertUtils.base64ToBytes(psd.getPassword());
        byte[] output = DESede.decrypt(getKey(psd.getCompany(), psd.getUrl(), psd.getType(), psd.getLength(), psd.getAccount()), input);
        return new String(output);
    }
}
