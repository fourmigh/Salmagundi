package org.caojun.decidophobia.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TableRow
import org.caojun.decidophobia.R
import org.caojun.widget.VerticalSeekBar

class ChoicesActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {

    val ResIdGif = intArrayOf(R.drawable.dice1, R.drawable.dice2, R.drawable.dice3, R.drawable.dice4, R.drawable.dice5, R.drawable.dice6)
    val ResIdTableRow = intArrayOf(R.id.trOption1, R.id.trOption2, R.id.trOption3, R.id.trOption4, R.id.trOption5, R.id.trOption6)
    val ResIdEditText = intArrayOf(R.id.etOption1, R.id.etOption2, R.id.etOption3, R.id.etOption4, R.id.etOption5, R.id.etOption6)

    val etOption = arrayListOf<EditText>()
    val trOption = arrayListOf<TableRow>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choices)
//        setSupportActionBar(toolbar)
//
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }

        for (i in ResIdTableRow.indices) {
            trOption.add(findViewById(ResIdTableRow[i]))
        }
        for (i in ResIdEditText.indices) {
            etOption.add(findViewById(ResIdEditText[i]))
        }

        var seekBar: VerticalSeekBar = findViewById(R.id.seekBar)
        seekBar.setOnSeekBarChangeListener(this)
        seekBar.progress = 0
        checkTableRow(0)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_choices, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        checkTableRow(progress)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

    private fun checkTableRow(progress: Int) {
        for (i in ResIdTableRow.indices) {
            if (i < progress + 2) {
                trOption[i].visibility = View.VISIBLE
            } else {
                trOption[i].visibility = View.GONE
            }
        }
    }
}
