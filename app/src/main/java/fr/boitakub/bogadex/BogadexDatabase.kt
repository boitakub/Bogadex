package fr.boitakub.bogadex

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import fr.boitakub.bogadex.boardgame.BoardGameDao
import fr.boitakub.bogadex.boardgame.BoardGameListDao
import fr.boitakub.bogadex.boardgame.model.BoardGame
import fr.boitakub.bogadex.boardgame.model.CollectionItem

@Database(entities = [BoardGame::class, CollectionItem::class], version = 1, exportSchema = false)
@TypeConverters(DatabaseExtensions::class)
abstract class BogadexDatabase : RoomDatabase() {
    abstract fun boardGameDao(): BoardGameDao
    abstract fun boardGameListDao(): BoardGameListDao

    companion object {
        internal const val DB_NAME = "bogadex_database.db"

        @Volatile
        private var instance: BogadexDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            BogadexDatabase::class.java,
            DB_NAME
        ).build()
    }
}
