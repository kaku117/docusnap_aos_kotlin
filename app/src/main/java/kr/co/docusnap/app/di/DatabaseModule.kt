package kr.co.docusnap.app.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kr.co.docusnap.data.db.ApplicationDatabase
import kr.co.docusnap.data.db.dao.AccountInfoDao
import kr.co.docusnap.data.db.dao.FavoriteDao
import kr.co.docusnap.data.db.dao.ScanFileDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext
        context: Context
    ): ApplicationDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ApplicationDatabase::class.java,
            ApplicationDatabase.DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideScanFileDao(database: ApplicationDatabase): ScanFileDao {
        return database.scanFileDao()
    }

    @Singleton
    @Provides
    fun provideFavoriteDao(database: ApplicationDatabase): FavoriteDao {
        return database.favoriteDao()
    }

    @Singleton
    @Provides
    fun provideAccountInfoDao(database: ApplicationDatabase): AccountInfoDao {
        return database.accountInfoDao()
    }
}