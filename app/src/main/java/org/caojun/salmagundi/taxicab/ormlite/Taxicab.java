package org.caojun.salmagundi.taxicab.ormlite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.socks.library.KLog;
import org.caojun.salmagundi.taxicab.TaxicabUtils;
import java.io.Serializable;
import java.math.BigInteger;

/**
 * 的士数实体类
 * Created by fourm on 2017/5/3.
 */

@DatabaseTable
public class Taxicab implements Serializable {

    @DatabaseField(id = true)
    private String id;
    @DatabaseField
    private BigInteger a;
    @DatabaseField
    private BigInteger b;
    @DatabaseField
    private BigInteger taxicab;

    public Taxicab() {}

    public Taxicab(BigInteger a, BigInteger b) {
//        BigInteger absA = a.abs();
//        BigInteger absB = b.abs();
//        if (absA.compareTo(absB) < 1) {
//            //a绝对值小于等于b绝对值
//            this.setA(a);
//            this.setB(b);
//        } else {
//            this.setA(b);
//            this.setB(a);
//        }
        this.setA(a);
        this.setB(b);
        this.setTaxicab(TaxicabUtils.getTaxicab(a, b));
        this.setId(this.getA().toString() + "_" + this.getB().toString());

        KLog.d("Taxicab", "id: " + this.getId());
        KLog.d("Taxicab", "a: " + this.getA().toString());
        KLog.d("Taxicab", "b: " + this.getB().toString());
        KLog.d("Taxicab", "taxicab: " + this.getTaxicab().toString());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigInteger getA() {
        return a;
    }

    public void setA(BigInteger a) {
        this.a = a;
    }

    public BigInteger getB() {
        return b;
    }

    public void setB(BigInteger b) {
        this.b = b;
    }

    public BigInteger getTaxicab() {
        return taxicab;
    }

    public void setTaxicab(BigInteger taxicab) {
        this.taxicab = taxicab;
    }
}
