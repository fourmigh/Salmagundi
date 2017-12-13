package org.caojun.salmagundi.utils;

import android.content.Context;
import android.text.TextUtils;

import com.iflytek.cloud.thirdparty.A;

import org.caojun.salmagundi.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by CaoJun on 2017-12-7.
 */

public class MobileUtils {

    private static class SortComparator implements Comparator {
        @Override
        public int compare(Object lhs, Object rhs) {
            int a = Integer.valueOf((String) lhs);
            int b = Integer.valueOf((String) rhs);
            return a - b;
        }
    }

    public static final byte ChinaMobile = 0;
    public static final byte ChinaUnicom = 1;
    public static final byte ChinaTelecom = 2;
    public static final byte VNO = 3;
    private static String[][] sections;
    private static String[] AllNumbers = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    private static String[][] Sections = {
            {"139", "138", "137", "136", "135", "134", "147", "150", "151", "152", "157", "158", "159", "178", "182", "183", "184", "187", "188"},//ChinaMobile
            {"130", "131", "132", "155", "156", "185", "186", "145", "176"},//ChinaUnicom
            {"133", "153", "177", "173", "180", "181", "189"},//ChinaTelecom
            {"170", "171"}//VNO
    };

    private static void initialize(Context context) {
        sections = new String[VNO + 1][];
        sections[ChinaMobile] = context.getResources().getStringArray(R.array.section_cm);
        sections[ChinaUnicom] = context.getResources().getStringArray(R.array.section_cu);
        sections[ChinaTelecom] = context.getResources().getStringArray(R.array.section_ct);
        sections[VNO] = context.getResources().getStringArray(R.array.section_vno);
    }

    /**
     * 接下来可输入的号段
     *
     * @param context
     * @param number
     * @return
     */
    private static byte[] getSectionType(Context context, String number) {
        if (TextUtils.isEmpty(number)) {
            return new byte[]{ChinaMobile, ChinaUnicom, ChinaTelecom, VNO};
        }
        if (number.length() >= 3) {
            return null;
        }
        if (sections == null) {
            initialize(context);
        }
        ArrayList<Byte> list = new ArrayList<>();
        for (byte i = 0; i < sections.length; i++) {
            for (int j = 0; j < sections[i].length; j++) {
                if (sections[i][j].indexOf(number) == 0) {
                    list.add(i);
                    break;
                }
            }
        }
        int size = list.size();
        if (size < 1) {
            return null;
        }
        byte[] bytes = new byte[size];
        for (int i = 0; i < size; i++) {
            bytes[i] = list.get(i);
        }
        return bytes;
    }

    /**
     * 接下来要输入第几位号码
     *
     * @param number
     * @return
     */
    private static int getInputPosition(String number) {
        if (TextUtils.isEmpty(number)) {
            return 0;
        }
        int length = number.length();
        if (length >= 3) {
            return -1;
        }
        return length;
    }

    /**
     * 接下来可输入的数字
     *
     * @param context
     * @param number
     * @return
     */
    public static String[] getSectionNumber(Context context, String number) {
        if (!TextUtils.isEmpty(number) && number.length() >= 11) {
            return null;
        }
        byte[] types = getSectionType(context, number);
        if (types == null) {
            return AllNumbers;
        }
        int position = getInputPosition(number);
        if (position < 0) {
            return AllNumbers;
        }
        Set<String> set = new HashSet();
        if (sections == null) {
            initialize(context);
        }
        for (int i = 0; i < types.length; i++) {
            for (int j = 0; j < sections[types[i]].length; j++) {
                if (sections[types[i]][j].indexOf(number) == 0) {
                    String s = sections[types[i]][j].substring(position, position + 1);
                    set.add(s);
                }
            }
        }
        ArrayList<String> list = new ArrayList<>();
        for (String s : set) {
            list.add(s);
        }
        Comparator comp = new SortComparator();
        Collections.sort(list, comp);
        return list.toArray(new String[list.size()]);
    }
}
