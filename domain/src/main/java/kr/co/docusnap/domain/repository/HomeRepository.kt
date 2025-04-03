package kr.co.docusnap.domain.repository

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.flow.Flow
import kr.co.docusnap.domain.model.BaseModel
import kr.co.docusnap.domain.model.FileType
import kr.co.docusnap.domain.model.FilterType
import kr.co.docusnap.domain.model.ScanFile

interface HomeRepository {

    fun getScanFilesByFilter(type: FilterType): Flow<List<BaseModel>>

    fun getScanFileById(id: Int): Flow<ScanFile>

    suspend fun favoriteFile(scanFile: ScanFile)

    suspend fun lockFile(scanFile: ScanFile)

    suspend fun removeFile(scanFile: ScanFile)

    suspend fun saveScanFiles(uriPairList: List<Pair<FileType, Uri>>)

    suspend fun removeAllData()
}