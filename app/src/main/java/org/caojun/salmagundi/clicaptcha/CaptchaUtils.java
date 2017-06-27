package org.caojun.salmagundi.clicaptcha;

import java.util.Random;

/**
 * Created by CaoJun on 2017/6/26.
 */

public class CaptchaUtils {
    public static String getRandomChar() {
        Random random = new Random();
        int hightPos = (176 + Math.abs(random.nextInt(39)));
        int lowPos = (161 + Math.abs(random.nextInt(93)));
        byte[] b = new byte[2];
        b[0] = (Integer.valueOf(hightPos)).byteValue();
        b[1] = (Integer.valueOf(lowPos)).byteValue();
        try {
            return new String(b, "GBK");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
