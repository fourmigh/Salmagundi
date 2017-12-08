package org.caojun.calman.utils;

import android.widget.Button;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Created by CaoJun on 2017/11/17.
 */

public class Utils {

    private int getRandom(int min, int max) {
        return 0;
    }

    private int MinTransfer = 1;
    private int MaxTransfer = 99;
    private int[] createTransfer() {

        Hashtable ht = new Hashtable();
        ht.put("1", "One");
        ht.put("2", "Two");
        ht.put("3", "Three");
        Enumeration e = ht.keys();
        while (e.hasMoreElements()){
            System.out.println(e.nextElement());
        }

        boolean negative = false;
        int start = negative?1:0;

        int digital = getRandom(MinTransfer, MaxTransfer);
        int vice = getRandom(MinTransfer, MaxTransfer);
        return new int[]{digital, vice};
    }

    private int[] NumButton = {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    private Button[] Buttons = new Button[9];
}
