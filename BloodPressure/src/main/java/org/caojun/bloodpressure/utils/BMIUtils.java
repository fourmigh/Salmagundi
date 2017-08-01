package org.caojun.bloodpressure.utils;

/**
 * Created by CaoJun on 2017/7/7.
 */

public class BMIUtils {
    public static byte getWHO(float bmi) {
        if (bmi < 18.5) {
            return 0;//偏瘦
        }
        if (bmi < 25) {
            return 1;//正常
        }
        if (bmi < 30) {
            return 2;//偏胖
        }
        if (bmi < 35) {
            return 3;//肥胖
        }
        if (bmi < 40) {
            return 4;//重度肥胖
        }
        return 5;//极重度肥胖
    }

    public static byte getAsia(float bmi) {
        if (bmi < 18.5) {
            return 0;//偏瘦
        }
        if (bmi < 23) {
            return 1;//正常
        }
        if (bmi < 25) {
            return 2;//偏胖
        }
        if (bmi < 30) {
            return 3;//肥胖
        }
        if (bmi < 40) {
            return 4;//重度肥胖
        }
        return 5;//极重度肥胖
    }

    public static byte getChina(float bmi) {
        if (bmi < 18.5) {
            return 0;//偏瘦
        }
        if (bmi < 24) {
            return 1;//正常
        }
        if (bmi < 27) {
            return 2;//偏胖
        }
        if (bmi < 30) {
            return 3;//肥胖
        }
        if (bmi < 40) {
            return 4;//重度肥胖
        }
        return 5;//极重度肥胖
    }

    /**
     * 计算BMI
     * @param height 单位：厘米
     * @param weight 单位：公斤
     * @return
     */
    public static float getBMI(int height, float weight) {
        return (weight * 10000) / (height * height);
    }
}
