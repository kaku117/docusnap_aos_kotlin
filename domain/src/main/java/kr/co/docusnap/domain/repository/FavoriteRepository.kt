package kr.co.docusnap.domain.repository

import kotlinx.coroutines.flow.Flow
import kr.co.docusnap.domain.model.ScanFile

interface FavoriteRepository {

    fun getFavoriteFiles(): Flow<List<ScanFile>>

    suspend fun addFavoriteFile(scanFile: ScanFile)

    suspend fun lockFile(scanFile: ScanFile)

}