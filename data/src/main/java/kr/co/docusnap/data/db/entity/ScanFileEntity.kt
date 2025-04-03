package kr.co.docusnap.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kr.co.docusnap.data.db.converter.ZonedDateTimeConverters
import kr.co.docusnap.domain.model.FileType
import kr.co.docusnap.domain.model.ScanFile
import java.time.ZonedDateTime

@Entity(tableName = "scan_file")
@TypeConverters(ZonedDateTimeConverters::class)
data class ScanFileEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val filePath: String,
    val fileName: String,
    val fileType: FileType,
    val isLocked: Boolean = false,
    val createAt: ZonedDateTime = ZonedDateTime.now(),
    val updateAt: ZonedDateTime = ZonedDateTime.now(),
    val isFavorite: Boolean = false,
)

fun ScanFileEntity.toDomainModel(): ScanFile {
    return ScanFile(
        id = id,
        filePath = filePath,
        fileName = fileName,
        fileType = fileType,
        isLocked = isLocked,
        createAt = createAt,
        updateAt = updateAt,
        isFavorite = isFavorite,
    )
}

fun ScanFile.toEntity(): ScanFileEntity {
    return ScanFileEntity(
        id = id,
        filePath = filePath,
        fileName = fileName,
        fileType = fileType,
        isLocked = isLocked,
        createAt = createAt,
        updateAt = updateAt,
        isFavorite = isFavorite,
    )
}
