# Image Picker Library : Simplified Image Handling for Android Developers

Are you an Android developer struggling with the intricacies of image selection, cropping, and rotation within your app? The `image-picker` library is here to simplify your image handling tasks and provide a seamless experience for your users. This versatile library offers the following features:

1. **Streamlined Image Selection:** Tired of complex code for capturing images from the camera or selecting from the gallery? The `image-picker` library allows you to effortlessly integrate image selection capabilities in your app. Bid farewell to tedious coding and concentrate on building a delightful user experience.
2. **Intuitive Cropping:** Cropping images often involves intricate calculations and user interactions. With the library's built-in cropping functionality, you can enable users to crop images intuitively. Whether it's refining a profile picture or selecting the perfect image dimensions, the library simplifies the process.

3. **Image Rotation Made Easy:** Correcting image orientation after capture can be a pain point. Our library seamlessly handles image rotation, ensuring your users always see their images correctly aligned. Say goodbye to rotated selfies and landscape images that don't display correctly!

4. **Configuration Flexibility:** Customizing the image selection experience is crucial. With the `image-picker` library, you can tailor settings to your app's requirements. Whether it's allowing only gallery or camera access, enabling crop and compression, or setting up a camera-only mode, the library provides flexible configuration options.

5. **Real-time Image Handling:** The library streamlines the process of receiving and processing selected images. With a dedicated `ResultImage` callback, you can easily retrieve the image path and file, facilitating seamless real-time handling and manipulation.

6. **Robust Error Handling:** The library includes comprehensive error handling to ensure smooth user interactions. If an issue arises during the image selection process, error handling mechanisms are in place to inform users gracefully.

7. **Compressed Images:** Image size can impact app performance and user experience. With built-in compression options.

8. **User-friendly Design:** The library's user interface is designed to be intuitive and user-friendly. Users can navigate image selection, cropping, and other processes with ease, enhancing their interaction with your app.

Say goodbye to the complexities of image handling and make the development process smoother with the `image-picker` library. Empower your app with seamless image selection, cropping, and rotation, and provide your users with a delightful experience they'll appreciate.

Integrate the `image-picker` library into your project using the provided setup instructions and let it revolutionize your image handling challenges. If you encounter any issues or have questions, our support team is here to assist you on this journey to simpler, more efficient image handling.


## Step 1. Add the JitPack repository to your build file
Add the following code to your build.gradle file at the project level:

```kotlin
allprojects {
    repositories {
        // ...
        maven { url 'https://jitpack.io' }
    }
}
```
## Step 2. Add the dependency
In your app-level build.gradle file, add the dependency for the image-picker library:
```kotlin
dependencies {
    implementation 'com.github.colourmoon:image-picker:$VersonName'
}
```
Replace `$VersonName` with the actual version you want to use, for example:
```kotlin
implementation 'com.github.colourmoon:image-picker:v1.0.3'
```

## Step 3. Implement ResultImage in your Activity/Fragment and Call CMImagePicker

In your activity or fragment, follow these steps to integrate and use the CMImagePicker:

```kotlin
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.colourmoon.imagepicker.CMImagePicker
import com.colourmoon.imagepicker.ResultImage
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imagePicker = CMImagePicker(this, launcher)
        imagePicker
            .allowCrop(false)
            .allowCompress(true)
            .allowGalleryOnly(false)
            .allowCameraOnly(false)
            .start()
    }

    private val launcher = object : ResultImage {
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

                // Handle the image path and image file here
                // Make sure to check for null and file existence before using them
                // doSomeOperations()
            }
        }
    }
}
```

## Configuration and descriotion Along with Comment
```kotlin
val imagePicker = CMImagePicker(this, launcher)
imagePicker
    .allowCrop(false) // Enable or disable cropping (default: false)
    .allowCompress(true) // Enable compression (default: false)
    .allowGalleryOnly(false) // Allow only gallery selection (default: both camera and gallery)
    .allowCameraOnly(false) // Allow only camera usage (default: both camera and gallery)
    .start() // Launch the image picker
````
## Result Image Callback:
```kotlin
private val launcher = object : ResultImage {
    override val result: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Retrieve image path and file
            val imagePath = result.data?.getStringExtra(RESULT_IMAGE_PATH)
            val imageFile = result.data?.getSerializableExtra(RESULT_IMAGE_FILE) as File?
            
            // Handle the image path and file
            // ...
        }
    }
}
````

With these steps, you should be able to successfully integrate and use the Image Picker Library library in your project. If you encounter any issues or have further questions, feel free to reach out.

## Contributing

Contributions to Image Picker Library are welcome! If you find any issues or have suggestions for improvements, feel free to open an issue or submit a pull request.

## License

Image Picker Library is released under the **MIT License**. See the [LICENSE](https://en.wikipedia.org/wiki/MIT_License) file for more details.

## Support

For any questions or support related to Image Picker Library, you can reach out to us at ronilgwalani@colourmoon.com, pushpendra@thecolourmoon.com or join our community forum.

## Credits

The Image Picker Library library was developed by [Ronil Gwalani](https://github.com/ronilgwalnai)
