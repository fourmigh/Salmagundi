package org.caojun.salmagundi.taxicab;

import android.content.Context;
import org.caojun.salmagundi.taxicab.ormlite.Taxicab;
import org.caojun.salmagundi.taxicab.ormlite.TaxicabDatabase;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by fourm on 2017/5/2.
 */

public class TaxicabUtils {
    /**
     * 立方
     * @param number
     * @return
     */
    private static BigInteger getCube(BigInteger number) {
        return number.pow(3);
    }

    /**
     * 立方和
     * @param a
     * @param b
     * @return
     */
    public static BigInteger getTaxicab(BigInteger a, BigInteger b) {
        BigInteger aCube = getCube(a);
        BigInteger bCube = getCube(b);
        return aCube.add(bCube);
    }

    public static List<Taxicab> getList(Context context, BigInteger max, boolean isTaxicab) {
        if (isTaxicab) {
            //正整数
            for (BigInteger a = BigInteger.ONE; a.compareTo(max) <= 0; a = a.add(BigInteger.ONE)) {
                for (BigInteger b = BigInteger.ONE; b.compareTo(a) <= 0; b = b.add(BigInteger.ONE)) {
                    TaxicabDatabase.getInstance(context).insert(a, b);
                }
            }
        } else {
            //正或负或零
            for (BigInteger a = BigInteger.ZERO; a.compareTo(max.subtract(BigInteger.ONE)) < 0; a = a.add(BigInteger.ONE)) {
                BigInteger b = a.add(BigInteger.ONE);
                TaxicabDatabase.getInstance(context).insert(a, b);
                BigInteger nb = BigInteger.ZERO.subtract(b);
                TaxicabDatabase.getInstance(context).insert(a, nb);
            }
        }

        return TaxicabDatabase.getInstance(context).query();
    }
}
