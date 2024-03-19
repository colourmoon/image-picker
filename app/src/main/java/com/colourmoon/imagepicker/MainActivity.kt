package com.colourmoon.imagepicker

//import com.colourmoon.imagepicker.utils.RESULT_IMAGE_FILE
//import com.colourmoon.imagepicker.utils.RESULT_IMAGE_PATH
//import com.colourmoon.imagepicker.utils.ResultImage
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.tcm.imagepicker.project.R
import java.io.File


class MainActivity : AppCompatActivity() {
    private var profilePath: File? = null
    private val image: ImageView by lazy { findViewById(R.id.image) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
   /*     val imagePicker = CMImagePicker(
            this,
            resultCallback
        )
        imagePicker
            .allowCrop(true)
            .allowCompress(true)
            .allowGalleryOnly(false)
            .allowCameraOnly(false)
            .start()*/

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
                    val sizeInMb = (sizeInBytes ?: 1) / (1024 * 1024)
                    Log.e("TAG", " ${profilePath?.length()}: $sizeInMb")
                } else {
//                    callImagePicker()
                }

            }


        }
    }
*/


}
