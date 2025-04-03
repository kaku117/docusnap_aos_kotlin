package kr.co.docusnap.domain.usecase

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kr.co.docusnap.domain.model.BaseModel
import kr.co.docusnap.domain.model.FileType
import kr.co.docusnap.domain.model.FilterType
import kr.co.docusnap.domain.model.ScanFile
import kr.co.docusnap.domain.repository.HomeRepository
import java.io.File
import javax.inject.Inject
import javax.inject.Named

class HomeUseCase @Inject constructor(
    @Named("ApplicationId") val applicationId: String,
    private val homeRepository: HomeRepository
) {

    private val PROVIDER_AUTHORITY = "$applicationId.fileprovider"

    fun getScanFilesByFilter(type: FilterType): Flow<List<BaseModel>> {
        return homeRepository.getScanFilesByFilter(type)
    }

    fun getScanFile(id: Int): Flow<ScanFile> {
        return homeRepository.getScanFileById(id)
    }

    suspend fun updateFavoriteFile(scanFile: ScanFile) {
        return homeRepository.favoriteFile(scanFile)
    }

    // 지문 인증
    fun requestBiometricForLock(
        context: Context,
        title: String,
        subTitle: String,
        onSuccess: () -> Unit,
        onFailed: (String) -> Unit
    ) {
        val executor = ContextCompat.getMainExecutor(context)

        val biometricPrompt: BiometricPrompt = BiometricPrompt(
            context as AppCompatActivity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)

                    onSuccess()

                    Log.d("Biometric", "Authentication succeeded!")
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)

                    onFailed(errString.toString())

                    Log.e("Biometric", "Authentication error: $errString")
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()

                    onFailed("지문 인식이 취소되었습니다.")

                    Log.e("Biometric", "Authentication failed")
                }
            })

        val promptInfo: BiometricPrompt.PromptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subTitle)
            .setNegativeButtonText("취소")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    suspend fun updateLockFile(scanFile: ScanFile) {
        return homeRepository.lockFile(scanFile)
    }

    // 문서 이미지 스캔 결과 처리
    suspend fun handleScanResult(result: GmsDocumentScanningResult?) {
        val uriPairList = mutableListOf<Pair<FileType, Uri>>()

        result?.pages?.let { pages ->
            for (page in pages) {
                val imageUri = pages[0].imageUri
                uriPairList.add(Pair(FileType.IMAGE, imageUri))
                Log.d("aaa", "imageUri : $imageUri")
            }
        }
        result?.pdf?.let { pdf ->
            val pdfUri = pdf.uri
            val pageCount = pdf.pageCount
            uriPairList.add(Pair(FileType.PDF, pdfUri))
            Log.d("aaa", "pdfUri : $pdfUri")
        }

        Log.d("aaa", "uriPairList : $uriPairList")

        homeRepository.saveScanFiles(uriPairList)
    }

    // PDF 썸네일 이미지 추출
    suspend fun loadPdfThumbnail(context: Context, fileName: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            val pdfContentUri = getUriForFileByFileProvider(context, fileName)
            var pdfRenderer: PdfRenderer? = null
            var fileDescriptor: ParcelFileDescriptor? = null

            try {
                fileDescriptor = context.contentResolver.openFileDescriptor(pdfContentUri, "r")
                if (fileDescriptor != null) {
                    pdfRenderer = PdfRenderer(fileDescriptor)
                    if (pdfRenderer.pageCount > 0) {
                        pdfRenderer.openPage(0).use { page ->
                            val width = page.width / 2
                            val height = page.height / 2
                            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                            page.render(bitmap, Rect(0, 0, width, height), null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                            return@withContext bitmap
                        }
                    }
                }
                null
            } catch (e: Exception) {
                e.printStackTrace()
                null
            } finally {
                pdfRenderer?.close()
                fileDescriptor?.close()
            }
        }
    }

    // File Provider 가져오는 처리
    fun getUriForFileByFileProvider(context: Context, fileName: String): Uri  {
        val file = File(context.filesDir, fileName)
        return FileProvider.getUriForFile(context, PROVIDER_AUTHORITY, file)
    }
}