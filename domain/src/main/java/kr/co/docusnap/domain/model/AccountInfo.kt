package kr.co.docusnap.domain.model

data class AccountInfo(
    val nickname: String,
    val type: Type,
    val profileImageUrl: String,
    val tokenId: String
) {
    enum class Type {
        GOOGLE, KAKAO, NAVER
    }
}