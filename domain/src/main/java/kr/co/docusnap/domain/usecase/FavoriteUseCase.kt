package kr.co.docusnap.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.docusnap.domain.model.ScanFile
import kr.co.docusnap.domain.repository.FavoriteRepository
import javax.inject.Inject

class FavoriteUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {

    fun getFavoriteFiles(): Flow<List<ScanFile>> {
        return repository.getFavoriteFiles()
    }

    suspend fun addFavoriteFile(scanFile: ScanFile) {
        repository.addFavoriteFile(scanFile)
    }

    suspend fun updateLockFile(scanFile: ScanFile) {
        return repository.lockFile(scanFile)
    }
}