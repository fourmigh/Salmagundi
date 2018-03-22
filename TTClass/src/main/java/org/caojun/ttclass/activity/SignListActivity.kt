package org.caojun.ttclass.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import com.hys.utils.FileUtils
import com.hys.utils.ImageUtils
import kotlinx.android.synthetic.main.activity_sign_list.*
import org.caojun.library.moments.activity.MomentsActivity
import org.caojun.ttclass.Constant
import org.caojun.ttclass.R
import org.caojun.ttclass.Utilities
import org.caojun.ttclass.adapter.SignAdapter
import org.caojun.ttclass.room.IClass
import org.caojun.ttclass.room.Sign
import org.caojun.ttclass.room.TTCDatabase
import org.caojun.utils.TimeUtils
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.uiThread
import java.util.ArrayList

/**
 * Created by CaoJun on 2017-12-19.
 */
class SignListActivity : Activity() {

    private var adapter: SignAdapter? = null
    private var iClass: IClass? = null
    private var sign:Sign? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_list)

        iClass = intent.getParcelableExtra(Constant.Key_Class)
        if (iClass == null) {
            finish()
            return
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            sign = adapter?.getItem(position)
            val date = TimeUtils.getTime("yyyy/MM/dd", sign!!.time)
            val weekday = Utilities.getWeekday(this, sign!!.time)
            val className = iClass!!.name
            val title = className + "-" + date + "-" + weekday
            val content = sign!!.note
            val hint = getString(R.string.note)

            val paths = ArrayList<String>()
            val ht = sign!!.images
            val e = ht.keys()
            while (e.hasMoreElements()) {
                val key = e.nextElement()
                paths.add(key)
            }
            startActivityForResult<MomentsActivity>(Constant.RequestCode_Note, MomentsActivity.Key_Title to title, MomentsActivity.Key_Content to content, MomentsActivity.Key_Hint to hint, MomentsActivity.Key_ImagePaths to paths)
        }

//        setFinishOnTouchOutside(false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constant.RequestCode_Note && resultCode == RESULT_OK && data != null) {
            val content = data.getStringExtra(MomentsActivity.Key_Content)
            val paths = data.getStringArrayListExtra(MomentsActivity.Key_ImagePaths)
            doSaveSign(content, paths)
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onResume() {
        super.onResume()
        refreshUI()
    }

    private fun refreshUI() {
        doAsync {
            val signs = TTCDatabase.getDatabase(this@SignListActivity).getSign().query(iClass!!.id)
            uiThread {
                if (adapter == null) {
                    adapter = SignAdapter(this@SignListActivity, signs)
                    listView?.adapter = adapter

                    doCheckImages(signs)
                } else {
                    adapter?.setData(signs)
                    adapter?.notifyDataSetChanged()
                }
            }
        }
    }

    private fun doCheckImages(signs: List<Sign>) {
        for (i in signs.indices) {
            val ht = signs[i].images
            val e = ht.keys()
            while (e.hasMoreElements()) {
                val key = e.nextElement()
                val value = ht[key] ?: continue
                doCheckImage(key, value)
            }
        }
    }

    private fun doCheckImage(path: String, bitmap: Bitmap) {
        doAsync {
            if (FileUtils.isFileExists(path)) {
                return@doAsync
            }
            ImageUtils.save(bitmap, path, Bitmap.CompressFormat.JPEG)
        }
    }

    private fun doSaveSign(content: String, paths: ArrayList<String>) {
        doAsync {
            sign!!.note = content
            sign!!.images.clear()
            for (i in paths.indices) {
                val bitmap = ImageUtils.getBitmap(paths[i])
                sign!!.images[paths[i]] = bitmap
            }
            TTCDatabase.getDatabase(this@SignListActivity).getSign().update(sign!!)
            refreshUI()
        }
    }
}