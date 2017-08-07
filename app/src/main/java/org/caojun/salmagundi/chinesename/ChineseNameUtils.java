package org.caojun.salmagundi.chinesename;

import android.content.Context;

import com.luhuiguo.chinese.ChineseUtils;

import org.caojun.salmagundi.R;

import java.util.Locale;
import java.util.Random;

/**
 * Created by CaoJun on 2017/8/4.
 */

public class ChineseNameUtils {

    public static final int Type_Name_Single = 0;//单名
    public static final int Type_Name_Double = 1;//双名
    public static final int Type_Name_Same = 2;//叠名
    public static final int Type_Name_Random = 3;//随机
    public static final int Type_Name_Custom = 4;//自定义

    public static final int Type_Surname_Single = 0;//单姓
    public static final int Type_Surname_Compound = 1;//双姓
    public static final int Type_Surname_Random = 2;//随机
    public static final int Type_Surname_Custom = 3;//自定义

    private static int getRandom(int min, int max) {
        return (int)(Math.random() * (max + 1 - min) + min);
    }

    public static String getSurname(Context context, int type) {
        String surname;
        switch (type) {
            case Type_Surname_Single:
                surname = getSingleSurname(context);
                break;
            case Type_Surname_Compound:
                surname = getCompoundSurname(context);
                break;
            case Type_Surname_Random:
                surname = getSurname(context);
                break;
            default:
                return null;
        }
        if (Locale.getDefault().equals(Locale.TRADITIONAL_CHINESE)) {
            surname = ChineseUtils.toTraditional(surname);
        }
        return surname;
    }

    private static String getSurname(Context context) {
        String[] surnames = context.getResources().getStringArray(R.array.surname);
        int index = getRandom(0, surnames.length);
        String surname = surnames[index];
        return surname;
    }

    private static String getCompoundSurname(Context context) {
        String surname = getSurname(context);
        while (surname.length() != 2) {
            surname = getSurname(context);
        }
        return surname;
    }

    private static String getSingleSurname(Context context) {
        String surname = getSurname(context);
        while (surname.length() != 1) {
            surname = getSurname(context);
        }
        return surname;
    }

    public static String getName(int type) {
        switch (type) {
            case Type_Name_Single:
                return getSingleName();
            case Type_Name_Double:
                return getDoubleName();
            case Type_Name_Same:
                return getSameName();
            case Type_Name_Random:
                type = (byte) getRandom(Type_Name_Single, Type_Name_Same);
                return getName(type);
            default:
                return null;
        }
    }

    private static String getRandomChar() {
        Random random = new Random();
        int hightPos = (176 + Math.abs(random.nextInt(39)));
        int lowPos = (161 + Math.abs(random.nextInt(93)));
        byte[] b = new byte[2];
        b[0] = (Integer.valueOf(hightPos)).byteValue();
        b[1] = (Integer.valueOf(lowPos)).byteValue();
        try {
            String chinese = new String(b, "GBK");
            if (Locale.getDefault().equals(Locale.TRADITIONAL_CHINESE)) {
                chinese = ChineseUtils.toTraditional(chinese);
            }
            return chinese;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String getSingleName() {
        return getRandomChar();
    }

    private static String getDoubleName() {
        String name1 = getRandomChar();
        String name2 = getRandomChar();
        while (name2.equals(name1)) {
            name2 = getRandomChar();
        }
        return name1 + name2;
    }

    private static String getSameName() {
        String name = getRandomChar();
        return name + name;
    }
}
