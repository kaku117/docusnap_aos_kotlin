package kr.co.docusnap.presentation.ui.viewer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.rajat.pdfviewer.PdfRendererView
import com.rajat.pdfviewer.compose.PdfRendererViewCompose
import kr.co.docusnap.presentation.model.MainVM
import kr.co.docusnap.presentation.viewmodel.MainViewModel

@Composable
fun DocumentViewerScreen(
    viewModel: MainViewModel,
    presentationVM: MainVM
) {
    val context = LocalContext.current

//    var isShowBottomActionBar by remember { mutableStateOf(false) }

    val pdfContentUri = viewModel.getUriForFileByFileProvider(context, presentationVM.model.fileName)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        PdfRendererViewCompose(
            uri = pdfContentUri,
            modifier = Modifier
//                .pointerInput(Unit) {
//                    detectTapGestures(
//                        onTap = {
//                            isShowBottomActionBar = !isShowBottomActionBar
//                            Log.d("aaa", "PdfRendererViewCompose tapped. isShowBottomActionBar: $isShowBottomActionBar")
//                        }
//                    )
//                }
                .fillMaxSize(),
            statusCallBack = object : PdfRendererView.StatusCallBack {

            }
        )
        BottomActionScreen(
            fileId = presentationVM.model.id,
            viewModel = viewModel,
            onFavoriteClick = { model ->
                presentationVM.onFavoriteButtonClick(model)
            },
            onShareClick = { type ->
                presentationVM.onShareButtonClick(context, pdfContentUri, type)
            },
            onLockClick = { model ->
                presentationVM.onLockButtonClick(context, model)
            }
        )
//        AnimatedVisibility(
//            visible = isShowBottomActionBar,
//            modifier = Modifier
//                .fillMaxWidth()
//                .align(Alignment.BottomCenter)
//        ) {
//            BottomActionScreen(
//                isFavorite = presentationVM.model.isFavorite,
//                onFavoriteClick = {
//                    presentationVM.onFavoriteButtonClick(presentationVM.model)
//                },
//                onShareClick = {
//
//                }
//            )
//        }
    }
}