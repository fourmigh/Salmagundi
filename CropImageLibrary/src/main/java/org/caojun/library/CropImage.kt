package org.caojun.library

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.*
import android.media.FaceDetector
import android.net.Uri
import android.os.*
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import org.jetbrains.anko.doAsync
import java.io.*
import java.util.concurrent.CountDownLatch

/**
 * Created by CaoJun on 2018-3-14.
 */
class CropImage : MonitoredActivity() {

    companion object {
        val IMAGE_MAX_SIZE = 1024

        @JvmField val IMAGE_PATH = "image-path"
        @JvmField val SCALE = "scale"
        @JvmField val ORIENTATION_IN_DEGREES = "orientation_in_degrees"
        @JvmField val ASPECT_X = "aspectX"
        @JvmField val ASPECT_Y = "aspectY"
        @JvmField val OUTPUT_X = "outputX"
        @JvmField val OUTPUT_Y = "outputY"
        @JvmField val SCALE_UP_IF_NEEDED = "scaleUpIfNeeded"
        @JvmField val CIRCLE_CROP = "circleCrop"
        @JvmField val RETURN_DATA = "return-data"
        @JvmField val RETURN_DATA_AS_BITMAP = "data"
        @JvmField val ACTION_INLINE_DATA = "inline-data"

        val NO_STORAGE_ERROR = -1
        val CANNOT_STAT_ERROR = -2
    }

    // These are various options can be specified in the intent.
    private val mOutputFormat = Bitmap.CompressFormat.JPEG
    private var mSaveUri: Uri? = null
    private val mDoFaceDetection = true
    private var mCircleCrop = false
    private val mHandler = Handler()

    private var mAspectX: Int = 0
    private var mAspectY: Int = 0
    private var mOutputX: Int = 0
    private var mOutputY: Int = 0
    private var mScale: Boolean = false
    private var mImageView: CropImageView? = null
    private var mContentResolver: ContentResolver? = null
    private var mBitmap: Bitmap? = null
    private var mImagePath: String? = null

    internal var mWaitingToPick: Boolean = false // Whether we are wait the user to pick a face.
    internal var mSaving: Boolean = false  // Whether the "save" button is already clicked.
    internal var mCrop: HighlightView? = null

    // These options specifiy the output image size and whether we should
    // scale the output to fit it (or just crop it).
    private var mScaleUp = true

//    private final BitmapManager.ThreadSet mDecodingThreads =
//            new BitmapManager.ThreadSet();

    override fun onCreate(icicle: Bundle?) {

        super.onCreate(icicle)
        mContentResolver = contentResolver

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.cropimage)

        mImageView = findViewById<View>(R.id.image) as CropImageView

        showStorageToast(this)

        val intent = intent
        val extras = intent.extras
        if (extras != null) {

            if (extras.getString(CIRCLE_CROP) != null) {

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
                    mImageView!!.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
                }

                mCircleCrop = true
                mAspectX = 1
                mAspectY = 1
            }

            mImagePath = extras.getString(IMAGE_PATH)

            mSaveUri = getImageUri(mImagePath!! + ".tmp")
            mBitmap = getBitmap(mImagePath)

            if (extras.containsKey(ASPECT_X) && extras.get(ASPECT_X) is Int) {

                mAspectX = extras.getInt(ASPECT_X)
            } else {

                throw IllegalArgumentException("aspect_x must be integer")
            }
            if (extras.containsKey(ASPECT_Y) && extras.get(ASPECT_Y) is Int) {

                mAspectY = extras.getInt(ASPECT_Y)
            } else {

                throw IllegalArgumentException("aspect_y must be integer")
            }
            mOutputX = extras.getInt(OUTPUT_X)
            mOutputY = extras.getInt(OUTPUT_Y)
            mScale = extras.getBoolean(SCALE, true)
            mScaleUp = extras.getBoolean(SCALE_UP_IF_NEEDED, true)
        }


        if (mBitmap == null) {

            finish()
            return
        }

        // Make UI fullscreen.
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        findViewById<View>(R.id.discard).setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        findViewById<View>(R.id.save).setOnClickListener(
                object : View.OnClickListener {
                    override fun onClick(v: View) {

                        try {
                            onSaveClicked()
                        } catch (e: Exception) {
                            finish()
                        }

                    }
                })
        findViewById<View>(R.id.rotateLeft).setOnClickListener(
                object : View.OnClickListener {
                    override fun onClick(v: View) {

                        mBitmap = CropImageUtils.rotateImage(mBitmap!!, -90f)
                        val rotateBitmap = RotateBitmap(mBitmap)
                        mImageView!!.setImageRotateBitmapResetBase(rotateBitmap, true)
                        mRunFaceDetection.run()
                    }
                })

        findViewById<View>(R.id.rotateRight).setOnClickListener(
                object : View.OnClickListener {
                    override fun onClick(v: View) {

                        mBitmap = CropImageUtils.rotateImage(mBitmap!!, 90f)
                        val rotateBitmap = RotateBitmap(mBitmap)
                        mImageView!!.setImageRotateBitmapResetBase(rotateBitmap, true)
                        mRunFaceDetection.run()
                    }
                })
        startFaceDetection()
    }

    private fun getImageUri(path: String?): Uri {

        return Uri.fromFile(File(path!!))
    }

    private fun getBitmap(path: String?): Bitmap? {

        val uri = getImageUri(path)
        var `in`: InputStream? = null
        try {
            `in` = mContentResolver!!.openInputStream(uri)

            //Decode image size
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true

            BitmapFactory.decodeStream(`in`, null, o)
            `in`!!.close()

            var scale = 1
            if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
                scale = Math.pow(2.0, Math.round(Math.log(IMAGE_MAX_SIZE / Math.max(o.outHeight, o.outWidth).toDouble()) / Math.log(0.5)).toInt().toDouble()).toInt()
            }

            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            `in` = mContentResolver!!.openInputStream(uri)
            val b = BitmapFactory.decodeStream(`in`, null, o2)
            `in`!!.close()

            return b
        } catch (e: FileNotFoundException) {
            //            Log.e(TAG, "file " + path + " not found");
        } catch (e: IOException) {
            //            Log.e(TAG, "file " + path + " not found");
        }

        return null
    }


    private fun startFaceDetection() {

        if (isFinishing) {
            return
        }

        mImageView!!.setImageBitmapResetBase(mBitmap, true)

        doAsync {
            val latch = CountDownLatch(1)
            val b = mBitmap
            mHandler.post(object : Runnable {
                override fun run() {

                    if (b != mBitmap && b != null) {
                        mImageView!!.setImageBitmapResetBase(b, true)
                        mBitmap!!.recycle()
                        mBitmap = b
                    }
                    if (mImageView!!.getScale() == 1f) {
                        mImageView!!.center(true, true)
                    }
                    latch.countDown()
                }
            })
            try {
                latch.await()
            } catch (e: InterruptedException) {
                throw RuntimeException(e)
            }

            mRunFaceDetection.run()
        }
    }


    @Throws(Exception::class)
    private fun onSaveClicked() {
        // step api so that we don't require that the whole (possibly large)
        // bitmap doesn't have to be read into memory
        if (mSaving) return

        if (mCrop == null) {

            return
        }

        mSaving = true

        val r = mCrop!!.getCropRect()

        val width = r.width()
        val height = r.height()

        // If we are circle cropping, we want alpha channel, which is the
        // third param here.
        var croppedImage: Bitmap?
        try {

            croppedImage = Bitmap.createBitmap(width, height,
                    if (mCircleCrop) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565)
        } catch (e: Exception) {
            throw e
        }

        if (croppedImage == null) {

            return
        }

        run {
            val canvas = Canvas(croppedImage)
            val dstRect = Rect(0, 0, width, height)
            canvas.drawBitmap(mBitmap!!, r, dstRect, null)
        }

        if (mCircleCrop) {

            // OK, so what's all this about?
            // Bitmaps are inherently rectangular but we want to return
            // something that's basically a circle.  So we fill in the
            // area around the circle with alpha.  Note the all important
            // PortDuff.Mode.CLEAR.
            val c = Canvas(croppedImage)
            val p = Path()
            p.addCircle(width / 2f, height / 2f, width / 2f,
                    Path.Direction.CW)
            c.clipPath(p, Region.Op.DIFFERENCE)
            c.drawColor(0x00000000, PorterDuff.Mode.CLEAR)
        }

        /* If the output is required to a specific size then scale or fill */
        if (mOutputX != 0 && mOutputY != 0) {

            if (mScale) {

                /* Scale the image to the required dimensions */
                croppedImage = CropImageUtils.transform(Matrix(),
                        croppedImage, mOutputX, mOutputY, mScaleUp)
                if (croppedImage != croppedImage) {

                    croppedImage.recycle()
                }
            } else {

                /* Don't scale the image crop it to the size requested.
                                * Create an new image with the cropped image in the center and
                                * the extra space filled.
                                */

                // Don't scale the image but instead fill it so it's the
                // required dimension
                val b = Bitmap.createBitmap(mOutputX, mOutputY,
                        Bitmap.Config.RGB_565)
                val canvas = Canvas(b)

                val srcRect = mCrop!!.getCropRect()
                val dstRect = Rect(0, 0, mOutputX, mOutputY)

                val dx = (srcRect.width() - dstRect.width()) / 2
                val dy = (srcRect.height() - dstRect.height()) / 2

                /* If the srcRect is too big, use the center part of it. */
                srcRect.inset(Math.max(0, dx), Math.max(0, dy))

                /* If the dstRect is too big, use the center part of it. */
                dstRect.inset(Math.max(0, -dx), Math.max(0, -dy))

                /* Draw the cropped bitmap in the center */
                canvas.drawBitmap(mBitmap!!, srcRect, dstRect, null)

                /* Set the cropped bitmap as the new bitmap */
                croppedImage.recycle()
                croppedImage = b
            }
        }

        // Return the cropped image directly or save it to the specified URI.
        val myExtras = intent.extras
        if ((myExtras != null && ((myExtras.getParcelable<Parcelable>(RETURN_DATA_AS_BITMAP) != null || myExtras.getBoolean(RETURN_DATA))))) {

            //            Bundle extras = new Bundle();
            //            extras.putParcelable(RETURN_DATA_AS_BITMAP, croppedImage);
            //            setResult(RESULT_OK,
            //                    (new Intent()).setAction(ACTION_INLINE_DATA).putExtras(extras));
            val intent = Intent()
            intent.putExtra(RETURN_DATA_AS_BITMAP, CropImageUtils.bitmap2Bytes(croppedImage))
            setResult(Activity.RESULT_OK, intent)
            finish()
        } else {
            doAsync {
                saveOutput(croppedImage!!)
            }
        }
    }

    private fun saveOutput(croppedImage: Bitmap) {

        if (mSaveUri != null) {
            var outputStream: OutputStream? = null
            try {
                outputStream = mContentResolver!!.openOutputStream(mSaveUri!!)
                if (outputStream != null) {
                    croppedImage.compress(mOutputFormat, 90, outputStream)
                }
            } catch (ex: IOException) {

                //                Log.e(TAG, "Cannot open file: " + mSaveUri, ex);
                setResult(Activity.RESULT_CANCELED)
                finish()
                return
            } finally {

                outputStream?.close()
            }

            val extras = Bundle()
            val intent = Intent(mSaveUri!!.toString())
            intent.putExtras(extras)
            intent.putExtra(IMAGE_PATH, mSaveUri!!.path)
            intent.putExtra(ORIENTATION_IN_DEGREES, CropImageUtils.getOrientationInDegree(this))
            setResult(Activity.RESULT_OK, intent)
        }
        croppedImage.recycle()
        finish()
    }

    override fun onDestroy() {

        super.onDestroy()

        if (mBitmap != null) {

            mBitmap!!.recycle()
        }
    }


    internal var mRunFaceDetection: Runnable = object : Runnable {
        internal var mScale = 1f
        internal var mImageMatrix: Matrix? = null
        internal var mFaces = arrayOfNulls<FaceDetector.Face>(3)
        internal var mNumFaces: Int = 0

        // For each face, we create a HightlightView for it.
        private fun handleFace(f: FaceDetector.Face) {

            val midPoint = PointF()

            val r = ((f.eyesDistance() * mScale).toInt()) * 2
            f.getMidPoint(midPoint)
            midPoint.x *= mScale
            midPoint.y *= mScale

            val midX = midPoint.x.toInt()
            val midY = midPoint.y.toInt()

            val hv = HighlightView(mImageView!!)

            val width = mBitmap!!.width
            val height = mBitmap!!.height

            val imageRect = Rect(0, 0, width, height)

            val faceRect = RectF(midX.toFloat(), midY.toFloat(), midX.toFloat(), midY.toFloat())
            faceRect.inset((-r).toFloat(), (-r).toFloat())
            if (faceRect.left < 0) {
                faceRect.inset(-faceRect.left, -faceRect.left)
            }

            if (faceRect.top < 0) {
                faceRect.inset(-faceRect.top, -faceRect.top)
            }

            if (faceRect.right > imageRect.right) {
                faceRect.inset(faceRect.right - imageRect.right,
                        faceRect.right - imageRect.right)
            }

            if (faceRect.bottom > imageRect.bottom) {
                faceRect.inset(faceRect.bottom - imageRect.bottom,
                        faceRect.bottom - imageRect.bottom)
            }

            hv.setup(mImageMatrix!!, imageRect, faceRect, mCircleCrop,
                    mAspectX != 0 && mAspectY != 0)

            mImageView!!.add(hv)
        }

        // Create a default HightlightView if we found no face in the picture.
        private fun makeDefault() {

            val hv = HighlightView(mImageView!!)

            val width = mBitmap!!.width
            val height = mBitmap!!.height

            val imageRect = Rect(0, 0, width, height)

            // make the default size about 4/5 of the width or height
            var cropWidth = Math.min(width, height) * 4 / 5
            var cropHeight = cropWidth

            if (mAspectX != 0 && mAspectY != 0) {

                if (mAspectX > mAspectY) {

                    cropHeight = cropWidth * mAspectY / mAspectX
                } else {

                    cropWidth = cropHeight * mAspectX / mAspectY
                }
            }

            val x = (width - cropWidth) / 2
            val y = (height - cropHeight) / 2

            val cropRect = RectF(x.toFloat(), y.toFloat(), (x + cropWidth).toFloat(), (y + cropHeight).toFloat())
            hv.setup(mImageMatrix!!, imageRect, cropRect, mCircleCrop,
                    mAspectX != 0 && mAspectY != 0)

            mImageView!!.mHighlightViews.clear() // Thong added for rotate

            mImageView!!.add(hv)
        }

        // Scale the image down for faster face detection.
        private fun prepareBitmap(): Bitmap? {

            if (mBitmap == null) {

                return null
            }

            // 256 pixels wide is enough.
            if (mBitmap!!.width > 256) {

                mScale = 256.0f / mBitmap!!.width
            }
            val matrix = Matrix()
            matrix.setScale(mScale, mScale)
            return Bitmap.createBitmap(mBitmap!!, 0, 0, mBitmap!!.width, mBitmap!!.height, matrix, true)
        }

        override fun run() {

            mImageMatrix = mImageView!!.imageMatrix
            val faceBitmap = prepareBitmap()

            mScale = 1.0f / mScale
            if (faceBitmap != null && mDoFaceDetection) {
                val detector = FaceDetector(faceBitmap.width,
                        faceBitmap.height, mFaces.size)
                mNumFaces = detector.findFaces(faceBitmap, mFaces)
            }

            if (faceBitmap != null && faceBitmap != mBitmap) {
                faceBitmap.recycle()
            }

            mHandler.post(object : Runnable {
                override fun run() {

                    mWaitingToPick = mNumFaces > 1
                    if (mNumFaces > 0) {
                        for (i in 0 until mNumFaces) {
                            handleFace(mFaces[i]!!)
                        }
                    } else {
                        makeDefault()
                    }
                    mImageView!!.invalidate()
                    if (mImageView!!.mHighlightViews.size == 1) {
                        mCrop = mImageView!!.mHighlightViews[0]
                        mCrop!!.setFocus(true)
                    }

                    if (mNumFaces > 1) {
                        Toast.makeText(this@CropImage,
                                "Multi face crop help",
                                Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }

    fun showStorageToast(activity: Activity) {

        showStorageToast(activity, calculatePicturesRemaining(activity))
    }

    fun showStorageToast(activity: Activity, remaining: Int) {

        var noStorageText: String? = null

        if (remaining == NO_STORAGE_ERROR) {

            val state = Environment.getExternalStorageState()
            if (state == Environment.MEDIA_CHECKING) {

                noStorageText = activity.getString(R.string.preparing_card)
            } else {

                noStorageText = activity.getString(R.string.no_storage_card)
            }
        } else if (remaining < 1) {

            noStorageText = activity.getString(R.string.not_enough_space)
        }

        if (noStorageText != null) {

            Toast.makeText(activity, noStorageText, Toast.LENGTH_LONG).show()
        }
    }

    fun calculatePicturesRemaining(activity: Activity): Int {

        try {
            /*if (!ImageManager.hasStorage()) {
                           return NO_STORAGE_ERROR;
                       } else {*/
            var storageDirectory = ""
            val state = Environment.getExternalStorageState()
            if (Environment.MEDIA_MOUNTED == state) {
                storageDirectory = Environment.getExternalStorageDirectory().toString()
            } else {
                storageDirectory = activity.filesDir.toString()
            }
            val stat = StatFs(storageDirectory)
            val remaining = (((stat.availableBlocks.toFloat() * stat.blockSize.toFloat())) / 400000f)
            return remaining.toInt()
            //}
        } catch (ex: Exception) {
            // if we can't stat the filesystem then we don't know how many
            // pictures are remaining.  it might be zero but just leave it
            // blank since we really don't know.
            return CANNOT_STAT_ERROR
        }

    }
}