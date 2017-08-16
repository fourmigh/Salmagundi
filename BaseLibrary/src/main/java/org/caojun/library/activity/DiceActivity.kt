package org.caojun.library.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import org.caojun.library.Constant
import org.caojun.library.R
import org.caojun.library.utils.RandomUtils
import pl.droidsonroids.gif.AnimationListener
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageView

/**
 * Created by CaoJun on 2017/8/14.
 */
class DiceActivity : Activity(), AnimationListener {

    val ResIdGif = intArrayOf(R.drawable.dice1, R.drawable.dice2, R.drawable.dice3, R.drawable.dice4, R.drawable.dice5, R.drawable.dice6)
    val ResIdLayout = intArrayOf(R.layout.activity_dice1, R.layout.activity_dice2, R.layout.activity_dice3, R.layout.activity_dice4, R.layout.activity_dice5, R.layout.activity_dice6)
    val ResIdDice = intArrayOf(R.id.givDice1, R.id.givDice2, R.id.givDice3, R.id.givDice4, R.id.givDice5, R.id.givDice6)
    val givDice = arrayListOf<GifImageView>()
    var number: Int = 0//个数（1-6，默认1）
    val dice = intArrayOf(0,0,0,0,0,0,0)//第一个表示总数，后面依次为每个骰子的数字

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFinishOnTouchOutside(false);

        number = intent.getIntExtra(Constant.Key_Number, 1)
        setContentView(ResIdLayout[number - 1])

        for (i in 1..number) {
            givDice.add(findViewById(ResIdDice[i - 1]))
        }

        doGif()
    }

    override fun onBackPressed() {}

    override fun onAnimationCompleted(loopNumber: Int) {
        Thread(Runnable {
            Thread.sleep(2000)
            var intent: Intent = Intent()
            intent.putExtra(Constant.Key_Dice, dice)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }).start()
    }

    fun doGif() {

        dice[0] = 0
        for (i in 1..number) {
            dice[i] = RandomUtils.getRandom(1, 6)
            dice[0] += dice[i]

            var gifDrawable = GifDrawable(resources, ResIdGif[dice[i] - 1])
            givDice[i - 1].setImageDrawable(gifDrawable)
            if (i == number) {
                gifDrawable.removeAnimationListener(this)
                gifDrawable.addAnimationListener(this)
            }
        }
    }
}