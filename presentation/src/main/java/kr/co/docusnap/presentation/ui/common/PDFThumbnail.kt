package kr.co.docusnap.presentation.ui.common

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import kr.co.docusnap.presentation.viewmodel.MainViewModel

@Composable
fun PDFThumbnail(
    modifier: Modifier = Modifier,
    viewmodel: MainViewModel,
    fileName: String,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val thumbnail by produceState<Bitmap?>(initialValue = null, fileName) {
        value = viewmodel.loadPdfThumbnail(context, fileName)
    }

    if (thumbnail != null) {
        Image(
            bitmap = thumbnail!!.asImageBitmap(),
            contentDescription = "PDF Thumbnail",
            modifier = modifier
                .clickable { onClick() }
        )
    } else {
        Box(
            modifier = modifier
                .background(Color.Gray)
                .fillMaxSize()
        ) {
            Text(
                text = "썸네일 로딩 중",
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}