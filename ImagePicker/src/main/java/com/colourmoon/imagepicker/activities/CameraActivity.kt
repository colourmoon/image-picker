package com.colourmoon.imagepicker.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.*
import android.graphics.Bitmap.CompressFormat
import android.hardware.camera2.*
import android.media.AudioManager
import android.media.MediaPlayer
import com.tcm.imagepicker.R
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.core.Camera
import androidx.camera.core.ImageCapture.OutputFileOptions
import androidx.camera.core.ImageCapture.OutputFileResults
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleOwner
import com.colourmoon.imagepicker.utils.RESULT_IMAGE_FILE
import com.colourmoon.imagepicker.utils.RESULT_IMAGE_PATH
import com.google.common.util.concurrent.ListenableFuture
import com.tcm.imagepicker.databinding.ActivityCameraBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit


class CameraActivity : AppCompatActivity() {
    lateinit var binding: ActivityCameraBinding
    private lateinit var imageCapture: ImageCapture
    private var backCamera = true
    var file: File? = null
    private lateinit var mediaPlayer: MediaPlayer
    var camera: Camera? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        supportActionBar?.hide()
        mediaPlayer = MediaPlayer.create(this, R.raw.camera_sound)
        initCamera(backCamera,cameraProviderFuture)
        setFocusable()
//        setZoomable()


        binding.changerCamera.setOnClickListener {

            backCamera = !backCamera
            initCamera(backCamera, cameraProviderFuture)
        }
        binding.click.setOnClickListener {

            captureImage()
        }
        binding.cancelCapture.setOnClickListener {
            captureImageAgain()
        }
        binding.goBack.setOnClickListener {
            finish()
        }
        binding.done.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra(RESULT_IMAGE_PATH, file?.absolutePath)
            resultIntent.putExtra(RESULT_IMAGE_FILE, file)
            cameraProviderFuture.get().unbindAll()
            setResult(Activity.RESULT_OK, resultIntent)
            finish()

        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun setFocusable() {
        val listener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                // Get the camera's current zoom ratio
                val currentZoomRatio = camera?.cameraInfo?.zoomState?.value?.zoomRatio ?: 0F

                // Get the pinch gesture's scaling factor
                val delta = detector.scaleFactor

                // Update the camera's zoom ratio. This is an asynchronous operation that returns
                // a ListenableFuture, allowing you to listen to when the operation completes.
                camera?.cameraControl?.setZoomRatio(currentZoomRatio * delta)

                // Return true, as the event was handled
                return true
            }
        }

        val scaleGestureDetector = ScaleGestureDetector(this, listener)

        binding.previewView.afterMeasured {
            val autoFocusPoint = SurfaceOrientedMeteringPointFactory(1f, 1f)
                .createPoint(.5f, .5f)
            try {
                val autoFocusAction = FocusMeteringAction.Builder(
                    autoFocusPoint,
                    FocusMeteringAction.FLAG_AF
                ).apply {
                    //start auto-focusing after 2 seconds
                    setAutoCancelDuration(1, TimeUnit.SECONDS)
                }.build()
                camera?.cameraControl?.startFocusAndMetering(autoFocusAction)
            } catch (e: CameraInfoUnavailableException) {
                e.printStackTrace()
            }
        }

        binding.previewView.afterMeasured {
            binding.previewView.setOnTouchListener { _, event ->
                return@setOnTouchListener when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        val factory: MeteringPointFactory = SurfaceOrientedMeteringPointFactory(
                            binding.previewView.width.toFloat(),
                            binding.previewView.height.toFloat()
                        )
                        val autoFocusPoint = factory.createPoint(event.x, event.y)
                        try {
                            camera?.cameraControl?.startFocusAndMetering(
                                FocusMeteringAction.Builder(
                                    autoFocusPoint,
                                    FocusMeteringAction.FLAG_AF
                                ).apply {
                                    //focus only when the user tap the preview
                                    disableAutoCancel()
                                }.build()
                            )
                        } catch (e: CameraInfoUnavailableException) {
                            Log.d("ERROR", "cannot access camera", e)
                        }
                        true
                    }
                    else -> {
                        scaleGestureDetector.onTouchEvent(event)
                        return@setOnTouchListener true
                    }
                }
            }
        }

    }

    private fun initCamera(
        front: Boolean,
        cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    ) {
        cameraProviderFuture.addListener({
            try {
                val processCameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                startCameraX(processCameraProvider, front)
            } catch (e: ExecutionException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }, getExecutor())

    }

    private fun startCameraX(processCameraProvider: ProcessCameraProvider, backCamera: Boolean) {
        processCameraProvider.unbindAll()
        val cameraSelector: CameraSelector = if (backCamera) {
            CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
        } else {
            CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT).build()
        }
        val preview = Preview.Builder().build()
        preview.setSurfaceProvider(binding.previewView.surfaceProvider)
        imageCapture =
            ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .build()



        camera = processCameraProvider.bindToLifecycle(
            (this as LifecycleOwner),
            cameraSelector,
            preview,
            imageCapture
        )
        imageCapture


    }


    private fun captureImage() {
        binding.click.isEnabled = false
        binding.goBack.isEnabled = false
        binding.changerCamera.isEnabled = false
        binding.previewView.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        val am: AudioManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            am = getSystemService(AUDIO_SERVICE) as AudioManager
            when (am.ringerMode) {
                AudioManager.RINGER_MODE_SILENT -> Toast.makeText(
                    this,
                    "Image Captured",
                    Toast.LENGTH_SHORT
                ).show()
                AudioManager.RINGER_MODE_VIBRATE -> Toast.makeText(
                    this,
                    "Image Captured",
                    Toast.LENGTH_SHORT
                ).show()
                AudioManager.RINGER_MODE_NORMAL -> {
                    mediaPlayer.start()
                }
            }
        } else {
            mediaPlayer.start()
        }

        try {
            val photo = File.createTempFile(Date().time.toString() + "", ".jpg")
            imageCapture.takePicture(
                OutputFileOptions.Builder(photo).build(),
                getExecutor(),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: OutputFileResults) {

//                        if (!backCamera) {
//                            val rotatedBitmapFile = reverseByHorizontal(photo)
//                            binding.savedImage.setImageURI(rotatedBitmapFile.toUri())
//                            file = rotatedBitmapFile
//                        } else {
                        file = photo
                        binding.savedImage.setImageURI(photo.toUri())
//                    }
                        binding.click.isEnabled = true
                        binding.goBack.isEnabled = true
                        binding.changerCamera.isEnabled = true
                        binding.progressBar.visibility = View.GONE
                        showOptionLayout()
                    }

                    override fun onError(exception: ImageCaptureException) {

                        Toast.makeText(
                            this@CameraActivity,
                            exception.message + "",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.click.isEnabled = true
                        binding.goBack.isEnabled = true
                        binding.changerCamera.isEnabled = true
                        binding.progressBar.visibility = View.GONE

                    }
                })
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun showOptionLayout() {
        binding.optionLayout.visibility = View.VISIBLE
        binding.savedImage.visibility = View.VISIBLE
        binding.previewView.visibility = View.GONE
        binding.actionLayout.visibility = View.INVISIBLE
    }

    private fun captureImageAgain() {
        binding.optionLayout.visibility = View.GONE
        binding.savedImage.visibility = View.GONE
        binding.previewView.visibility = View.VISIBLE
        binding.actionLayout.visibility = View.VISIBLE
    }


    private fun getExecutor() = ContextCompat.getMainExecutor(this)

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (binding.optionLayout.visibility == View.VISIBLE) {
            captureImageAgain()
        } else {
            super.onBackPressed()
        }
    }

    private inline fun View.afterMeasured(crossinline block: () -> Unit) {
        if (measuredWidth > 0 && measuredHeight > 0) {
            block()
        } else {
            viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    if (measuredWidth > 0 && measuredHeight > 0) {
                        viewTreeObserver.removeOnGlobalLayoutListener(this)
                        block()
                    }
                }
            })


        }
    }


    fun reverseByHorizontal(original: File): File {
        val bitmap = BitmapFactory.decodeFile(original.path)

        val photo = File.createTempFile(Date().time.toString() + "", ".jpg")
        val bos = ByteArrayOutputStream()
        val matrix = Matrix()
        matrix.preScale(-1f, 1f)
        matrix.postRotate(90f)
        val rotatedBitmap = Bitmap.createBitmap(
            bitmap, 0, 0, bitmap.width,
            bitmap.height, matrix, false
        )
        rotatedBitmap.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos)
        val bitmapData: ByteArray = bos.toByteArray()

        val fos = FileOutputStream(photo)
        fos.write(bitmapData)
        fos.flush()
        fos.close()
        return photo

    }


}
