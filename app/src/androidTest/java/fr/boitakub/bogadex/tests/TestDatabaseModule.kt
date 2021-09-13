package fr.boitakub.bogadex.tests

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import fr.boitakub.bogadex.BogadexDatabase
import fr.boitakub.bogadex.di.DatabaseModule
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [DatabaseModule::class])
class TestDatabaseModule {

    @Singleton
    @Provides
    fun provideInMemoryDbEmpty(@ApplicationContext context: Context?): BogadexDatabase {
        return Room.inMemoryDatabaseBuilder(context!!, BogadexDatabase::class.java).build()
    }
}
