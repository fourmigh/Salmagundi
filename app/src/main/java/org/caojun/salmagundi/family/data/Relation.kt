package org.caojun.salmagundi.family.data

/**
 * Created by CaoJun on 2017/8/10.
 */
class Relation() {

    companion object {
        val FZ: Int = 0//父子
        val FQ: Int = 1//夫妻
        val XD: Int = 2//兄弟
    }

    constructor(_rank: Int, _male: Boolean, _link: Int, _name: String):this() {
        rank = _rank
        male = _male
        link = _link
        name = _name
    }

    constructor(_rank: Int, _male: Boolean, _link: Int, _name: String, _elder: Boolean):this(_rank, _male, _link, _name) {
        elder = _elder
    }

    var rank: Int = 0//辈分（0：平辈，1：长一辈，-1：小一辈，……）
    var male: Boolean = false//性别（true：男性，false：女性）
    var link: Int = FZ//关系（0：父子，1：夫妻，2：兄弟）
    var elder: Boolean = false//true：年长，false：年幼
    var name: String = ""//称谓
}