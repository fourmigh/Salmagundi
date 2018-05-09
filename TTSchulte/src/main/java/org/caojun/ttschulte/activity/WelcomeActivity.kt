package org.caojun.ttschulte.activity

import android.app.Activity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_welcome.*
import org.caojun.particle.ParticleView
import org.caojun.ttschulte.R
import org.jetbrains.anko.startActivity

class WelcomeActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        particleView.startAnim()
        particleView.setOnParticleAnimListener(object : ParticleView.ParticleAnimListener {
            override fun onAnimationEnd() {
                startActivity<GameActivity>()
                finish()
            }
        })
    }
}