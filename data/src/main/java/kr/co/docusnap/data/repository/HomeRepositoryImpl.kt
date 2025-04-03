package kr.co.docusnap.data.repository

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kr.co.docusnap.data.db.dao.FavoriteDao
import kr.co.docusnap.data.db.dao.ScanFileDao
import kr.co.docusnap.data.db.entity.ScanFileEntity
import kr.co.docusnap.data.db.entity.toDomainModel
import kr.co.docusnap.data.db.entity.toEntity
import kr.co.docusnap.domain.model.BaseModel
import kr.co.docusnap.domain.model.FileType
import kr.co.docusnap.domain.model.FilterType
import kr.co.docusnap.domain.model.ScanFile
import kr.co.docusnap.domain.repository.HomeRepository
import java.io.File
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val scanFileDao: ScanFileDao,
    private val favoriteDao: FavoriteDao
): HomeRepository {

    override fun getScanFilesByFilter(type: FilterType): Flow<List<BaseModel>> {
        val list = when(type) {
            FilterType.ALL -> scanFileDao.getAll()
            FilterType.IMAGE -> scanFileDao.getScanFilesByType(FileType.IMAGE)
            FilterType.PDF -> scanFileDao.getScanFilesByType(FileType.PDF)
        }

        val favoriteList = when(type) {
            FilterType.ALL -> favoriteDao.getAll()
            FilterType.IMAGE -> favoriteDao.getFavoriteFilesByType(FileType.IMAGE)
            FilterType.PDF -> favoriteDao.getFavoriteFilesByType(FileType.PDF)
        }

        return list.combine(favoriteList) { scanFiles, favoriteFiles ->
            scanFiles.map { model ->
                val convertedModel = model.toDomainModel()
                mappingLike(convertedModel, favoriteFiles.map { it.id })
            }
        }
    }

    override fun getScanFileById(id: Int): Flow<ScanFile> {
        return scanFileDao.getScanFileById(id).map { it.toDomainModel() }
    }

    override suspend fun favoriteFile(scanFile: ScanFile) {
        scanFileDao.updateScanFile(
            scanFile.copy(isFavorite = !scanFile.isFavorite).toEntity()
        )
    }

    override suspend fun lockFile(scanFile: ScanFile) {
        scanFileDao.updateScanFile(
            scanFile.copy(isLocked = !scanFile.isLocked).toEntity()
        )
    }

    private fun mappingLike(baseModel: BaseModel, likeProductIds: List<Int>): BaseModel {
        return when(baseModel) {
            is ScanFile -> { updateLikeStatus(baseModel, likeProductIds) }
            else -> baseModel
        }
    }

    // 상품 좋아요 상태 업데이트
    private fun updateLikeStatus(scanFile: ScanFile, likeFiles: List<Int>): ScanFile {
        return scanFile.copy(isFavorite = likeFiles.contains(scanFile.id))
    }

    override suspend fun removeFile(scanFile: ScanFile) {
        TODO("Not yet implemented")
    }

    override suspend fun saveScanFiles(uriPairList: List<Pair<FileType, Uri>>) {
        val savedFiles = coroutineScope {
            uriPairList.map { (type, uri) ->
                async(Dispatchers.IO) {
                    val fileName = when(type) {
                        FileType.IMAGE -> "scanned_${System.currentTimeMillis()}.jpg"
                        FileType.PDF -> "scanned_${System.currentTimeMillis()}.pdf"
                    }
                    val filePath = saveUriToFile(uri, fileName)
                    filePath?.let { path ->
                        ScanFileEntity(
                            filePath = path,
                            fileName = fileName,
                            fileType = type
                        )
                    }
                }
            }.awaitAll().filterNotNull()
        }

        scanFileDao.insertScanFiles(savedFiles)
    }

    private suspend fun saveUriToFile(uri: Uri, fileName: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri) ?: return@withContext null
                val file = File(context.filesDir, fileName)
                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
                file.absolutePath
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    override suspend fun removeAllData() {
        scanFileDao.removeAll()
        favoriteDao.deleteAll()
    }
}