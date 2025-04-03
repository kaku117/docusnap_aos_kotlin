package kr.co.docusnap.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import kr.co.docusnap.data.db.entity.FavoriteEntity
import kr.co.docusnap.domain.model.FileType

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorite")
    fun getAll(): Flow<List<FavoriteEntity>>

    @Query("SELECT * FROM scan_file WHERE fileType = :type")
    fun getFavoriteFilesByType(type: FileType): Flow<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: FavoriteEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(file: FavoriteEntity)

    @Query("DELETE FROM favorite WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("DELETE FROM favorite")
    suspend fun deleteAll()
}