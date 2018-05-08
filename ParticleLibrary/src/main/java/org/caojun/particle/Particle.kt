package org.caojun.particle

class Particle {

    var x: Float = 0.toFloat()
    var y: Float = 0.toFloat()
    var radius: Float = 0.toFloat()

    constructor()

    constructor(x: Float, y: Float, radius: Float): this() {
        this.x = x
        this.y = y
        this.radius = radius
    }
}