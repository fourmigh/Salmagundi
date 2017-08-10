package org.caojun.salmagundi.family.utils;

import org.caojun.salmagundi.family.data.Person;
import org.caojun.salmagundi.family.data.Relation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CaoJun on 2017/8/10.
 */

public class RelationUtils {

    private static final List<Relation> relations = new ArrayList<>();

    public static void clear() {
        relations.clear();
    }

    public static void add(Relation relation) {
        relations.add(relation);
    }

    public static Relation claim(Relation relation, boolean male) {
        for (Relation r : Person.BaseRelation) {
            if (r.getLink() == relation.getLink()) {
                switch (relation.getLink()) {
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
