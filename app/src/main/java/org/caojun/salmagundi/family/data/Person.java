package org.caojun.salmagundi.family.data;

/**
 * Created by CaoJun on 2017/8/10.
 */

public class Person {

    public static final Relation Father = new Relation(1, true, Relation.FZ, "父");
    public static final Relation Mother = new Relation(1, false, Relation.FZ, "母");
    public static final Relation Son = new Relation(-1, true, Relation.FZ, "子");
    public static final Relation Daughter = new Relation(-1, false, Relation.FZ, "女");
    public static final Relation EBrother = new Relation(0, true, Relation.XD, "兄", true);
    public static final Relation YBrother = new Relation(0, true, Relation.XD, "弟", false);
    public static final Relation ESister = new Relation(0, false, Relation.XD, "姐", true);
    public static final Relation YSister = new Relation(0, true, Relation.XD, "妹", false);
    public static final Relation Husband = new Relation(0, true, Relation.FQ, "夫");
    public static final Relation Wife = new Relation(0, false, Relation.FQ, "妻");

    public static final Relation[] BaseRelation = {Father, Mother, Son, Daughter, EBrother, YBrother, ESister, YSister, Husband, Wife};
}
