package org.caojun.salmagundi.family.data

/**
 * Created by CaoJun on 2017/8/10.
 */
class Person {

    companion object {
        private var father: Relation = Relation(1, true, Relation.FZ, "父")
        private var mother: Relation = Relation(1, false, Relation.FZ, "母")
        private var son: Relation = Relation(-1, true, Relation.FZ, "子")
        private var daughter: Relation = Relation(-1, false, Relation.FZ, "女")
        private var ebrother: Relation = Relation(0, true, Relation.XD, "兄", true)
        private var ybrother: Relation = Relation(0, true, Relation.XD, "弟", false)
        private var esister: Relation = Relation(0, false, Relation.XD, "姐", true)
        private var ysister: Relation = Relation(0, true, Relation.XD, "妹", false)
        private var husband: Relation = Relation(0, true, Relation.FQ, "夫")
        private var wife: Relation = Relation(0, false, Relation.FQ, "妻")

        val persons = listOf<Relation>(father, mother, son, daughter, ebrother, ybrother, esister, ysister, husband, wife)
    }
}