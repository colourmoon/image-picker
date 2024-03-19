package com.colourmoon.imagepicker

import android.content.Context
import android.content.Intent
import com.colourmoon.imagepicker.activities.ImagePickerMainActivity
import com.colourmoon.imagepicker.utils.BOTH
import com.colourmoon.imagepicker.utils.CAMERA
import com.colourmoon.imagepicker.utils.GALLERY
import com.colourmoon.imagepicker.utils.ResultImage
import com.colourmoon.imagepicker.utils.SELECTION_TYPE
import com.colourmoon.imagepicker.utils.WANT_COMPRESSION
import com.colourmoon.imagepicker.utils.WANT_CROP

class CMImagePicker(
    private val activity: Context,
    private var resultImageCallback: ResultImage

) {
    private var crop: Boolean = false
    private var cameraOnly: Boolean = false
    private var galleryOnly: Boolean = false
    private var compress: Boolean = false


    fun allowCrop(crop: Boolean): CMImagePicker {
        this.crop = crop
        return this
    }

    fun allowCameraOnly(cameraOnly: Boolean): CMImagePicker {
        this.cameraOnly = cameraOnly
        return this
    }

    fun allowGalleryOnly(galleryOnly: Boolean): CMImagePicker {
        this.galleryOnly = galleryOnly
        return this
    }

    fun allowCompress(compress: Boolean): CMImagePicker {
        this.compress = compress
        return this
    }

    fun start() {
        val selection = if (cameraOnly && !galleryOnly) {
            CAMERA
        } else if (galleryOnly && !cameraOnly) {
            GALLERY
        } else {
            BOTH
        }

        resultImageCallback.result.launch(
            Intent(
                activity,
                ImagePickerMainActivity::class.java
            ).apply {
                putExtra(WANT_CROP, crop)
                putExtra(SELECTION_TYPE, selection)
                putExtra(WANT_COMPRESSION, compress)
            }
        )
    }


}

