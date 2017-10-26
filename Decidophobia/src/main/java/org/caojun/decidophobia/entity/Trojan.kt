package org.caojun.decidophobia.entity

import java.io.Serializable

/**
 * Created by CaoJun on 2017/10/26.
 */
class Trojan: Serializable {

    var color = 0
    var content = ""

    constructor(color: Int, content: String) {
        this.color = color
        this.content = content
    }
}