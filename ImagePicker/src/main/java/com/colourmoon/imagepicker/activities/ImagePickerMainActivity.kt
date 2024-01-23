package com.colourmoon.imagepicker.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.colourmoon.imagepicker.utils.CAMERA
import com.colourmoon.imagepicker.utils.COMPRESSION_PERCENTAGE
import com.colourmoon.imagepicker.utils.FILE_PATH
import com.colourmoon.imagepicker.utils.GALLERY
import com.colourmoon.imagepicker.utils.RESULT_IMAGE_FILE
import com.colourmoon.imagepicker.utils.RESULT_IMAGE_PATH
import com.colourmoon.imagepicker.utils.SELECTION_TYPE
import com.colourmoon.imagepicker.utils.WANT_COMPRESSION
import com.colourmoon.imagepicker.utils.WANT_CROP
import com.colourmoon.imagepicker.utils.compreser.Compressor
import com.tcm.imagepicker.R
import com.tcm.imagepicker.databinding.ImagPickerDialogueBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import java.util.Date
import java.util.Objects


class ImagePickerMainActivity : AppCompatActivity() {
    private var from = ""
    private var compressPercentage = 100
    private var wantCrop: Boolean = false
    private var wantCompress: Boolean = false
    private var cameraImageUri: Uri? = null
    private lateinit var dialog: AlertDialog
    private lateinit var binding: ImagPickerDialogueBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_picker_main)
        supportActionBar?.hide()
//        clearCache()
        binding = ImagPickerDialogueBinding.inflate(layoutInflater)
        dialog = AlertDialog.Builder(this).setView(binding.root).create()
        title = ""
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        wantCrop = intent.getBooleanExtra(WANT_CROP, false)
        wantCompress = intent.getBooleanExtra(WANT_COMPRESSION, false)
        compressPercentage = intent.getIntExtra(COMPRESSION_PERCENTAGE, 100)
        when (intent.getStringExtra(SELECTION_TYPE) ?: "") {
            CAMERA -> {
                checkCameraPermission()
            }

            GALLERY -> {
                checkGalleryPermission()
            }

            else -> {
                openSelectionDialogue()
            }
        }

    }

    private fun openSelectionDialogue() {
        dialog.setOnCancelListener {
            finish()
        }

        dialog.also {
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.window?.setWindowAnimations(R.style.DialogAnimation)
            it.setCanceledOnTouchOutside(true)
        }.show()
        binding.camera.setOnClickListener {
            checkCameraPermission()
            dialog.dismiss()
        }
        binding.gallery.setOnClickListener {
            checkGalleryPermission()
            dialog.dismiss()
        }
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data?.data
            if (data != null) {
                val uri1: Uri = data
                val filepath = arrayOf(MediaStore.Images.Media.DATA)
                val cursor: Cursor =
                    contentResolver?.query(uri1, filepath, null, null, null)!!
                cursor.moveToFirst()
                val columIndex = cursor.getColumnIndex(filepath[0])
                val path = cursor.getString(columIndex)

                val file = File(path)
                if (file.exists()) {
                    if (wantCrop) {
                        cropImage(path)
                    } else {
                        val resultIntent = Intent()
                        if (wantCompress && compressPercentage > 50 && compressPercentage < 100) {
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    val compressedFile =
                                        compress(
                                            compressPercentage,
                                            file
                                        )
                                    if (compressedFile.exists()) {
                                        setResultAndFinish(
                                            Uri.fromFile(compressedFile).path,
                                            compressedFile,
                                            resultIntent
                                        )
                                    } else {
                                        setResultAndFinish(path, file, resultIntent)

                                    }
                                } catch (e: Exception) {
                                    setResultAndFinish(path, file, resultIntent)
                                }
                            }
                        } else {
                            setResultAndFinish(path, file, resultIntent)
                        }
                    }
                }
            }
        } else {
            dialog.dismiss()
            finish()
        }
    }

    private fun checkGalleryPermission() {
        from = GALLERY
        val readImagePermission =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.READ_EXTERNAL_STORAGE
        if (checkCallingOrSelfPermission(readImagePermission) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(
                readImagePermission
            )

        } else {
            openGallery()
        }
    }

    private fun checkCameraPermission() {
        from = CAMERA
        if (checkCallingOrSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(
                Manifest.permission.CAMERA
            )
        } else {
            openCamera()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryLauncher.launch(intent)
    }

    private fun openCamera() {
        startCameraForResult.launch(Intent(this, CameraActivity::class.java))
    }

    private fun openCamera2() {
        val file = createImageFile()
        this.cameraImageUri =
            FileProvider.getUriForFile(this, "${applicationContext.packageName}.fileprovider", file)

        cameraLauncher.launch(this.cameraImageUri)

    }


    private fun cropImage(filePath: String) {
//        startForResult.launch(Intent(this, CroppingActivity::class.java).also {
        startForResult.launch(Intent(this, CroppingActivity::class.java).also {
            it.putExtra(FILE_PATH, filePath)
        })
//
    }

    private val startForResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imagePath = result.data?.getStringExtra(RESULT_IMAGE_PATH)
            val imageFile: File? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                result.data?.getSerializableExtra(
                    RESULT_IMAGE_FILE, File::class.java
                )
            } else {
                result.data?.getSerializableExtra(RESULT_IMAGE_FILE) as File
            }
            val resultIntent = Intent()
            if (wantCompress && compressPercentage > 50 && compressPercentage < 100) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val compressedFile =
                            if (imageFile?.exists() == true) {
                                compress(
                                    compressPercentage,
                                    imageFile
                                )
                            } else {
                                imageFile
                            }
                        if (compressedFile?.exists()==true) {
                            setResultAndFinish(
                                Uri.fromFile(compressedFile).path,
                                compressedFile,
                                resultIntent
                            )
                        } else {
                            setResultAndFinish(compressedFile?.path, compressedFile, resultIntent)

                        }
                    } catch (e: Exception) {
                        setResultAndFinish(imagePath, imageFile, resultIntent)
                    }
                }
            } else {
                setResultAndFinish(imagePath, imageFile, resultIntent)
            }

//            val resultIntent = Intent()
//            resultIntent.putExtra(RESULT_IMAGE_PATH, imagePath)
//            resultIntent.putExtra(RESULT_IMAGE_FILE, imageFile)
//            setResult(Activity.RESULT_OK, resultIntent)
//            finish()
        } else {
            dialog.dismiss()
            finish()
        }
    }
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->

            if (isGranted) {
                if (from == GALLERY) {
                    openGallery()
                } else if (from == CAMERA) {
                    openCamera()
                }
            } else {
                val permissionMessage = if (from == GALLERY) {
                    "Storage Permission is not Provided yet Please Provide the Permission so that we can Proceed further"
                } else {
                    "Camera Permission is not Provided yet Please Provide the Permission so that we can Proceed further"
                }
                android.app.AlertDialog.Builder(this).setTitle("Alert!!")
                    .setMessage(permissionMessage)
                    .setPositiveButton("Okay") { _, _ -> finish() }.show()
            }
        }

    private var cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success == true) {
                val path = Environment.getExternalStoragePublicDirectory(
                    cameraImageUri?.path.toString().replace("/external_files", "")
                ).toString()
                val file = File(
                    path
                )
                if (file.exists()) {
                    if (wantCrop) {
                        cropImage(path)
                    } else {
                        val resultIntent = Intent()
                        if (wantCompress && compressPercentage > 50 && compressPercentage < 100) {
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    val compressedFile =
                                        compress(
                                            compressPercentage,
                                            file
                                        )
                                    if (compressedFile.exists()) {
                                        setResultAndFinish(
                                            Uri.fromFile(compressedFile).path,
                                            compressedFile,
                                            resultIntent
                                        )
                                    } else {
                                        setResultAndFinish(path, file, resultIntent)

                                    }
                                } catch (e: Exception) {
                                    setResultAndFinish(path, file, resultIntent)
                                }
                            }
                        } else {
                            setResultAndFinish(path, file, resultIntent)
                        }
//                        val resultIntent = Intent()
//                        setResultAndFinish(cameraImageUri?.path, file, resultIntent)
                    }
                }

            } else {
                dialog.dismiss()
                finish()
            }
        }

    private fun setResultAndFinish(path: String?, file: File?, resultIntent: Intent) {
        resultIntent.putExtra(RESULT_IMAGE_PATH, path)
        resultIntent.putExtra(RESULT_IMAGE_FILE, file)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private fun createImageFile(): File {
        return File.createTempFile(
            Date().time.toString(),
            "jpg",
            getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        )
    }


    private suspend fun compress(percentage: Int, bitmap: File): File {

        val job = CoroutineScope(Dispatchers.IO).async {
            Compressor.compress(this@ImagePickerMainActivity, bitmap)
        }
        return job.await().also {
            Log.e("compress", "yes the compressed worked ", )
        }

//        val file1 = File.createTempFile(Date().time.toString(), ".jpg")
//        val fileOutputStream = FileOutputStream(file1)
//        bitmap?.compress(Bitmap.CompressFormat.JPEG, percentage, fileOutputStream)
//        fileOutputStream.flush()
//        fileOutputStream.close()
//        return bitmap
    }


    private val startCameraForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imagePath = result.data?.getStringExtra(RESULT_IMAGE_PATH)
            val imageFile: File? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                result.data?.getSerializableExtra(
                    RESULT_IMAGE_FILE, File::class.java
                )
            } else {
                result.data?.getSerializableExtra(RESULT_IMAGE_FILE) as File
            }
            if (imageFile?.exists() == true) {
                if (wantCrop) {
                    cropImage(imagePath ?: "")
                } else {
                    val resultIntent = Intent()
                    if (wantCompress && compressPercentage > 50 && compressPercentage < 100) {
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val compressedFile =
                                    compress(
                                        compressPercentage,
                                        imageFile
                                    )
                                if (compressedFile.exists()) {
                                    setResultAndFinish(
                                        Uri.fromFile(compressedFile).path,
                                        compressedFile,
                                        resultIntent
                                    )
                                } else {
                                    setResultAndFinish(imagePath, imageFile, resultIntent)

                                }
                            } catch (e: Exception) {
                                setResultAndFinish(imagePath, imageFile, resultIntent)
                            }
                        }
                    } else {
                        setResultAndFinish(imagePath, imageFile, resultIntent)
                    }
                }
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.some_error_occur_try_again),
                    Toast.LENGTH_SHORT
                ).show()
                dialog.dismiss()
                finish()
            }
        } else {
            dialog.dismiss()
            finish()
        }
    }

    private fun clearCache() {
        try {
            val dir = cacheDir
            deleteDir(dir)
            val externalDir = externalCacheDir
            deleteDir(externalDir)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun deleteDir(dir: File?): Boolean {
        return if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (i in Objects.requireNonNull(children).indices) {
                children?.get(i)?.let {
                    val success = deleteDir(File(dir, it))
                    if (!success) {
                        return false
                    }
                }
            }
            dir.delete()
        } else if (dir != null && dir.isFile) {
            dir.delete()
        } else {
            false
        }
    }


}
