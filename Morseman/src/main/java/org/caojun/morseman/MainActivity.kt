package org.caojun.morseman

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import com.socks.library.KLog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_camera.*
import kotlinx.android.synthetic.main.layout_morsecode.*
import kotlinx.android.synthetic.main.layout_translate.*
import org.caojun.morseman.adapter.MorseCodeAdapter
import org.caojun.morseman.listener.OnColorStatusChange
import org.caojun.morseman.utils.ColorUtils
import org.caojun.morseman.utils.FlashUtils
import org.caojun.morseman.utils.MorseUtils
import org.caojun.morseman.utils.ViewUtils
import org.caojun.morseman.widget.MorseKeyboard
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.lang.Thread.sleep

class MainActivity : AppCompatActivity() {

    private var isMorseShowing = false
    private var isScreen = false

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                showTranslate()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_camera -> {
                showCamera()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_help -> {
                showMorseCode()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        cameraView.setOnColorStatusChange(object : OnColorStatusChange {
            override fun onColorChange(color: Int) {
                MorseUtils.addColor(color)
            }
        })

        initTranslate()
        initCamera()
        initMorseCode()
    }

    public override fun onResume() {
        super.onResume()
        cameraView.onResume()
    }

    public override fun onPause() {
        stopShowMorse()
        cameraView.onPause()
        super.onPause()
    }

    private fun initCamera() {
        layoutCamera.visibility = View.GONE
        cameraView.onPause()
    }

    private fun showCamera() {
        cameraView.onResume()
        layoutTranslate.visibility = View.GONE
        layoutCamera.visibility = View.VISIBLE
        layoutMorseCode.visibility = View.GONE
    }

    private fun initMorseCode() {
        lvMoseCode.adapter = MorseCodeAdapter(this)
        layoutMorseCode.visibility = View.GONE
    }

    private fun showMorseCode() {
        cameraView.onPause()
        layoutTranslate.visibility = View.GONE
        layoutCamera.visibility = View.GONE
        layoutMorseCode.visibility = View.VISIBLE
    }

    private fun initTranslate() {
        layoutTranslate.visibility = View.VISIBLE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            etOriginal.showSoftInputOnFocus = false
            etMorse.showSoftInputOnFocus = false
        } else {
            etOriginal.inputType = InputType.TYPE_NULL
            etMorse.inputType = InputType.TYPE_NULL
        }

        etOriginal.setOnFocusChangeListener { view, b ->
            if (b) {
                morseKeyboard.visibility = View.VISIBLE
            } else {
                morseKeyboard.visibility = View.GONE
            }
        }

        etOriginal.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                if (editable.isEmpty()) {
                    btnScreen.isEnabled = false
                    btnFlashlight.isEnabled = false
                    btnStop.isEnabled = false
                } else {
                    btnScreen.isEnabled = true
                    btnFlashlight.isEnabled = true
                }
            }
        })

        morseKeyboard.onClickListener = object: MorseKeyboard.OnClickListener {
            override fun onClick(key: String?): Boolean {
                if (key == null) {
                    etOriginal.text = null
                } else if (TextUtils.isEmpty(key)) {
                    if (etOriginal.text.isNotEmpty()) {
                        etOriginal.setText(etOriginal.text.subSequence(0, etOriginal.text.length - 1))
                        etOriginal.setSelection(etOriginal.text.length)
                    }
                } else {
                    etOriginal.text.append(key)
                }

                val morse = MorseUtils.string2Morse(etOriginal.text.toString())
                etMorse.setText(morse)
                return true
            }
        }

        btnStop.isEnabled = false
        btnScreen.isEnabled = true
        btnFlashlight.isEnabled = true

        btnScreen.setOnClickListener({
            isScreen = true
            startShowMorse()
            btnFlashlight.isEnabled = true
            btnStop.isEnabled = true
            btnScreen.isEnabled = false
        })

        btnFlashlight.setOnClickListener({
            isScreen = false
            startShowMorse()
            btnScreen.isEnabled = true
            btnStop.isEnabled = true
            btnFlashlight.isEnabled = false
        })

        btnStop.setOnClickListener({
            stopShowMorse()
        })
    }

    private fun startShowMorse() {
        if (!isMorseShowing) {
            isMorseShowing = true
            showMorse()
        }
        morseKeyboard.visibility = View.GONE
    }

    private fun stopShowMorse() {
        isMorseShowing = false
        btnStop.isEnabled = false
        val isEnabled = !TextUtils.isEmpty(etOriginal.text.toString())
        btnScreen.isEnabled = isEnabled
        btnFlashlight.isEnabled = isEnabled
        morseKeyboard.visibility = View.VISIBLE
    }

    private fun showTranslate() {
        layoutTranslate.visibility = View.VISIBLE
        layoutCamera.visibility = View.GONE
        layoutMorseCode.visibility = View.GONE
    }

    fun onButtonClick(view: View) {
        morseKeyboard.onButtonClick(view)
        etOriginal.requestFocus()
    }

    private fun showMorse() {
        val string = etOriginal.text.toString()
        val byteArray = MorseUtils.string2ByteArray(string)
        val morse = MorseUtils.byteArray2Morse(byteArray)
        KLog.d("string", string)
        KLog.d("morse", morse)
        val strings = MorseUtils.byteArray2String(byteArray)
        KLog.d("strings", strings)

        doAsync {
            while (true) {
                for (i in byteArray.indices) {
                    when (byteArray[i]) {
                        MorseUtils.Dit[MorseUtils.Type_Number] -> {
                            uiThread {
                                if (isScreen) {
                                    ViewUtils.on(message)
                                } else {
                                    FlashUtils.on(this@MainActivity)
                                }
                            }
                            sleep(MorseUtils.Time)
                        }
                        MorseUtils.Dah[MorseUtils.Type_Number] -> {
                            uiThread {
                                if (isScreen) {
                                    ViewUtils.on(message)
                                } else {
                                    FlashUtils.on(this@MainActivity)
                                }
                            }
                            sleep(MorseUtils.Time * 3)
                        }
                        else -> {
                            uiThread {
                                if (isScreen) {
                                    ViewUtils.on(message)
                                } else {
                                    FlashUtils.on(this@MainActivity)
                                }
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
                    if (!isMorseShowing) {
                        break;
                    }
                }
            }
            if (!isMorseShowing) {
                FlashUtils.release(this@MainActivity)
            }
        }
    }
}
