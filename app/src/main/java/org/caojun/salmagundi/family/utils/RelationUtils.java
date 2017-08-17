package org.caojun.salmagundi.family.utils;

import org.caojun.salmagundi.family.data.Person;
import org.caojun.salmagundi.family.data.Relation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CaoJun on 2017/8/10.
 */

public class RelationUtils {

    public interface OnCalculateListener {
        void onAskMale(boolean isMale);
        void onAskElder(boolean isElder);
    }

    private static final List<Relation> relations = new ArrayList<>();

    public static void clear() {
        relations.clear();
    }

    public static void add(Relation relation) {
        relations.add(relation);
    }

    public static Relation link(Relation r1, Relation r2, OnCalculateListener onCalculateListener) {
        if (r1 == null || r2 == null) {
            return null;
        }
        Relation r = new Relation();
        //计算性别
        r.setMale(r2.isMale());
        //计算辈分
        r.setRank(r1.getRank() + r2.getRank());
        //计算关系
        r.setLink(r1.getLink());
        r.setLink(r2.getLink());
        return r;
    }

    public static Relation claim(Relation relation, boolean male) {
        for (Relation r : Person.BaseRelation) {
            if (r.getLink(0) == relation.getLink(0)) {
                switch (relation.getLink(0)) {
                    case Relation.FZ:
                        if (r.getRank() + relation.getRank() == 0 && r.isMale() == male) {
                            return r;
                        }
                        break;
                    case Relation.FQ:
                        if (r.getRank() == relation.getRank() && r.isMale() != relation.isMale()) {
                            return r;
                        }
                        break;
                    case Relation.XD:
                        if (r.getRank() == relation.getRank() && r.isMale() == male && r.isElder() != relation.isElder()) {
                            return r;
                        }
                        break;
                }
            }
        }
        return relation;
    }
}
