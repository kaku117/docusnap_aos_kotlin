package kr.co.docusnap.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kr.co.docusnap.data.db.dao.FavoriteDao
import kr.co.docusnap.data.db.entity.toDomainModel
import kr.co.docusnap.data.db.entity.toFavoriteEntity
import kr.co.docusnap.domain.model.ScanFile
import kr.co.docusnap.domain.repository.FavoriteRepository
import java.time.ZonedDateTime
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val dao: FavoriteDao
): FavoriteRepository {

    override fun getFavoriteFiles(): Flow<List<ScanFile>> {
        return dao.getAll().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun addFavoriteFile(scanFile: ScanFile) {
        if (scanFile.isFavorite) {
            dao.delete(scanFile.id)
        } else {
            dao.insert(scanFile.toFavoriteEntity().copy(
                createAt = ZonedDateTime.now(),
                isFavorite = true
            ))
        }
    }

    override suspend fun lockFile(scanFile: ScanFile) {
        dao.update(scanFile.toFavoriteEntity().copy(
            isLocked = !scanFile.isLocked
        ))
    }
}