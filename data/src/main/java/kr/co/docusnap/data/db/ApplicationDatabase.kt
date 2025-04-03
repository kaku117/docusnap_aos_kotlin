package kr.co.docusnap.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kr.co.docusnap.data.db.converter.ZonedDateTimeConverters
import kr.co.docusnap.data.db.dao.AccountInfoDao
import kr.co.docusnap.data.db.dao.FavoriteDao
import kr.co.docusnap.data.db.dao.ScanFileDao
import kr.co.docusnap.data.db.entity.AccountInfoEntity
import kr.co.docusnap.data.db.entity.FavoriteEntity
import kr.co.docusnap.data.db.entity.ScanFileEntity

@Database(
    entities = [
        ScanFileEntity::class,
        FavoriteEntity::class,
        AccountInfoEntity::class
    ],
    version = 1
)
@TypeConverters(ZonedDateTimeConverters::class)
abstract class ApplicationDatabase: RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "applicationDatabase.db"
    }

    abstract fun scanFileDao(): ScanFileDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun accountInfoDao(): AccountInfoDao
}