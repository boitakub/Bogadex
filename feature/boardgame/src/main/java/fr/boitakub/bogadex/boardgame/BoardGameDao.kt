package fr.boitakub.bogadex.boardgame

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fr.boitakub.bogadex.boardgame.model.BoardGame
import kotlinx.coroutines.flow.Flow

@Dao
interface BoardGameDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllBoardGame(boardGames: List<BoardGame>)

    @Query("SELECT * FROM boardgame")
    fun getBoardGameWithStatistics(): Flow<List<BoardGame>>

    @Query("SELECT * FROM boardgame WHERE bgg_id LIKE :bggId")
    fun getBoardGameWithId(bggId: String): BoardGame
}
