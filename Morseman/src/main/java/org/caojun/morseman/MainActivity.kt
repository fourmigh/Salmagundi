package org.caojun.morseman

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.socks.library.KLog
import kotlinx.android.synthetic.main.activity_main.*
import org.caojun.morseman.utils.FlashUtils
import org.caojun.morseman.utils.MorseUtils
import org.caojun.morseman.utils.ViewUtils
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.lang.Thread.sleep

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                message.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                message.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                message.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

//        val string = "Hello World, too!"
        val string = "SOS SOS SOS SOS SOS SOS"
        val byteArray = MorseUtils.string2ByteArray(string)
        val morse = MorseUtils.byteArray2Morse(byteArray)
        KLog.d("string", string)
        KLog.d("morse", morse)
        val strings = MorseUtils.byteArray2String(byteArray)
        KLog.d("strings", strings)

        doAsync {
            for (i in byteArray.indices) {
                KLog.d("doAsync", i.toString() + " : " + byteArray[i].toString())
                when (byteArray[i]) {
                    MorseUtils.Dit[MorseUtils.Type_Number] -> {
//                        FlashUtils.on(this@MainActivity)
                        uiThread {
                            ViewUtils.on(message)
                        }
                        sleep(MorseUtils.Time)
                    }
                    MorseUtils.Dah[MorseUtils.Type_Number] -> {
//                        FlashUtils.on(this@MainActivity)
                        uiThread {
                            ViewUtils.on(message)
                        }
                        sleep(MorseUtils.Time * 3)
                    }
                    else -> {
//                        FlashUtils.off(this@MainActivity)
                        uiThread {
                            ViewUtils.off(message)
                        }
                        when (byteArray[i]) {
                            MorseUtils.Space1[MorseUtils.Type_Number] -> {
                                sleep(MorseUtils.Time)
                            }
                            MorseUtils.Space3[MorseUtils.Type_Number] -> {
                                sleep(MorseUtils.Time * 3)
                            }
                            MorseUtils.Space7[MorseUtils.Type_Number] -> {
                                sleep(MorseUtils.Time * 7)
                            }
                        }
                    }
                }
            }
        }
    }
}
