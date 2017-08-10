package org.caojun.salmagundi.family.utils

import org.caojun.salmagundi.family.data.Person
import org.caojun.salmagundi.family.data.Relation

/**
 * Created by CaoJun on 2017/8/10.
 */
class RelationUtils {

    companion object {

        private val relations = mutableListOf<Relation>()

        fun clear() {
            relations.clear()
        }

        fun add(relation: Relation) {
            relations.add(relation)
        }

        /**
         * 自称
         * 关系的逆运算
         * 例如：输入父，返回子
         */
        fun claim(relation: Relation, male: Boolean): Relation {
            for (r in Person.persons) {
                if (r.link == relation.link) {
                    when (relation.link) {
                        Relation.FZ -> {
                            if (r.rank + relation.rank == 0 && r.male == male) {
                                return r
                            }
                        }
                        Relation.FQ -> {
                            if (r.rank == relation.rank && r.male != relation.male) {
                                return r
                            }
                        }
                        Relation.XD -> {
                            if (r.rank == relation.rank && r.male == male && r.elder != relation.elder) {
                                return r
                            }
                        }
                    }
                }
            }
            return relation
        }
    }
}