package kr.co.docusnap.presentation.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kr.co.docusnap.domain.model.BaseModel
import kr.co.docusnap.domain.model.ScanFile
import kr.co.docusnap.presentation.model.MainVM
import kr.co.docusnap.presentation.model.PresentationVM
import kr.co.docusnap.presentation.viewmodel.MainViewModel

@Composable
fun FileListScreen(
    viewModel: MainViewModel,
    list: List<PresentationVM<out BaseModel>>,
    columnCount: Int,
    onImageClick: (ScanFile) -> Unit,
    onPdfClick: (ScanFile) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 50.dp),
        horizontalArrangement = Arrangement.spacedBy(
            space = 8.dp,
            alignment = Alignment.CenterHorizontally
        ),
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 70.dp)
    ) {
        items(
            count = list.size,
            span = {
                GridItemSpan(columnCount)
            }
        ) { index ->
            val scanFile = list[index]
            FileCard(
                viewModel = viewModel,
                presentationVM = scanFile as MainVM,
                onImageClick = {
                    onImageClick(scanFile.model)
                },
                onPdfClick = {
                    onPdfClick(scanFile.model)
                }
            )
        }
    }
}