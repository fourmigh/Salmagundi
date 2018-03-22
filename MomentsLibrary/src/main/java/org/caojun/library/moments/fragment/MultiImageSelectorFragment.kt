package org.caojun.library.moments.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.ContextCompat
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v7.app.AlertDialog
import android.support.v7.widget.ListPopupWindow
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.mis_fragment_multi_image.*
import org.caojun.library.moments.R
import org.caojun.library.moments.adapter.FolderAdapter
import org.caojun.library.moments.adapter.ImageGridAdapter
import org.caojun.library.moments.bean.Folder
import org.caojun.library.moments.bean.Image
import org.caojun.library.velocimeter.utils.FileUtils
import org.caojun.library.velocimeter.utils.ScreenUtils
import java.io.File
import java.io.IOException
import java.util.ArrayList

/**
 * Created by CaoJun on 2017-12-20.
 */
class MultiImageSelectorFragment: Fragment() {
    companion object {
        val TAG = "MultiImageSelectorFragment"
        private val REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 110
        private val REQUEST_CAMERA = 100

        private val KEY_TEMP_FILE = "key_temp_file"

        // Single choice
        val MODE_SINGLE = 0
        // Multi choice
        val MODE_MULTI = 1

        /** Max image size，int， */
        val EXTRA_SELECT_COUNT = "max_select_count"
        /** Select mode，[.MODE_MULTI] by default  */
        val EXTRA_SELECT_MODE = "select_count_mode"
        /** Whether show camera，true by default  */
        val EXTRA_SHOW_CAMERA = "show_camera"
        /** Original data set  */
        val EXTRA_DEFAULT_SELECTED_LIST = "default_list"

        // loaders
        private val LOADER_ALL = 0
        private val LOADER_CATEGORY = 1
    }

    // image result data set
    private val resultList = ArrayList<String>()
    // folder result data set
    private val mResultFolder = ArrayList<Folder>()

//    private var mGridView: GridView? = null
    private var mCallback: Callback? = null

    private var mImageAdapter: ImageGridAdapter? = null
    private var mFolderAdapter: FolderAdapter? = null

    private var mFolderPopupWindow: ListPopupWindow? = null

//    private var mCategoryText: TextView? = null
//    private var mPopupAnchorView: View? = null

    private var hasFolderGened = false

    private var mTmpFile: File? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            mCallback = activity as Callback
        } catch (e: ClassCastException) {
            throw ClassCastException("The Activity must implement MultiImageSelectorFragment.Callback interface...")
        }

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.mis_fragment_multi_image, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mode = selectMode()
        if (mode == MODE_MULTI) {
            val tmp = arguments.getStringArrayList(EXTRA_DEFAULT_SELECTED_LIST)
            if (tmp != null && tmp.size > 0) {
                resultList.addAll(tmp)
            }
        }
        mImageAdapter = ImageGridAdapter(activity, showCamera()/*, 3*/)
        mImageAdapter!!.showSelectIndicator(mode == MODE_MULTI)

//        mPopupAnchorView = view!!.findViewById(R.id.footer)

//        mCategoryText = view.findViewById(R.id.category_btn) as TextView
        category_btn.setText(R.string.mis_folder_all)
        category_btn.setOnClickListener {
            if (mFolderPopupWindow == null) {
                createPopupFolderList()
            }

            if (mFolderPopupWindow!!.isShowing) {
                mFolderPopupWindow!!.dismiss()
            } else {
                mFolderPopupWindow!!.show()
                var index = mFolderAdapter!!.getSelectIndex()
                index = if (index == 0) index else index - 1
                mFolderPopupWindow!!.listView!!.setSelection(index)
            }
        }

//        mGridView = view.findViewById(R.id.grid) as GridView
        grid.numColumns = 3
        grid.adapter = mImageAdapter
        grid.onItemClickListener = AdapterView.OnItemClickListener { adapterView, _, i, _ ->
            if (mImageAdapter!!.isShowCamera()) {
                if (i == 0) {
                    showCameraAction()
                } else {
                    val image = adapterView.adapter.getItem(i) as Image
                    selectImageFromGrid(image, mode)
                }
            } else {
                val image = adapterView.adapter.getItem(i) as Image
                selectImageFromGrid(image, mode)
            }
        }
//        grid.setOnScrollListener(object : AbsListView.OnScrollListener {
//            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
//                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
//                    Picasso.with(view.context).pauseTag(TAG)
//                } else {
//                    Picasso.with(view.context).resumeTag(TAG)
//                }
//            }
//
//            override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
//
//            }
//        })

        mFolderAdapter = FolderAdapter(activity)
    }

    /**
     * Create popup ListView
     */
    private fun createPopupFolderList() {
        val point = ScreenUtils.getScreenSize(activity)
        val width = point.x
        val height = (point.y * (4.5f / 8.0f)).toInt()
        mFolderPopupWindow = ListPopupWindow(activity)
        mFolderPopupWindow?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        mFolderPopupWindow?.setAdapter(mFolderAdapter)
        mFolderPopupWindow?.setContentWidth(width)
        mFolderPopupWindow?.width = width
        mFolderPopupWindow?.height = height
        mFolderPopupWindow?.anchorView = footer
        mFolderPopupWindow?.isModal = true
        mFolderPopupWindow?.setOnItemClickListener { adapterView, _, i, _ ->
            mFolderAdapter?.setSelectIndex(i)

            Handler().postDelayed({
                mFolderPopupWindow?.dismiss()

                if (i == 0) {
                    activity.supportLoaderManager.restartLoader(LOADER_ALL, Bundle(), mLoaderCallback)
                    category_btn.setText(R.string.mis_folder_all)
                    if (showCamera()) {
                        mImageAdapter?.setShowCamera(true)
                    } else {
                        mImageAdapter?.setShowCamera(false)
                    }
                } else {
                    val folder = adapterView.adapter.getItem(i) as Folder
                    mImageAdapter!!.setData(folder.images)
                    category_btn.text = folder.name
                    if (resultList.size > 0) {
                        mImageAdapter?.setDefaultSelected(resultList)
                    }
                    mImageAdapter?.setShowCamera(false)
                }

                grid.smoothScrollToPosition(0)
            }, 100)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState!!.putSerializable(KEY_TEMP_FILE, mTmpFile)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            mTmpFile = savedInstanceState.getSerializable(KEY_TEMP_FILE) as File
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // load image data
        activity.supportLoaderManager.initLoader(LOADER_ALL, Bundle(), mLoaderCallback)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                if (mTmpFile != null) {
                    mCallback?.onCameraShot(mTmpFile!!)
                }
            } else {
                // delete tmp file
                while (mTmpFile != null && mTmpFile!!.exists()) {
                    val success = mTmpFile!!.delete()
                    if (success) {
                        mTmpFile = null
                    }
                }
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        if (mFolderPopupWindow != null) {
            if (mFolderPopupWindow!!.isShowing) {
                mFolderPopupWindow?.dismiss()
            }
        }
        super.onConfigurationChanged(newConfig)
    }

    /**
     * Open camera
     */
    private fun showCameraAction() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    getString(R.string.mis_permission_rationale_write_storage),
                    REQUEST_STORAGE_WRITE_ACCESS_PERMISSION)
        } else {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (intent.resolveActivity(activity.packageManager) != null) {
                try {
                    mTmpFile = FileUtils.createTmpFile(activity)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                if (mTmpFile != null && mTmpFile!!.exists()) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpFile))
                    startActivityForResult(intent, REQUEST_CAMERA)
                } else {
                    Toast.makeText(activity, R.string.mis_error_image_not_exist, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(activity, R.string.mis_msg_no_camera, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun requestPermission(permission: String, rationale: String, requestCode: Int) {
        if (shouldShowRequestPermissionRationale(permission)) {
            AlertDialog.Builder(context)
                    .setTitle(R.string.mis_permission_dialog_title)
                    .setMessage(rationale)
                    .setPositiveButton(R.string.mis_permission_dialog_ok) { _, _ -> requestPermissions(arrayOf(permission), requestCode) }
                    .setNegativeButton(R.string.mis_permission_dialog_cancel, null)
                    .create().show()
        } else {
            requestPermissions(arrayOf(permission), requestCode)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_STORAGE_WRITE_ACCESS_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showCameraAction()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    /**
     * notify callback
     * @param image image data
     */
    private fun selectImageFromGrid(image: Image?, mode: Int) {
        if (image != null) {
            if (mode == MODE_MULTI) {
                if (resultList.contains(image.path)) {
                    resultList.remove(image.path)
                    if (mCallback != null) {
                        mCallback?.onImageUnselected(image.path)
                    }
                } else {
                    if (selectImageCount() == resultList.size) {
                        Toast.makeText(activity, R.string.mis_msg_amount_limit, Toast.LENGTH_SHORT).show()
                        return
                    }
                    resultList.add(image.path)
                    if (mCallback != null) {
                        mCallback?.onImageSelected(image.path)
                    }
                }
                mImageAdapter?.select(image)
            } else if (mode == MODE_SINGLE) {
                if (mCallback != null) {
                    mCallback!!.onSingleImageSelected(image.path)
                }
            }
        }
    }

    private val mLoaderCallback = object : LoaderManager.LoaderCallbacks<Cursor> {

        private val IMAGE_PROJECTION = arrayOf(MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.Media.MIME_TYPE, MediaStore.Images.Media.SIZE, MediaStore.Images.Media._ID)

        override fun onCreateLoader(id: Int, args: Bundle): Loader<Cursor>? {
            var cursorLoader: CursorLoader? = null
            if (id == LOADER_ALL) {
                cursorLoader = CursorLoader(activity,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        IMAGE_PROJECTION[4] + ">0 AND " + IMAGE_PROJECTION[3] + "=? OR " + IMAGE_PROJECTION[3] + "=? ",
                        arrayOf("image/jpeg", "image/png"), IMAGE_PROJECTION[2] + " DESC")
            } else if (id == LOADER_CATEGORY) {
                cursorLoader = CursorLoader(activity,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        IMAGE_PROJECTION[4] + ">0 AND " + IMAGE_PROJECTION[0] + " like '%" + args.getString("path") + "%'", null, IMAGE_PROJECTION[2] + " DESC")
            }
            return cursorLoader
        }

        private fun fileExist(path: String): Boolean {
            return if (!TextUtils.isEmpty(path)) {
                File(path).exists()
            } else false
        }

        override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
            if (data != null) {
                if (data.count > 0) {
                    val images = ArrayList<Image>()
                    data.moveToFirst()
                    do {
                        val path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]))
                        val name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]))
                        val dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]))
                        if (!fileExist(path)) {
                            continue
                        }
                        var image: Image? = null
                        if (!TextUtils.isEmpty(name)) {
                            image = Image(path, name, dateTime)
                            images.add(image)
                        }
                        if (!hasFolderGened) {
                            // get all folder data
                            val folderFile = File(path).parentFile
                            if (folderFile != null && folderFile.exists()) {
                                val fp = folderFile.absolutePath
                                val f = getFolderByPath(fp)
                                if (f == null) {
                                    val folder = Folder()
                                    folder.name = folderFile.name
                                    folder.path = fp
                                    folder.cover = image
                                    val imageList = ArrayList<Image>()
                                    if (image != null) {
                                        imageList.add(image)
                                    }
//                                    folder.images = imageList
                                    folder.images.addAll(imageList)
                                    mResultFolder.add(folder)
                                } else if (image != null) {
                                    f.images.add(image)
                                }
                            }
                        }

                    } while (data.moveToNext())

                    mImageAdapter?.setData(images)
                    if (resultList.size > 0) {
                        mImageAdapter?.setDefaultSelected(resultList)
                    }
                    if (!hasFolderGened) {
                        mFolderAdapter?.setData(mResultFolder)
                        hasFolderGened = true
                    }
                }
            }
        }

        override fun onLoaderReset(loader: Loader<Cursor>) {

        }
    }

    private fun getFolderByPath(path: String): Folder? {
//        if (mResultFolder != null) {
            for (folder in mResultFolder) {
                if (TextUtils.equals(folder.path, path)) {
                    return folder
                }
            }
//        }
        return null
    }

    private fun showCamera(): Boolean {
        return arguments == null || arguments.getBoolean(EXTRA_SHOW_CAMERA, true)
    }

    private fun selectMode(): Int {
        return if (arguments == null) MODE_MULTI else arguments.getInt(EXTRA_SELECT_MODE)
    }

    private fun selectImageCount(): Int {
        return if (arguments == null) 9 else arguments.getInt(EXTRA_SELECT_COUNT)
    }

    /**
     * Callback for host activity
     */
    interface Callback {
        fun onSingleImageSelected(path: String)
        fun onImageSelected(path: String)
        fun onImageUnselected(path: String)
        fun onCameraShot(imageFile: File)
    }
}