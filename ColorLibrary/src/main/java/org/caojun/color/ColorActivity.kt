package org.caojun.color

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.ViewTreeObserver
import android.widget.EditText
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_rgb2hsv.*

class ColorActivity: Activity() {

    companion object {
        val KEY_HEX = "KEY_HEX"
        val KEY_INT = "KEY_INT"
    }
    private var HEX = ""
    private var isHex = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rgb2hsv)

        if (intent.hasExtra(KEY_HEX)) {
            HEX = intent.getStringExtra(KEY_HEX)
            if (HEX.length > 6) {
                HEX = HEX.substring(HEX.length - 6)
            }
            isHex = true
        }
        if (TextUtils.isEmpty(HEX)) {
            val color = intent.getIntExtra(KEY_INT, Color.parseColor("#FFFFFF"))
            HEX = ColorUtils.toHexEncoding(color)
            isHex = false
        }

        val ivMinuses = arrayOf(ivMinusR, ivMinusG, ivMinusB)
        val ivPluses = arrayOf(ivPlusR, ivPlusG, ivPlusB)
        val etColors = arrayOf(etR, etG, etB)
        val sbColors = arrayOf(sbR, sbG, sbB)

        for (i in ivMinuses.indices) {
            ivMinuses[i].setOnClickListener({
                try {
                    var color = Integer.parseInt(etColors[i].text.toString())
                    color--
                    if (color < 0) {
                        color = 0
                    }
                    setEditTextColor(etColors[i], color)
                    sbColors[i].progress = color
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            })
        }

        for (i in ivPluses.indices) {
            ivPluses[i].setOnClickListener({
                try {
                    var color = Integer.parseInt(etColors[i].text.toString())
                    color++
                    if (color > 255) {
                        color = 255
                    }
                    setEditTextColor(etColors[i], color)
                    sbColors[i].progress = color
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            })
        }

        for (i in etColors.indices) {
            etColors[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable) {
                    val text = s.toString()
                    if (TextUtils.isEmpty(text)) {
                        sbColors[i].progress = 0
                        return
                    }
                    var color = Integer.parseInt(text)
                    if (color > 255) {
                        color = 255
                    } else if (color < 0) {
                        color = 0
                    }
                    sbColors[i].progress = color
                }
            })
        }

        for (i in sbColors.indices) {
            sbColors[i].setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    setEditTextColor(etColors[i], progress)
                    resetEditTextRGB(sbColors[0].progress, sbColors[1].progress, sbColors[2].progress)
                    resetImageView(sbColors[0].progress, sbColors[1].progress, sbColors[2].progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {

                }
            })
        }

        btnOK.setOnClickListener {
            val intent = Intent()
            if (isHex) {
                HEX = etRGB.text.toString()
                intent.putExtra(KEY_HEX, HEX)
            } else {
                val color = getColor(sbColors[0].progress, sbColors[1].progress, sbColors[2].progress)
                intent.putExtra(KEY_INT, color)
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!TextUtils.isEmpty(HEX)) {
            etRGB.setText(HEX)
            etRGB.setSelection(HEX.length)

            val etColors = arrayOf(etR, etG, etB)
            val sbColors = arrayOf(sbR, sbG, sbB)

            val color = Integer.parseInt(HEX, 16)
            val colors = intArrayOf(Color.red(color), Color.green(color), Color.blue(color))
            for (i in colors.indices) {
                setEditTextColor(etColors[i], colors[i])
                sbColors[i].progress = colors[i]
            }

            val vto = ivColor.viewTreeObserver
            vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    ivColor.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    resetImageView(sbColors[0].progress, sbColors[1].progress, sbColors[2].progress)
                }
            })
        }
    }

    private fun getColor(r: Int, g: Int, b: Int): Int {
        return Color.rgb(r, g, b)
    }

    private fun setEditTextColor(editText: EditText, color: Int) {
        val value = color.toString()
        editText.setText(value)
        editText.setSelection(value.length)
    }

    private fun resetEditTextRGB(r: Int, g: Int, b: Int) {
        val color = getColor(r, g, b)
        HEX = ColorUtils.toHexEncoding(color)
        etRGB.setText(HEX.toUpperCase())
        etRGB.setSelection(HEX.length)
    }

    private fun resetImageView(r: Int, g: Int, b: Int) {
        val color = getColor(r, g, b)
        ivColor.setBackgroundColor(color)
    }
}