package com.azwar.editphotoprofile.cropper.util

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.withSaveLayer

fun DrawScope.drawWithLayer(block: DrawScope.() -> Unit) {
    drawContext.canvas.withSaveLayer(Rect.Zero, Paint()) {
        block()
    }
}