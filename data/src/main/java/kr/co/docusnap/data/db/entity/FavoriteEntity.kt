package kr.co.docusnap.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kr.co.docusnap.data.db.converter.ZonedDateTimeConverters
import kr.co.docusnap.domain.model.FileType
import kr.co.docusnap.domain.model.ScanFile
import java.time.ZonedDateTime

@Entity(tableName = "favorite")
@TypeConverters(ZonedDateTimeConverters::class)
data class FavoriteEntity(
    @PrimaryKey
    val id: Int,
    val filePath: String,
    val fileName: String,
    val fileType: FileType,
    val isLocked: Boolean = false,
    val createAt: ZonedDateTime = ZonedDateTime.now(),
    val isFavorite: Boolean = false,
)

fun FavoriteEntity.toDomainModel(): ScanFile {
    return ScanFile(
        id = id,
        filePath = filePath,
        fileName = fileName,
        fileType = fileType,
        isLocked = isLocked,
        createAt = createAt,
        isFavorite = isFavorite,
    )
}

fun ScanFile.toFavoriteEntity(): FavoriteEntity {
    return FavoriteEntity(
        id = id,
        filePath = filePath,
        fileName = fileName,
        fileType = fileType,
        isLocked = isLocked,
        createAt = createAt,
        isFavorite = isFavorite,
    )
}