package kr.co.docusnap.presentation.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import kr.co.docusnap.domain.model.FileType
import kr.co.docusnap.presentation.model.MainVM
import kr.co.docusnap.presentation.viewmodel.MainViewModel

@Composable
fun FileCard(
    viewModel: MainViewModel,
    presentationVM: MainVM,
    onImageClick: () -> Unit,
    onPdfClick: () -> Unit
) {
    val modifier = if (presentationVM.model.isLocked) {
        Modifier.blur(16.dp)
    } else {
        Modifier
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(8.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .shadow(elevation = 4.dp)
        ) {
            when(presentationVM.model.fileType) {
                FileType.IMAGE -> {
                    Image(
                        painter = rememberAsyncImagePainter(presentationVM.model.filePath),
                        contentDescription = presentationVM.model.fileType.toString(),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth()
                            .clickable { onImageClick() }
                    )
                }
                FileType.PDF -> {
                    PDFThumbnail(Modifier.fillMaxWidth(), viewModel, presentationVM.model.fileName, onPdfClick)
                }
            }
        }

        Text(
            text = presentationVM.model.fileName,
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}