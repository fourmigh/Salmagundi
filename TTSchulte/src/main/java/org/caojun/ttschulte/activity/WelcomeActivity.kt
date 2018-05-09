package org.caojun.ttschulte.activity

import android.app.Activity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_welcome.*
import org.caojun.particle.ParticleView
//import org.caojun.smasher.ParticleSmasher
//import org.caojun.smasher.SmashAnimator
import org.caojun.ttschulte.R
import org.jetbrains.anko.startActivity


class WelcomeActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        particleView.startAnim()
        particleView.setOnParticleAnimListener(object : ParticleView.ParticleAnimListener {
            override fun onAnimationEnd() {
                startActivity<GameActivity>()
                finish()

//                val smasher = ParticleSmasher(this@WelcomeActivity)
//                smasher.with(particleView)
//                        .setStyle(SmashAnimator.STYLE_FLOAT_LEFT)    // 设置动画样式
//                        .setDuration(1500)                     // 设置动画时间
//                        .setStartDelay(300)                    // 设置动画前延时
//                        .setHorizontalMultiple(2f)              // 设置横向运动幅度，默认为3
//                        .setVerticalMultiple(2f)                // 设置竖向运动幅度，默认为4
//                        .addAnimatorListener(object : SmashAnimator.OnAnimatorListener() {
//
//                            override fun onAnimatorEnd() {
//                                startActivity<GameActivity>()
//                                finish()
//                            }
//                        })
//                        .start()
            }
        })
    }
}