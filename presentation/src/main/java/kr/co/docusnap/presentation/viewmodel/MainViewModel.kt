package kr.co.docusnap.presentation.viewmodel

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kr.co.docusnap.domain.model.BaseModel
import kr.co.docusnap.domain.model.FileType
import kr.co.docusnap.domain.model.FilterType
import kr.co.docusnap.domain.model.ScanFile
import kr.co.docusnap.domain.usecase.FavoriteUseCase
import kr.co.docusnap.domain.usecase.HomeUseCase
import kr.co.docusnap.presentation.R
import kr.co.docusnap.presentation.delegate.ScanFileDelegate
import kr.co.docusnap.presentation.model.MainVM
import kr.co.docusnap.presentation.model.PresentationVM
import kr.co.docusnap.presentation.ui.DocumentViewerNav
import kr.co.docusnap.presentation.ui.ImageViewerNav
import kr.co.docusnap.presentation.utils.NavigationUtils
import kr.co.docusnap.presentation.utils.StringResUtils
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val homeUseCase: HomeUseCase,
    private val favoriteUseCase: FavoriteUseCase
): ViewModel(), ScanFileDelegate {

    private val _columnCount = MutableStateFlow(DEFAULT_ROW_COUNT)
    val columnCount: StateFlow<Int> = _columnCount

    private val _scanRequestEvent = MutableSharedFlow<Unit>()
    val scanRequestEvent: SharedFlow<Unit> = _scanRequestEvent

    val favoriteFiles = favoriteUseCase.getFavoriteFiles().map(::convertToPresentationVM)

    fun getScanFiles(type: FilterType): Flow<List<PresentationVM<out BaseModel>>> {
        return homeUseCase.getScanFilesByFilter(type).map(::convertToPresentationVM)
    }

    fun getScanFile(id: Int): Flow<ScanFile> {
        return homeUseCase.getScanFile(id)
    }

    // PDF 문서 보기로 이동
    override fun showDocumentViewerScreen(context: Context, navHostController: NavHostController, scanFile: ScanFile) {
        if (scanFile.isLocked) {
            homeUseCase.requestBiometricForLock(
                context = context,
                title = StringResUtils.getString(R.string.locked_file_access_notify_title),
                subTitle = StringResUtils.getString(R.string.locked_file_access_notify_msg),
                onSuccess = {
                    NavigationUtils.navigate(navHostController, DocumentViewerNav.navigationWithArg(scanFile))
                },
                onFailed = { failedMsg ->
                    Toast.makeText(context, failedMsg, Toast.LENGTH_SHORT).show()
                }
            )
        } else {
            NavigationUtils.navigate(navHostController, DocumentViewerNav.navigationWithArg(scanFile))
        }
    }

    // 이미지 보기로 이동
    override fun showImageViewerScreen(context: Context, navHostController: NavHostController, scanFile: ScanFile) {
        if (scanFile.isLocked) {
            homeUseCase.requestBiometricForLock(
                context = context,
                title = StringResUtils.getString(R.string.locked_image_access_notify_title),
                subTitle = StringResUtils.getString(R.string.locked_image_access_notify_msg),
                onSuccess = {
                    NavigationUtils.navigate(navHostController, ImageViewerNav.navigationWithArg(scanFile))
                },
                onFailed = { failedMsg ->
                    Toast.makeText(context, failedMsg, Toast.LENGTH_SHORT).show()
                }
            )
        } else {
            NavigationUtils.navigate(navHostController, ImageViewerNav.navigationWithArg(scanFile))
        }
    }


    override fun onFavoriteButtonClick(scanFile: ScanFile) {
        viewModelScope.launch {
            homeUseCase.updateFavoriteFile(scanFile)
            favoriteUseCase.addFavoriteFile(scanFile)
        }
    }

    override fun onShareButtonClick(context: Context, uri: Uri, type: FileType) {
        // 공유할 파일 타입, 공유 창 제목 정의
        val (fileType, shareTitle) = when(type) {
            FileType.PDF -> Pair("application/pdf", StringResUtils.getString(R.string.share_file_notify_title))
            FileType.IMAGE -> Pair("image/*", StringResUtils.getString(R.string.share_image_notify_title))
        }
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            this.type = fileType
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(shareIntent, shareTitle))
    }

    override fun onLockButtonClick(context: Context, scanFile: ScanFile) {
        viewModelScope.launch {
            if (!scanFile.isLocked) {
                // title, msg 정의
                val (title, msg) = when(scanFile.fileType) {
                    FileType.PDF -> Pair(
                        StringResUtils.getString(R.string.lock_file_notify_title),
                        StringResUtils.getString(R.string.lock_file_notify_msg)
                    )
                    FileType.IMAGE -> Pair(
                        StringResUtils.getString(R.string.lock_image_notify_title),
                        StringResUtils.getString(R.string.lock_image_notify_msg)
                    )
                }

                homeUseCase.requestBiometricForLock(
                    context = context,
                    title = title,
                    subTitle = msg,
                    onSuccess = {
                        viewModelScope.launch {
                            homeUseCase.updateLockFile(scanFile)
                            favoriteUseCase.updateLockFile(scanFile)
                        }
                    },
                    onFailed = { failedMsg ->
                        Toast.makeText(context, failedMsg, Toast.LENGTH_SHORT).show()
                    }
                )
            } else {
                homeUseCase.updateLockFile(scanFile)
                favoriteUseCase.updateLockFile(scanFile)
            }
        }
    }

    fun updateColumnCount(count: Int) {
        viewModelScope.launch {
            _columnCount.emit(count)
        }
    }

    fun requestDocumentScan() {
        viewModelScope.launch {
            _scanRequestEvent.emit(Unit)
        }
    }

    fun handleScanResult(result: GmsDocumentScanningResult?) {
        viewModelScope.launch {
            homeUseCase.handleScanResult(result)
        }
    }

    suspend fun loadPdfThumbnail(context: Context, fileName: String): Bitmap? {
        return homeUseCase.loadPdfThumbnail(context, fileName)
    }

    fun getUriForFileByFileProvider(context: Context, fileName: String): Uri {
        return homeUseCase.getUriForFileByFileProvider(context, fileName)
    }

    fun convertModelToModelVM(model: BaseModel): PresentationVM<out BaseModel> {
        return when(model) {
            is ScanFile -> MainVM(model, this)
        }
    }

    private fun convertToPresentationVM(list: List<BaseModel>): List<PresentationVM<out BaseModel>> {
        return list.map { model ->
            when(model) {
                is ScanFile -> MainVM(model, this)
            }
        }
    }

    companion object {
        private const val DEFAULT_ROW_COUNT = 2
    }
}