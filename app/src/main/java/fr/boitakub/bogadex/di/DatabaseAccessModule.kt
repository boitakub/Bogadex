package fr.boitakub.bogadex.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.boitakub.bogadex.BogadexDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseAccessModule {

    @Singleton
    @Provides
    fun providesBoardGameDao(bogadexDatabase: BogadexDatabase) = bogadexDatabase.boardGameDao()

    @Singleton
    @Provides
    fun providesBoardGameListDao(bogadexDatabase: BogadexDatabase) =
        bogadexDatabase.boardGameListDao()
}
