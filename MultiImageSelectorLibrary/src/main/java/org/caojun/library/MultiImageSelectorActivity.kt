package org.caojun.library

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.mis_activity_default.*
import java.io.File
import java.util.ArrayList

/**
 * Created by CaoJun on 2017-12-20.
 */
class MultiImageSelectorActivity: AppCompatActivity(), MultiImageSelectorFragment.Callback {
    companion object {
        // Single choice
        val MODE_SINGLE = 0
        // Multi choice
        val MODE_MULTI = 1

        /** Max image size，int，[.DEFAULT_IMAGE_SIZE] by default  */
        val EXTRA_SELECT_COUNT = "max_select_count"
        /** Select mode，[.MODE_MULTI] by default  */
        val EXTRA_SELECT_MODE = "select_count_mode"
        /** Whether show camera，true by default  */
        val EXTRA_SHOW_CAMERA = "show_camera"
        /** Result data set，ArrayList&lt;String&gt; */
        val EXTRA_RESULT = "select_result"
        /** Original data set  */
        val EXTRA_DEFAULT_SELECTED_LIST = "default_list"
        // Default image size
        val DEFAULT_IMAGE_SIZE = 9
    }

    private val resultList = ArrayList<String>()
//    private var mSubmitButton: Button? = null
    private var mDefaultCount = DEFAULT_IMAGE_SIZE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.MIS_NO_ACTIONBAR)
        setContentView(R.layout.mis_activity_default)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.BLACK
        }

        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val intent = intent
        mDefaultCount = intent.getIntExtra(EXTRA_SELECT_COUNT, DEFAULT_IMAGE_SIZE)
        val mode = intent.getIntExtra(EXTRA_SELECT_MODE, MODE_MULTI)
        val isShow = intent.getBooleanExtra(EXTRA_SHOW_CAMERA, true)
        if (mode == MODE_MULTI && intent.hasExtra(EXTRA_DEFAULT_SELECTED_LIST)) {
            resultList.clear()
            val list = intent.getStringArrayListExtra(EXTRA_DEFAULT_SELECTED_LIST)
            if (list != null) {
                resultList.addAll(list)
            }
        }

//        mSubmitButton = findViewById(R.id.commit) as Button
        if (mode == MODE_MULTI) {
            updateDoneText(resultList)
            commit.visibility = View.VISIBLE
            commit.setOnClickListener {
                if (resultList.size > 0) {
                    // Notify success
                    val data = Intent()
                    data.putStringArrayListExtra(EXTRA_RESULT, resultList)
                    setResult(Activity.RESULT_OK, data)
                }
//                else {
//                    setResult(Activity.RESULT_CANCELED)
//                }
                finish()
            }
        } else {
            commit.visibility = View.GONE
        }

        if (savedInstanceState == null) {
            val bundle = Bundle()
            bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_COUNT, mDefaultCount)
            bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_MODE, mode)
            bundle.putBoolean(MultiImageSelectorFragment.EXTRA_SHOW_CAMERA, isShow)
            bundle.putStringArrayList(MultiImageSelectorFragment.EXTRA_DEFAULT_SELECTED_LIST, resultList)

            supportFragmentManager.beginTransaction()
                    .add(R.id.image_grid, Fragment.instantiate(this, MultiImageSelectorFragment::class.java.name, bundle))
                    .commit()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
//                setResult(Activity.RESULT_CANCELED)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Update done button by select image data
     * @param resultList selected image data
     */
    private fun updateDoneText(resultList: ArrayList<String>?) {
        var size = 0
        if (resultList == null || resultList.size <= 0) {
            commit.setText(R.string.mis_action_done)
            commit.isEnabled = false
        } else {
            size = resultList.size
            commit.isEnabled = true
        }
        commit.text = getString(R.string.mis_action_button_string,
                getString(R.string.mis_action_done), size, mDefaultCount)
    }

    override fun onSingleImageSelected(path: String) {
        val data = Intent()
        resultList.add(path)
        data.putStringArrayListExtra(EXTRA_RESULT, resultList)
        setResult(Activity.RESULT_OK, data)
        finish()
    }

    override fun onImageSelected(path: String) {
        if (!resultList.contains(path)) {
            resultList.add(path)
        }
        updateDoneText(resultList)
    }

    override fun onImageUnselected(path: String) {
        if (resultList.contains(path)) {
            resultList.remove(path)
        }
        updateDoneText(resultList)
    }

    override fun onCameraShot(imageFile: File) {
        // notify system the image has change
        sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(imageFile)))

        val data = Intent()
        resultList.add(imageFile.absolutePath)
        data.putStringArrayListExtra(EXTRA_RESULT, resultList)
        setResult(Activity.RESULT_OK, data)
        finish()
    }
}