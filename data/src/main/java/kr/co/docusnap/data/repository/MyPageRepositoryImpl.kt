package kr.co.docusnap.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kr.co.docusnap.data.db.dao.AccountInfoDao
import kr.co.docusnap.data.db.dao.FavoriteDao
import kr.co.docusnap.data.db.dao.ScanFileDao
import kr.co.docusnap.data.db.entity.toDomainModel
import kr.co.docusnap.data.db.entity.toEntity
import kr.co.docusnap.domain.model.AccountInfo
import kr.co.docusnap.domain.repository.MyPageRepository
import javax.inject.Inject

class MyPageRepositoryImpl @Inject constructor(
    private val accountInfoDao: AccountInfoDao
): MyPageRepository {

    override fun getAccountInfo(): Flow<AccountInfo?> {
        return accountInfoDao.getAccountInfo().map {
            it?.toDomainModel()
        }
    }

    override suspend fun signIn(accountInfo: AccountInfo) {
        accountInfoDao.deleteAll()

        accountInfoDao.insert(accountInfo.toEntity())
    }

    override suspend fun signOut() {
        accountInfoDao.deleteAll()
    }
}