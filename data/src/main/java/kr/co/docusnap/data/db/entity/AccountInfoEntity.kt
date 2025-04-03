package kr.co.docusnap.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kr.co.docusnap.domain.model.AccountInfo
import kr.co.docusnap.domain.model.AccountInfo.Type

@Entity(tableName = "account_info")
data class AccountInfoEntity(
    val nickname: String,
    val type: Type,
    val profileImageUrl: String,
    @PrimaryKey
    val tokenId: String
)

fun AccountInfoEntity.toDomainModel(): AccountInfo {
    return AccountInfo(
        nickname = nickname,
        type = type,
        profileImageUrl = profileImageUrl,
        tokenId = tokenId
    )
}

fun AccountInfo.toEntity(): AccountInfoEntity {
    return AccountInfoEntity(
        nickname = nickname,
        type = type,
        profileImageUrl = profileImageUrl,
        tokenId = tokenId
    )
}
