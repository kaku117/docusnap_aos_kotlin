package kr.co.docusnap.app.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.co.docusnap.data.repository.FavoriteRepositoryImpl
import kr.co.docusnap.data.repository.HomeRepositoryImpl
import kr.co.docusnap.data.repository.MyPageRepositoryImpl
import kr.co.docusnap.domain.repository.FavoriteRepository
import kr.co.docusnap.domain.repository.HomeRepository
import kr.co.docusnap.domain.repository.MyPageRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindHomeRepository(homeRepositoryImpl: HomeRepositoryImpl): HomeRepository

    @Singleton
    @Binds
    fun bindFavoriteRepository(favoriteRepositoryImpl: FavoriteRepositoryImpl): FavoriteRepository

    @Singleton
    @Binds
    fun bindMyPageRepository(myPageRepositoryImpl: MyPageRepositoryImpl): MyPageRepository
}