package com.rohan90.customcamera.narcissistic

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.os.Handler
import android.os.HandlerThread
import android.util.AttributeSet
import android.util.Log
import android.util.Size
import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.Surface
import android.view.TextureView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.rohan90.android_playground.R
import com.rohan90.narcissistic.CameraActivity
import kotlinx.android.synthetic.main.view_camera_preview.view.*
import java.util.*

/**
 * Created by rohan on 18/4/19.
 */
class CameraPreview : LinearLayout, LifecycleObserver {


    private val STATE_PREVIEW = 0
    private val STATE_WAITING_LOCK = 1
    private val STATE_WAITING_PRECAPTURE = 2
    private val STATE_WAITING_NON_PRECAPTURE = 3
    private val STATE_PICTURE_TAKEN = 4
    private var mState = STATE_PREVIEW

    //Check state orientation of output image
    private val ORIENTATIONS = SparseIntArray()

    private var mCamera: CameraDevice? = null
    private lateinit var mCameraStateCallback: CameraDevice.StateCallback
    private lateinit var mCaptureCallback: CameraCaptureSession.CaptureCallback

    private var mBackgroundHandler: Handler? = null
    private var mBackgroundThread: HandlerThread? = null

    private lateinit var mCameraCaptureSessions: CameraCaptureSession
    private lateinit var mCameraRequestBuilder: CaptureRequest.Builder

    private lateinit var imageDimension: Size
    private lateinit var cameraId: String
    private val REQUEST_CAMERA_PERMISSION: Int = 1000
    private val mIsImageAvailable: Boolean = false


    constructor(context: Context) : super(context) {
        initViews(context)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initViews(context)
    }

    private fun initViews(context: Context) {
        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_camera_preview, this)

        start()
    }


    private fun start() {
        initOrientations()
        initTextureSurfaceListener()
        initCameraCallbacks()

        btnCapture.setOnClickListener {
            if (!mIsImageAvailable) {
                Log.d("CustomCamera", "onClick: taking picture.")
                takePicture()
            }
        }
    }

    private fun takePicture() {
        lockFocus()
    }

    private fun initOrientations() {
        ORIENTATIONS.append(Surface.ROTATION_0, 90)
        ORIENTATIONS.append(Surface.ROTATION_90, 0)
        ORIENTATIONS.append(Surface.ROTATION_180, 270)
        ORIENTATIONS.append(Surface.ROTATION_270, 180)
    }

    private fun initCameraCallbacks() {
        mCameraStateCallback = object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice?) {
                mCamera = camera!!
                createCameraPreview()
            }

            override fun onDisconnected(camera: CameraDevice?) {
                camera?.close()
            }

            override fun onError(camera: CameraDevice?, error: Int) {
                camera?.close()
                mCamera = null
            }
        }

        mCaptureCallback = object : CameraCaptureSession.CaptureCallback() {

            private fun process(result: CaptureResult) {
                when (mState) {
                    STATE_PREVIEW -> {
                    }// We have nothing to do when the camera preview is working normally.
                    STATE_WAITING_LOCK -> {
                        val afState = result.get(CaptureResult.CONTROL_AF_STATE)
                        if (afState == null) {
                            captureStillPicture()
                        } else if (CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED == afState || CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED == afState) {
                            // CONTROL_AE_STATE can be null on some devices
                            val aeState = result.get(CaptureResult.CONTROL_AE_STATE)
                            if (aeState == null || aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED) {
                                mState = STATE_PICTURE_TAKEN
                                captureStillPicture()
                            } else {
//                                runPrecaptureSequence()
                            }
                        } else if (afState == CaptureResult.CONTROL_AF_STATE_INACTIVE) {
                            mState = STATE_PICTURE_TAKEN
                            captureStillPicture()
                        }
                    }
                    STATE_WAITING_PRECAPTURE -> {
                        // CONTROL_AE_STATE can be null on some devices
                        val aeState = result.get(CaptureResult.CONTROL_AE_STATE)
                        if (aeState == null ||
                                aeState == CaptureResult.CONTROL_AE_STATE_PRECAPTURE ||
                                aeState == CaptureRequest.CONTROL_AE_STATE_FLASH_REQUIRED) {
                            mState = STATE_WAITING_NON_PRECAPTURE
                        }
                    }
                    STATE_WAITING_NON_PRECAPTURE -> {
                        // CONTROL_AE_STATE can be null on some devices
                        val aeState = result.get(CaptureResult.CONTROL_AE_STATE)
                        if (aeState == null || aeState != CaptureResult.CONTROL_AE_STATE_PRECAPTURE) {
                            mState = STATE_PICTURE_TAKEN
                            captureStillPicture()
                        }
                    }
                }
            }

            override fun onCaptureProgressed(session: CameraCaptureSession,
                                             request: CaptureRequest,
                                             partialResult: CaptureResult) {
                process(partialResult)
            }

            override fun onCaptureCompleted(session: CameraCaptureSession,
                                            request: CaptureRequest,
                                            result: TotalCaptureResult) {
                process(result)
            }

        }
    }

    private fun createCameraPreview() {
        try {
            val texture = texture.surfaceTexture
            texture.setDefaultBufferSize(imageDimension!!.width, imageDimension!!.height)
            val surface = Surface(texture)
            mCameraRequestBuilder = mCamera!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            mCameraRequestBuilder!!.addTarget(surface)
            mCamera?.createCaptureSession(Arrays.asList(surface), object : CameraCaptureSession.StateCallback() {
                override fun onConfigureFailed(session: CameraCaptureSession?) {
                    Toast.makeText(context, "Changed", Toast.LENGTH_SHORT).show()
                }

                override fun onConfigured(session: CameraCaptureSession?) {
                    if (mCamera == null) return
                    mCameraCaptureSessions = session!!
                    updatePreview()
                }
            }, null)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun updatePreview() {
        if (mCamera == null) Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()

        mCameraRequestBuilder?.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO)

        try {
            mCameraCaptureSessions?.setRepeatingRequest(mCameraRequestBuilder?.build(), null, mBackgroundHandler)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }

    }

    private fun initTextureSurfaceListener() {
        var textureSurfaceListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
                Log.i("ccc", "inside stu")
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
                Log.i("ccc", "inside stu")
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
                Log.i("ccc", "inside std")

                return true
            }

            override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
                Log.i("ccc", "inside sta")
                ready()
            }
        }
        texture.surfaceTextureListener = textureSurfaceListener
    }

    private fun ready() {
        openCamera()
    }

    private fun openCamera() {
        val manager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            cameraId = manager.cameraIdList[0]
            val characteristics = manager.getCameraCharacteristics(cameraId)
            val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)!!
            imageDimension = map.getOutputSizes(SurfaceTexture::class.java)[0]

            //Check realtime permission if run higher API 23
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(context as CameraActivity, arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CAMERA_PERMISSION)
                return
            }
            manager.openCamera(cameraId, mCameraStateCallback, null)

        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        startBackgroundThread()

        if (texture.isAvailable) ready()
        else initTextureSurfaceListener()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        stopBackgroundThread()
    }

    private fun stopBackgroundThread() {
        mBackgroundThread?.quitSafely()
        try {
            mBackgroundThread?.join()
            mBackgroundThread = null
            mBackgroundHandler = null
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }

    private fun startBackgroundThread() {
        mBackgroundThread = HandlerThread("Camera Background")
        mBackgroundThread?.start()
        mBackgroundHandler = Handler(mBackgroundThread?.looper)
    }

    private fun lockFocus() {
        try {
            // This is how to tell the camera to lock focus.
            mCameraRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_START)

            // Tell #mCaptureCallback to wait for the lock.
            mState = STATE_WAITING_LOCK

            mCameraCaptureSessions.capture(mCameraRequestBuilder.build(), mCaptureCallback,
                    mBackgroundHandler)

        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }

    }

    private fun unlockFocus() {
//        try {
//            // Reset the auto-focus trigger
//            mCameraRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
//                    CameraMetadata.CONTROL_AF_TRIGGER_CANCEL)
//
//            setAutoFlash(mPreviewRequestBuilder)
//
//            mCameraCaptureSessions.capture(mCameraRequestBuilder.build(), mCaptureCallback, mBackgroundHandler)
//            // After this, the camera will go back to the normal state of preview.
//            mState = STATE_PREVIEW
//            mCameraCaptureSessions.setRepeatingRequest(mPreviewRequest, mCaptureCallback, mBackgroundHandler)
//
//        } catch (e: CameraAccessException) {
//            e.printStackTrace()
//        }

    }

    private fun captureStillPicture() {
//        Log.d("CustomCamera", "captureStillPicture: capturing picture.")
//        try {
//
//            val activity = context as CameraActivity
//            if (null == activity || null == mCamera) {
//                return
//            }
//            // This is the CaptureRequest.Builder that we use to take a picture.
//            val captureBuilder = mCamera!!.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
//            captureBuilder!!.addTarget(mImageReader.getSurface())
//
//            // Use the same AE and AF modes as the preview.
//            captureBuilder.set(CaptureRequest.CONTROL_AF_MODE,
//                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
//
//            setAutoFlash(captureBuilder)
//
//            // Orientation
//            // Rotate the image from screen orientation to image orientation
//            val rotation = activity!!.windowManager.defaultDisplay.rotation
//            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, getOrientation(rotation))
//
//            val CaptureCallback = object : CameraCaptureSession.CaptureCallback() {
//
//                override fun onCaptureCompleted(session: CameraCaptureSession,
//                                                request: CaptureRequest,
//                                                result: TotalCaptureResult) {
//                    unlockFocus()
//                }
//            }
//
//            mCameraCaptureSessions.stopRepeating()
//            mCameraCaptureSessions.abortCaptures()
//            mCameraCaptureSessions.capture(captureBuilder.build(), CaptureCallback, null)
//
//        } catch (e: CameraAccessException) {
//            e.printStackTrace()
//        }

    }

}