package kr.co.docusnap.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import kr.co.docusnap.data.db.entity.ScanFileEntity
import kr.co.docusnap.domain.model.FileType
import kr.co.docusnap.domain.model.ScanFile

@Dao
interface ScanFileDao {

//    suspend fun favoriteFile(scanFile: ScanFile)

//    suspend fun removeFile(scanFile: ScanFile)

    @Query("SELECT * FROM scan_file ORDER BY id DESC")
    fun getAll(): Flow<List<ScanFileEntity>>

    @Query("SELECT * FROM scan_file WHERE fileType = :type")
    fun getScanFilesByType(type: FileType): Flow<List<ScanFileEntity>>

    @Query("SELECT * FROM scan_file WHERE id = :id")
    fun getScanFileById(id: Int): Flow<ScanFileEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScanFiles(files: List<ScanFileEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateScanFile(file: ScanFileEntity)

    @Query("DELETE FROM scan_file")
    suspend fun removeAll()
}