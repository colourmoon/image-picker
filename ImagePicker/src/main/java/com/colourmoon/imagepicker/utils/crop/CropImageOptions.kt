package com.colourmoon.imagepicker.utils.crop

import android.content.res.Resources
import android.graphics.Bitmap.CompressFormat
import android.graphics.Color
import android.graphics.Rect
import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.Px


data class CropImageOptions @JvmOverloads constructor(
  @JvmField var imageSourceIncludeGallery: Boolean = true,
  @JvmField var imageSourceIncludeCamera: Boolean = true,
  @JvmField var cropShape: CropImageView.CropShape = CropImageView.CropShape.RECTANGLE,
  @JvmField var cornerShape: CropImageView.CropCornerShape = CropImageView.CropCornerShape.RECTANGLE,
  @JvmField @Px var cropCornerRadius: Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, Resources.getSystem().displayMetrics),
  /**
   * An edge of the crop window will snap to the corresponding edge of a specified bounding box when
   * the crop window edge is less than or equal to this distance away from the bounding
   * box edge.
   */
  @JvmField @Px var snapRadius: Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3f, Resources.getSystem().displayMetrics),
  /** The radius of the touchable area around the handle. */
  @JvmField @Px var touchRadius: Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24f, Resources.getSystem().displayMetrics),
  @JvmField var guidelines: CropImageView.Guidelines = CropImageView.Guidelines.ON,
  @JvmField var scaleType: CropImageView.ScaleType = CropImageView.ScaleType.FIT_CENTER,
  @JvmField var showCropOverlay: Boolean = true,
  @JvmField var showCropLabel: Boolean = false,
  @JvmField var showProgressBar: Boolean = true,
  @JvmField @ColorInt var progressBarColor: Int = Color.rgb(153, 51, 153),
  @JvmField var autoZoomEnabled: Boolean = true,
  /** Multitouch allows to resize and drag the cropping window at the same time. */
  @JvmField var multiTouchEnabled: Boolean = false,
  /** If the crop window can be moved by dragging the crop window in the center. */
  @JvmField var centerMoveEnabled: Boolean = true,
  @JvmField var maxZoom: Int = 4,
  /** In percentage. 0.1 means 10% on both sides. */
  @JvmField var initialCropWindowPaddingRatio: Float = 0.0f,
  @JvmField var fixAspectRatio: Boolean = false,
  @JvmField var aspectRatioX: Int = 1,
  @JvmField var aspectRatioY: Int = 1,
  @JvmField @Px var borderLineThickness: Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3f, Resources.getSystem().displayMetrics),
  @JvmField @ColorInt var borderLineColor: Int = Color.argb(170, 255, 255, 255),
  @JvmField @Px var borderCornerThickness: Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, Resources.getSystem().displayMetrics),
  @JvmField @Px var borderCornerOffset: Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, Resources.getSystem().displayMetrics),
  @JvmField @Px var borderCornerLength: Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14f, Resources.getSystem().displayMetrics),
  @JvmField @ColorInt var borderCornerColor: Int = Color.WHITE,
  @JvmField @ColorInt var circleCornerFillColorHexValue: Int = Color.WHITE,
  @JvmField @Px var guidelinesThickness: Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, Resources.getSystem().displayMetrics),
  @JvmField @ColorInt var guidelinesColor: Int = Color.argb(170, 255, 255, 255),
  @JvmField @ColorInt var backgroundColor: Int = Color.argb(119, 0, 0, 0),
  @JvmField @Px var minCropWindowWidth: Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 42f, Resources.getSystem().displayMetrics).toInt(),
  @JvmField @Px var minCropWindowHeight: Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 42f, Resources.getSystem().displayMetrics).toInt(),
  @JvmField @Px var minCropResultWidth: Int = 40,
  @JvmField @Px var minCropResultHeight: Int = 40,
  @JvmField @Px var maxCropResultWidth: Int = 99999,
  @JvmField @Px var maxCropResultHeight: Int = 99999,
  @JvmField var activityTitle: String? = "",
  @JvmField @ColorInt var activityMenuIconColor: Int = 0,
  @JvmField @ColorInt var activityMenuTextColor: Int? = null,
  /** The Android Uri to save the cropped image to. */
  @JvmField var customOutputUri: Uri? = null,
  @JvmField var outputCompressFormat: CompressFormat = CompressFormat.JPEG,
  @JvmField var outputCompressQuality: Int = 90,
  /** The width to resize the cropped image to. */
  @JvmField @Px var outputRequestWidth: Int = 0,
  /** The height to resize the cropped image to. */
  @JvmField @Px var outputRequestHeight: Int = 0,
  @JvmField var outputRequestSizeOptions: CropImageView.RequestSizeOptions = CropImageView.RequestSizeOptions.NONE,
  /** If the result of crop image activity should not save the cropped image bitmap. */
  @JvmField var noOutputImage: Boolean = false,
  /** Will be set after the image has loaded. */
  @JvmField var initialCropWindowRectangle: Rect? = null,
  /** Will be set after the image has loaded. */
  @JvmField var initialRotation: Int = -1,
  @JvmField var allowRotation: Boolean = true,
  @JvmField var allowFlipping: Boolean = true,
  @JvmField var allowCounterRotation: Boolean = false,
  /** The amount of degrees to rotate clockwise or counter-clockwise. */
  @JvmField var rotationDegrees: Int = 90,
  @JvmField var flipHorizontally: Boolean = false,
  @JvmField var flipVertically: Boolean = false,
  @JvmField var cropMenuCropButtonTitle: CharSequence? = null,
  @JvmField @DrawableRes var cropMenuCropButtonIcon: Int = 0,
  @JvmField var skipEditing: Boolean = false,
  /** Enabling this option replaces the current AlertDialog to choose the image source with an Intent chooser. */
  @JvmField var showIntentChooser: Boolean = false,
  @JvmField var intentChooserTitle: String? = null,
  /** Reorders intent list displayed with the app package names passed here. */
  @JvmField var intentChooserPriorityList: List<String>? = emptyList(),
  @JvmField @Px var cropperLabelTextSize: Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20f, Resources.getSystem().displayMetrics),
  @JvmField @ColorInt var cropperLabelTextColor: Int = Color.WHITE,
  @JvmField var cropperLabelText: String? = "",
  @JvmField @ColorInt var activityBackgroundColor: Int = Color.WHITE,
  @JvmField @ColorInt var toolbarColor: Int? = null,
  @JvmField @ColorInt var toolbarTitleColor: Int? = null,
  @JvmField @ColorInt var toolbarBackButtonColor: Int? = null,
  @JvmField @ColorInt var toolbarTintColor: Int? = null,
) : Parcelable {
 /*
  constructor(parcel: Parcel) : this(
    parcel.readByte() != 0.toByte(),
    parcel.readByte() != 0.toByte(),
//    TODO("cropShape"),
//    TODO("cornerShape"),
    parcel.readFloat(),
    parcel.readFloat(),
    parcel.readFloat(),
//    TODO("guidelines"),
//    TODO("scaleType"),
    parcel.readByte() != 0.toByte(),
    parcel.readByte() != 0.toByte(),
    parcel.readByte() != 0.toByte(),
    parcel.readInt(),
    parcel.readByte() != 0.toByte(),
    parcel.readByte() != 0.toByte(),
    parcel.readByte() != 0.toByte(),
    parcel.readInt(),
    parcel.readFloat(),
    parcel.readByte() != 0.toByte(),
    parcel.readInt(),
    parcel.readInt(),
    parcel.readFloat(),
    parcel.readInt(),
    parcel.readFloat(),
    parcel.readFloat(),
    parcel.readFloat(),
    parcel.readInt(),
    parcel.readInt(),
    parcel.readFloat(),
    parcel.readInt(),
    parcel.readInt(),
    parcel.readInt(),
    parcel.readInt(),
    parcel.readInt(),
    parcel.readInt(),
    parcel.readInt(),
    parcel.readInt(),
    parcel.readString(),
    parcel.readInt(),
    parcel.readValue(Int::class.java.classLoader) as? Int,
    parcel.readParcelable(Uri::class.java.classLoader),
    TODO("outputCompressFormat"),
    parcel.readInt(),
    parcel.readInt(),
    parcel.readInt(),
    TODO("outputRequestSizeOptions"),
    parcel.readByte() != 0.toByte(),
    parcel.readParcelable(Rect::class.java.classLoader),
    parcel.readInt(),
    parcel.readByte() != 0.toByte(),
    parcel.readByte() != 0.toByte(),
    parcel.readByte() != 0.toByte(),
    parcel.readInt(),
    parcel.readByte() != 0.toByte(),
    parcel.readByte() != 0.toByte(),
    parcel.readString(),
    parcel.readInt(),
    parcel.readByte() != 0.toByte(),
    parcel.readByte() != 0.toByte(),
    parcel.readString(),
    parcel.createStringArrayList(),
    parcel.readFloat(),
    parcel.readInt(),
    parcel.readString(),
    parcel.readInt(),
    parcel.readValue(Int::class.java.classLoader) as? Int,
    parcel.readValue(Int::class.java.classLoader) as? Int,
    parcel.readValue(Int::class.java.classLoader) as? Int,
    parcel.readValue(Int::class.java.classLoader) as? Int
  )
*/
  init {
    require(maxZoom >= 0) { "Cannot set max zoom to a number < 1" }
    require(touchRadius >= 0) { "Cannot set touch radius value to a number <= 0 " }
    require(!(initialCropWindowPaddingRatio < 0 || initialCropWindowPaddingRatio >= 0.5)) { "Cannot set initial crop window padding value to a number < 0 or >= 0.5" }
    require(aspectRatioX > 0) { "Cannot set aspect ratio value to a number less than or equal to 0." }
    require(aspectRatioY > 0) { "Cannot set aspect ratio value to a number less than or equal to 0." }
    require(borderLineThickness >= 0) { "Cannot set line thickness value to a number less than 0." }
    require(borderCornerThickness >= 0) { "Cannot set corner thickness value to a number less than 0." }
    require(guidelinesThickness >= 0) { "Cannot set guidelines thickness value to a number less than 0." }
    require(minCropWindowHeight >= 0) { "Cannot set min crop window height value to a number < 0 " }
    require(minCropResultWidth >= 0) { "Cannot set min crop result width value to a number < 0 " }
    require(minCropResultHeight >= 0) { "Cannot set min crop result height value to a number < 0 " }
    require(maxCropResultWidth >= minCropResultWidth) { "Cannot set max crop result width to smaller value than min crop result width" }
    require(maxCropResultHeight >= minCropResultHeight) { "Cannot set max crop result height to smaller value than min crop result height" }
    require(outputRequestWidth >= 0) { "Cannot set request width value to a number < 0 " }
    require(outputRequestHeight >= 0) { "Cannot set request height value to a number < 0 " }
    require(!(rotationDegrees < 0 || rotationDegrees > DEGREES_360)) { "Cannot set rotation degrees value to a number < 0 or > 360" }
  }

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeByte(if (imageSourceIncludeGallery) 1 else 0)
    parcel.writeByte(if (imageSourceIncludeCamera) 1 else 0)
    parcel.writeFloat(cropCornerRadius)
    parcel.writeFloat(snapRadius)
    parcel.writeFloat(touchRadius)
    parcel.writeByte(if (showCropOverlay) 1 else 0)
    parcel.writeByte(if (showCropLabel) 1 else 0)
    parcel.writeByte(if (showProgressBar) 1 else 0)
    parcel.writeInt(progressBarColor)
    parcel.writeByte(if (autoZoomEnabled) 1 else 0)
    parcel.writeByte(if (multiTouchEnabled) 1 else 0)
    parcel.writeByte(if (centerMoveEnabled) 1 else 0)
    parcel.writeInt(maxZoom)
    parcel.writeFloat(initialCropWindowPaddingRatio)
    parcel.writeByte(if (fixAspectRatio) 1 else 0)
    parcel.writeInt(aspectRatioX)
    parcel.writeInt(aspectRatioY)
    parcel.writeFloat(borderLineThickness)
    parcel.writeInt(borderLineColor)
    parcel.writeFloat(borderCornerThickness)
    parcel.writeFloat(borderCornerOffset)
    parcel.writeFloat(borderCornerLength)
    parcel.writeInt(borderCornerColor)
    parcel.writeInt(circleCornerFillColorHexValue)
    parcel.writeFloat(guidelinesThickness)
    parcel.writeInt(guidelinesColor)
    parcel.writeInt(backgroundColor)
    parcel.writeInt(minCropWindowWidth)
    parcel.writeInt(minCropWindowHeight)
    parcel.writeInt(minCropResultWidth)
    parcel.writeInt(minCropResultHeight)
    parcel.writeInt(maxCropResultWidth)
    parcel.writeInt(maxCropResultHeight)
    parcel.writeString(activityTitle.toString())
    parcel.writeInt(activityMenuIconColor)
    parcel.writeValue(activityMenuTextColor)
    parcel.writeParcelable(customOutputUri, flags)
    parcel.writeInt(outputCompressQuality)
    parcel.writeInt(outputRequestWidth)
    parcel.writeInt(outputRequestHeight)
    parcel.writeByte(if (noOutputImage) 1 else 0)
    parcel.writeParcelable(initialCropWindowRectangle, flags)
    parcel.writeInt(initialRotation)
    parcel.writeByte(if (allowRotation) 1 else 0)
    parcel.writeByte(if (allowFlipping) 1 else 0)
    parcel.writeByte(if (allowCounterRotation) 1 else 0)
    parcel.writeInt(rotationDegrees)
    parcel.writeByte(if (flipHorizontally) 1 else 0)
    parcel.writeByte(if (flipVertically) 1 else 0)
    parcel.writeString(cropMenuCropButtonTitle?.toString())
    parcel.writeInt(cropMenuCropButtonIcon)
    parcel.writeByte(if (skipEditing) 1 else 0)
    parcel.writeByte(if (showIntentChooser) 1 else 0)
    parcel.writeString(intentChooserTitle)
    parcel.writeStringList(intentChooserPriorityList)
    parcel.writeFloat(cropperLabelTextSize)
    parcel.writeInt(cropperLabelTextColor)
    parcel.writeString(cropperLabelText)
    parcel.writeInt(activityBackgroundColor)
    parcel.writeValue(toolbarColor)
    parcel.writeValue(toolbarTitleColor)
    parcel.writeValue(toolbarBackButtonColor)
    parcel.writeValue(toolbarTintColor)
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Parcelable.Creator<CropImageOptions> {
    override fun createFromParcel(parcel: Parcel): CropImageOptions {
      return CropImageOptions(true)
    }

    override fun newArray(size: Int): Array<CropImageOptions?> {
      return arrayOfNulls(size)
    }
  }
}

internal const val DEGREES_360 = 360
