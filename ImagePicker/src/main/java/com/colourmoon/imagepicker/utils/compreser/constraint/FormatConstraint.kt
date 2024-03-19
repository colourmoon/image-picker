package com.colourmoon.imagepicker.utils.compreser.constraint

import android.graphics.Bitmap
import com.colourmoon.imagepicker.utils.compreser.compressFormat
import com.colourmoon.imagepicker.utils.compreser.loadBitmap
import com.colourmoon.imagepicker.utils.compreser.overWrite
import java.io.File


class FormatConstraint(private val format: Bitmap.CompressFormat) : Constraint {

    override fun isSatisfied(imageFile: File): Boolean {
        return format == imageFile.compressFormat()
    }

    override fun satisfy(imageFile: File): File {
        return overWrite(imageFile, loadBitmap(imageFile), format)
    }
}

fun Compression.format(format: Bitmap.CompressFormat) {
    constraint(FormatConstraint(format))
}