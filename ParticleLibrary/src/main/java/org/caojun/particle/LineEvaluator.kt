package org.caojun.particle

import android.animation.TypeEvaluator

class LineEvaluator: TypeEvaluator<Particle> {

    override fun evaluate(fraction: Float, startValue: Particle, endValue: Particle): Particle {
        val particle = Particle()
        particle.x = startValue.x + (endValue.x - startValue.x) * fraction
        particle.y = startValue.y + (endValue.y - startValue.y) * fraction
        particle.radius = startValue.radius + (endValue.radius - startValue.radius) * fraction
        return particle
    }
}