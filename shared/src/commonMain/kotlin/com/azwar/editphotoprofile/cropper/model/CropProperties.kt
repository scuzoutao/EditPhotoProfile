package com.azwar.editphotoprofile.cropper.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ContentScale.Companion.Fit
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp


object CropDefaults {

    fun properties(
        maxZoom: Float = 5f,
        contentScale: ContentScale = Fit,
        cropOutlineProperty: CropOutlineProperty = CropOutlineProperty(
            OutlineType.Rect,
            RectCropShape(0, "Rect")
        ),
        aspectRatio: AspectRatio = AspectRatio(1 / 1f),
        overlayRatio: Float = 1f,
        pannable: Boolean = true,
        fling: Boolean = false,
        zoomable: Boolean = true,
        rotatable: Boolean = false,
        fixedAspectRatio: Boolean = false,
        requiredSize: IntSize? = null,
        minDimension: IntSize? = null,
        panelColor: Color? = null,
        padding: Dp = 0.dp,
        scaleIn: Boolean = true
    ): CropProperties {
        return CropProperties(
            contentScale = contentScale,
            cropOutlineProperty = cropOutlineProperty,
            maxZoom = maxZoom,
            aspectRatio = aspectRatio,
            overlayRatio = overlayRatio,
            pannable = pannable,
            fling = fling,
            zoomable = zoomable,
            rotatable = rotatable,
            fixedAspectRatio = fixedAspectRatio,
            requiredSize = requiredSize,
            minDimension = minDimension,
            panelColor = panelColor,
            padding = padding,
            scaleIn = scaleIn
        )
    }

    fun style(
        backgroundColor: Color = Color.Transparent
    ): CropStyle {
        return CropStyle(
            backgroundColor = backgroundColor
        )
    }
}

@Immutable
data class CropProperties internal constructor(
    val contentScale: ContentScale,
    val cropOutlineProperty: CropOutlineProperty,
    val aspectRatio: AspectRatio,
    val overlayRatio: Float,
    val pannable: Boolean,
    val fling: Boolean,
    val rotatable: Boolean,
    val zoomable: Boolean,
    val maxZoom: Float,
    val fixedAspectRatio: Boolean = false,
    val requiredSize: IntSize? = null,
    val minDimension: IntSize? = null,
    val panelColor: Color? = null,
    val padding: Dp = 0.dp,
    val scaleIn: Boolean = true,
)

@Immutable
data class CropStyle internal constructor(
    val backgroundColor: Color,
)

@Immutable
data class CropOutlineProperty(
    val outlineType: OutlineType,
    val cropOutline: CropOutline
)

enum class OutlineType {
    Rect, RoundedRect, CutCorner, Oval, Polygon, Custom, ImageMask
}