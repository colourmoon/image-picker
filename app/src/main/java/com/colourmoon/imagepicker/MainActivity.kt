package com.colourmoon.imagepicker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tcm.imagepicker.project.R


class MainActivity : AppCompatActivity() {
/*    private var profilePath: File? = null
    private val image: ImageView by lazy { findViewById(R.id.image) }*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
      /*  val imagePicker = CMImagePicker(
            this,
            resultCallback
        )
        imagePicker
            .allowCrop(false)
            .allowCompress(
                true,
                60
            )//by default its false and the compress percentage is only applicable between 60-100
            .allowGalleryOnly(false)//by default both camera & gallery options are showing if you want only one mark it true
            .allowCameraOnly(false)//by default both camera & gallery options are showing if you want only one mark it true
            .start()//this method starts the image picker*/

    }

/*
    private val resultCallback = object : ResultImage {
        override val result: ActivityResultLauncher<Intent> = registerForActivityResult(
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

                if (!imagePath.isNullOrEmpty()) {
                    profilePath = File(imagePath)
                    image.setImageURI(Uri.fromFile(profilePath))
                    val sizeInBytes = profilePath?.length();
//transform in MB
                    val sizeInMb = (sizeInBytes ?:1) / (1024 * 1024)
                    Log.e("TAG", " ${profilePath?.length()}: $sizeInMb")
                } else {
//                    callImagePicker()
                }

            }


        }
    }
*/


}
