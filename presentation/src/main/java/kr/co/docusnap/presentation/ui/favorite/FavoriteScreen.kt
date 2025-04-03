package kr.co.docusnap.presentation.ui.favorite

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import kr.co.docusnap.presentation.R
import kr.co.docusnap.presentation.ui.common.EmptyScreen
import kr.co.docusnap.presentation.ui.common.FileListScreen
import kr.co.docusnap.presentation.viewmodel.MainViewModel

@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: MainViewModel
) {
    val context = LocalContext.current

    val scanFiles by viewModel.favoriteFiles.collectAsState(initial = listOf())
    val columnCount by viewModel.columnCount.collectAsState()

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        if (scanFiles.isEmpty()) {
            EmptyScreen(
                emptyMessage = stringResource(R.string.favorite_screen_empty_file_msg)
            )
        } else {
            FileListScreen(
                viewModel = viewModel,
                list = scanFiles,
                columnCount = columnCount,
                onImageClick = { file ->
                    viewModel.showImageViewerScreen(context, navController, file)
                },
                onPdfClick = { file ->
                    viewModel.showDocumentViewerScreen(context, navController, file)
                }
            )
        }
    }
}