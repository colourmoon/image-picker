package com.colourmoon.imagepicker.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.exifinterface.media.ExifInterface
import com.colourmoon.imagepicker.utils.FILE_PATH
import com.colourmoon.imagepicker.utils.RESULT_IMAGE_FILE
import com.colourmoon.imagepicker.utils.RESULT_IMAGE_PATH
import com.tcm.imagepicker.R
import com.tcm.imagepicker.databinding.ActivityCroppingBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Date

class CroppingActivity : AppCompatActivity() {
    private var path: String = ""

    private lateinit var binding: ActivityCroppingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCroppingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        path = intent.getStringExtra(FILE_PATH) ?: ""
        val tempBitmap = BitmapFactory.decodeFile(path)

        val exif = ExifInterface(path)
        val orientation = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )
        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
            // No rotation needed
        }
        val bitmap =
            Bitmap.createBitmap(tempBitmap, 0, 0, tempBitmap.width, tempBitmap.height, matrix, true)

        binding.cropImageView.setImageBitmap(bitmap)
        supportActionBar?.hide()
        binding.rotation.setOnClickListener {
            binding.cropImageView.rotateImage(90)
        }
        binding.noChange.setOnClickListener {
            finish()
        }
        binding.flipH.setOnClickListener {
            binding.cropImageView.flipImageHorizontally()
        }
        binding.flipV.setOnClickListener {
            binding.cropImageView.flipImageVertically()
        }
        binding.yesConfirm.setOnClickListener {
            try {
                val bitmapResult: Bitmap? = binding.cropImageView.getCroppedImage()
                val resultIntent = Intent()
                if (bitmapResult == null) {
                    Toast.makeText(
                        this,
                        getString(R.string.this_image_is_not_able_to_cropped_try_changing_the_rotation),
                        Toast.LENGTH_SHORT
                    ).show()
                    resultIntent.putExtra(RESULT_IMAGE_PATH, path)
                    resultIntent.putExtra(RESULT_IMAGE_FILE, File(path))
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                }
                val file1 = File.createTempFile(Date().time.toString() + "", ".jpg")
                val fileOutputStream: FileOutputStream?
                fileOutputStream = FileOutputStream(file1)
                bitmapResult?.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
                fileOutputStream.flush()
                fileOutputStream.close()
                val croppedPath = file1.path
                resultIntent.putExtra(RESULT_IMAGE_PATH, croppedPath)
                resultIntent.putExtra(RESULT_IMAGE_FILE, file1)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()

            } catch (e: IOException) {
                Toast.makeText(this, e.message + "", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }

        }


    }
}