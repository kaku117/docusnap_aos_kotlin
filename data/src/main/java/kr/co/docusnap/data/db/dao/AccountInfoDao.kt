package kr.co.docusnap.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kr.co.docusnap.data.db.entity.AccountInfoEntity

@Dao
interface AccountInfoDao {

    @Query("SELECT * FROM account_info LIMIT 1")
    fun getAccountInfo(): Flow<AccountInfoEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(accountInfo: AccountInfoEntity)

    @Query("DELETE FROM account_info")
    suspend fun deleteAll()
}