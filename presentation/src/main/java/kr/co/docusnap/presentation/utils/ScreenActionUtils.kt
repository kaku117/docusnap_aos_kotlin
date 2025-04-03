package kr.co.docusnap.presentation.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize

object ScreenActionUtils {

    fun getOffsetByGestures(containerSize: IntSize, offset: Offset, pan: Offset, newScale: Float): Offset {
        val containerWidth = containerSize.width.toFloat()
        val containerHeight = containerSize.height.toFloat()
        val scaleWidth = containerWidth * newScale
        val scaleHeight = containerHeight * newScale

        val maxOffsetX = ((scaleWidth - containerWidth) / 2).coerceAtLeast(0f)
        val maxOffsetY = ((scaleHeight - containerHeight) / 2).coerceAtLeast(0f)

        return if (newScale > 1f) {
            Offset(
                x = (offset.x + pan.x).coerceIn(-maxOffsetX, maxOffsetX),
                y = (offset.y + pan.y).coerceIn(-maxOffsetY, maxOffsetY)
            )
        } else {
            Offset.Zero
        }
    }
}