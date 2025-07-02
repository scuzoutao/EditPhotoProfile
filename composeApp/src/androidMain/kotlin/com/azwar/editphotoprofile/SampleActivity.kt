package com.azwar.editphotoprofile

import com.azwar.editphotoprofile.cropper.ImageCropper
import com.azwar.editphotoprofile.cropper.model.CropDefaults
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers


class SampleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding(),
                color = MaterialTheme.colorScheme.background
            ) {
                ImageCropDemoSimple()
            }
        }
    }
}

@Composable
fun ImageCropDemoSimple() {
    val cropProperties = remember {
        CropDefaults.properties(
            panelColor = Color.Red,
            padding = 20.dp,
            scaleIn = false
        )
    }
    val imageBitmapLarge = ImageBitmap.imageResource(
        LocalContext.current.resources,
        R.drawable.img_yourname
    )

    val imageBitmap by remember { mutableStateOf(imageBitmapLarge) }
    var crop by remember { mutableStateOf(false) }

    var showDialog by remember { mutableStateOf(false) }

    var croppedImage by remember { mutableStateOf<ImageBitmap?>(null) }
    var isCropping by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray),
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            Column(modifier = Modifier.fillMaxSize()) {

                ImageCropper(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Red)
                        .weight(1f),
                    imageBitmap = imageBitmap,
                    contentDescription = "Image Cropper",
                    cropProperties = cropProperties,
                    crop = crop,
                    onCropStart = {
                        isCropping = true
                    },
                    onCropSuccess = {
                        croppedImage = it
                        isCropping = false
                        crop = false
                        showDialog = true
                    },
                )
            }

            FixedSquareTransparentOverlay()

            Button(onClick = { crop = true }) {
                Text(text = "Potong")
            }

            if (showDialog) {
                croppedImage?.let {
                    ShowCroppedImageDialog(imageBitmap = it) {
                        showDialog = !showDialog
                        croppedImage = null
                    }
                }
            }
        }
    }
}

@Composable
fun FixedSquareTransparentOverlay(
    modifier: Modifier = Modifier,
    overlayColor: Color = Color(0x80000000),
    padding: Dp = 20.dp // 四周留白
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val paddingPx = padding.toPx()
        val squareSize = minOf(size.width, size.height) - paddingPx * 2

        val topLeft = Offset(
            x = (size.width - squareSize) / 2,
            y = (size.height - squareSize) / 2
        )

        val rectPath = Path().apply {
            addRect(Rect(topLeft, Size(squareSize, squareSize)))
        }

        // 挖空正方形区域
        clipPath(rectPath, clipOp = ClipOp.Difference) {
            drawRect(
                color = overlayColor,
                size = size
            )
        }
    }
}


@Composable
private fun ShowCroppedImageDialog(imageBitmap: ImageBitmap, onDismissRequest: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        text = {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.Fit,
                bitmap = imageBitmap,
                contentDescription = "result"
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}
