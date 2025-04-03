package kr.co.docusnap.presentation.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kr.co.docusnap.domain.model.FilterType
import kr.co.docusnap.presentation.R
import kr.co.docusnap.presentation.ui.common.EmptyScreen
import kr.co.docusnap.presentation.ui.common.FileListScreen
import kr.co.docusnap.presentation.ui.theme.DocuSnapTheme
import kr.co.docusnap.presentation.viewmodel.MainViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: MainViewModel
) {
    val context = LocalContext.current

    val options = FilterType.entries
    var selectionOption by rememberSaveable { mutableStateOf(options[0]) }

    val scanFiles by viewModel.getScanFiles(selectionOption).collectAsState(initial = listOf())
    val columnCount by viewModel.columnCount.collectAsState()

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        IconButton(
            onClick = {
                viewModel.requestDocumentScan()
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp)
        ) {
            Icon(
                imageVector = Icons.Sharp.Add,
                contentDescription = "Add",
                modifier = Modifier.size(24.dp)
            )
        }

        if (scanFiles.isEmpty()) {
            EmptyScreen(
                emptyMessage = stringResource(id = R.string.home_screen_empty_file_msg)
            )
        } else {
            SingleSelectionChips(options, selectionOption) { option ->
                selectionOption = option
            }

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

@Preview
@Composable
fun HomeScreenPreview() {
    DocuSnapTheme {
        HomeScreen(
            Modifier.padding(30.dp),
            rememberNavController(),
            hiltViewModel<MainViewModel>()
        )
    }
}