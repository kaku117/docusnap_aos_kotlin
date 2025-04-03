package kr.co.docusnap.presentation.ui.viewer

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import coil.compose.AsyncImage
import kr.co.docusnap.presentation.model.MainVM
import kr.co.docusnap.presentation.utils.ScreenActionUtils
import kr.co.docusnap.presentation.viewmodel.MainViewModel

@Composable
fun ImageViewerScreen(
    viewModel: MainViewModel,
    presentationVM: MainVM
) {
    val context = LocalContext.current

    val fileUri = viewModel.getUriForFileByFileProvider(context, presentationVM.model.fileName)

    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var containerSize by remember { mutableStateOf(IntSize.Zero) }

    val transformModifier = Modifier.pointerInput(Unit) {
        detectTransformGestures { _, pan, zoom, _ ->
            val minScale = 1f
            val maxScale = 5f

            val newScale = (zoom * scale).coerceIn(minScale, maxScale)

            if (containerSize != IntSize.Zero) {
                offset = ScreenActionUtils.getOffsetByGestures(containerSize, offset, pan, newScale)
            }
            scale = newScale
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .onGloballyPositioned { coordinates ->
                containerSize = coordinates.size
            }
    ) {
        AsyncImage(
            model = fileUri,
            contentDescription = "Image File",
            modifier = Modifier
                .fillMaxSize()
                .then(transformModifier)
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y
                )
        )

        BottomActionScreen(
            fileId = presentationVM.model.id,
            viewModel = viewModel,
            onFavoriteClick = { model ->
                presentationVM.onFavoriteButtonClick(model)
            },
            onShareClick = { type ->
                presentationVM.onShareButtonClick(context, fileUri, type)
            },
            onLockClick = { model ->
                presentationVM.onLockButtonClick(context, model)
            }
        )
    }
}