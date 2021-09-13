package fr.boitakub.bogadex.boardgame

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import fr.boitakub.bogadex.boardgame.model.CollectionItem
import fr.boitakub.bogadex.boardgame.model.CollectionItemWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface BoardGameListDao {

    @Query("SELECT * FROM collection_item")
    fun getAll(): Flow<List<CollectionItem>>

    @Transaction
    @Query("SELECT * FROM collection_item")
    fun getCollectionWithDetails(): Flow<List<CollectionItemWithDetails>>

    @Transaction
    suspend fun updateCollection(boardGames: List<CollectionItem>) {
        deleteCollectionItemTable()
        insertAllCollectionItem(boardGames)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCollectionItem(boardGames: List<CollectionItem>)

    @Query("DELETE FROM collection_item")
    fun deleteCollectionItemTable()

    @Delete
    suspend fun delete(boardGame: CollectionItem)
}
