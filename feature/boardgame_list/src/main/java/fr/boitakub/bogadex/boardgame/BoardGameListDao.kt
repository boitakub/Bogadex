package fr.boitakub.bogadex.boardgame

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import fr.boitakub.bogadex.boardgame.model.CollectionItem

@Dao
interface BoardGameListDao {

    @Query("SELECT * FROM collection_item")
    suspend fun getAll(): List<CollectionItem>

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
