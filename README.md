# image-picker


>Step 1. Add the JitPack repository to your build file

```gradel
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  ```
  >Step 2. Add the dependency

 ```gradel
dependencies {
	        implementation 'com.github.colourmoon:image-picker:$VersonName' // here VersionName = v1.0.0
	}
  ```
  
  > Step 3. Implement ResultImage in your Activity/Fragment and Call CMImagePicker Exactly Like this
  ``` 
  class MainActivity : AppCompatActivity()  {
    private lateinit var imageView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
              val imagePicker = CMImagePicker(this, launcher)//here you have to pass the context and ResultImageCallback
            //do not initialize the object in here initialize it exactly as it shown is the example
            imagePicker
                .allowCrop(false)//by default its false
                .allowCompress(true,90)//by default its false and the compress percentage is only applicable between 60-100
                .allowGalleyOnly(false)//by default both camera & gallery options are showing if you want only one mark it true
                .allowCameraOnly(false)//by default both camera & gallery options are showing if you want only one mark it true
                .start()//this method starts the image picker
      
        
        }
         
         
         
   // here you have make a object of the ResultImage to get the result from the image picker
privite val launcher=object : ResultImage {
    override val result: ActivityResultLauncher<Intent>  = registerForActivityResult(
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
//            here you got the image path and image file do whatever you want to do with it
//            Just check the null safety and file existence before using it
//            doSomeOperations()

        }


    }
}

        
 }
