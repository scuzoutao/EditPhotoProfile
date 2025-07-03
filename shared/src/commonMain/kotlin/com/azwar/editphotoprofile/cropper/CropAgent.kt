package com.azwar.editphotoprofile.cropper

import com.azwar.editphotoprofile.cropper.model.CropOutline
import com.azwar.editphotoprofile.cropper.model.CropShape
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.withSaveLayer
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection

class CropAgent {
    private val imagePaint = Paint().apply {
        blendMode = BlendMode.SrcIn
    }
    private val paint = Paint()

    fun crop(
        imageBitmap: ImageBitmap,
        cropRect: Rect,
        cropOutline: CropOutline,
        layoutDirection: LayoutDirection,
        density: Density,
    ): ImageBitmap {
        val imageToCrop = createImageBitmapWithArea(
            source = imageBitmap,
            width = cropRect.width.toInt(),
            height = cropRect.height.toInt(),
            srcLeft = -cropRect.left.toInt(),
            srcTop = -cropRect.top.toInt(),
            srcWidth = imageBitmap.width,
            srcHeight = imageBitmap.height
        )

        //把图片切成指定的形状
        drawCroppedImage(cropOutline, cropRect, layoutDirection, density, imageToCrop)

        return imageToCrop
    }

    private fun drawCroppedImage(
        cropOutline: CropOutline,
        cropRect: Rect,
        layoutDirection: LayoutDirection,
        density: Density,
        imageToCrop: ImageBitmap,
    ) {

        when (cropOutline) {
            is CropShape -> {
                val path = Path().apply {
                    val outline =
                        cropOutline.shape.createOutline(cropRect.size, layoutDirection, density)
                    addOutline(outline)
                }
                Canvas(image = imageToCrop).run {
                    withSaveLayer(cropRect, imagePaint) {
                        drawPath(path, paint)
                        drawImage(imageToCrop, Offset.Zero, imagePaint)
                    }
                }
            }
        }
    }

    fun resize(
        source: ImageBitmap,
        requiredWidth: Int,
        requiredHeight: Int
    ): ImageBitmap {
        val resized = ImageBitmap(requiredWidth, requiredHeight, ImageBitmapConfig.Argb8888)
        val canvas = Canvas(resized)
        val paint = Paint().apply {
            isAntiAlias = true
        }

        canvas.drawImageRect(
            image = source,
            srcOffset = IntOffset(0, 0),
            srcSize = IntSize(source.width, source.height),
            dstOffset = IntOffset(0, 0),
            dstSize = IntSize(requiredWidth, requiredHeight),
            paint = paint
        )

        return resized
    }
}

fun createImageBitmapWithArea(
    source: ImageBitmap,
    width: Int,
    height: Int,
    srcLeft: Int,
    srcTop: Int,
    srcWidth: Int,
    srcHeight: Int
): ImageBitmap {
    val paint = Paint()
    paint.isAntiAlias = true
    val clipBitmap = ImageBitmap(width, height, config = source.config)
    val canvas = Canvas(clipBitmap)
    canvas.drawImageRect(
        source,
        dstOffset = IntOffset(srcLeft, srcTop),
        dstSize = IntSize(srcWidth, srcHeight),
        paint = paint)
    return clipBitmap
}