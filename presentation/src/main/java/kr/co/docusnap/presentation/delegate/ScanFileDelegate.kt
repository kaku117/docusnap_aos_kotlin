package kr.co.docusnap.presentation.delegate

import android.content.Context
import android.net.Uri
import androidx.navigation.NavHostController
import kr.co.docusnap.domain.model.FileType
import kr.co.docusnap.domain.model.ScanFile

interface ScanFileDelegate {

    fun showDocumentViewerScreen(context: Context, navHostController: NavHostController, scanFile: ScanFile)

    fun showImageViewerScreen(context: Context, navHostController: NavHostController, scanFile: ScanFile)

    fun onFavoriteButtonClick(scanFile: ScanFile)

    fun onShareButtonClick(context: Context, uri: Uri, type: FileType)

    fun onLockButtonClick(context: Context, scanFile: ScanFile, )
}