package kr.co.docusnap.domain.repository

import kotlinx.coroutines.flow.Flow
import kr.co.docusnap.domain.model.AccountInfo

interface MyPageRepository {

    fun getAccountInfo(): Flow<AccountInfo?>

    suspend fun signIn(accountInfo: AccountInfo)

    suspend fun signOut()
}