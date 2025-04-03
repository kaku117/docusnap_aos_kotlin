package kr.co.docusnap.domain.model

import java.time.ZonedDateTime

data class ScanFile(
    val id: Int,
    val filePath: String,
    val fileName: String,
    val fileType: FileType,
    val isLocked: Boolean = false,
    val createAt: ZonedDateTime = ZonedDateTime.now(),
    val updateAt: ZonedDateTime = ZonedDateTime.now(),
    val isFavorite: Boolean = false,
    override val type: ModelType = ModelType.HOME,
): BaseModel()

enum class FileType {
    PDF, IMAGE
}

enum class FilterType {
    ALL, PDF, IMAGE
}