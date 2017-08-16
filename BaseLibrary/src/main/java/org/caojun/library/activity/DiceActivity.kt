package org.caojun.library.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import org.caojun.library.Constant
import org.caojun.library.R
import org.caojun.library.utils.RandomUtils
import pl.droidsonroids.gif.AnimationListener
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageView

/**
 * Created by CaoJun on 2017/8/14.
 */
class DiceActivity : Activity(), AnimationListener, View.OnClickListener {

    var times: Int = 0
    var index: Int = 0
    var givDice: GifImageView? = null
    var gifDrawable: GifDrawable? = null
    var isAnimationCompleted: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dice)

        setFinishOnTouchOutside(false);

        times = intent.getIntExtra(Constant.Key_Times, 1)

        givDice = findViewById(R.id.givDice)
        givDice?.setOnClickListener(this)

        doGif()
    }

    override fun onBackPressed() {
        if (isAnimationCompleted) {
            super.onBackPressed()
        }
    }

    override fun onAnimationCompleted(loopNumber: Int) {
        Thread(Runnable {
            Thread.sleep(2000)
            times --;
            if (times <= 0) {
                var intent: Intent = Intent()
                intent.putExtra(Constant.Key_Dice, index + 1)
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                isAnimationCompleted = true
            }
        }).start()
    }

    fun doGif() {
        val ResId = intArrayOf(R.drawable.dice1, R.drawable.dice2, R.drawable.dice3, R.drawable.dice4, R.drawable.dice5, R.drawable.dice6)
        index = RandomUtils.getRandom(0, 5)

        gifDrawable = GifDrawable(resources, ResId[index])
        givDice?.setImageDrawable(gifDrawable)
        gifDrawable?.removeAnimationListener(this)
        gifDrawable?.addAnimationListener(this)

        isAnimationCompleted = true
    }

    override fun onClick(p0: View?) {
        if (isAnimationCompleted) {
            doGif()
        }
    }
}