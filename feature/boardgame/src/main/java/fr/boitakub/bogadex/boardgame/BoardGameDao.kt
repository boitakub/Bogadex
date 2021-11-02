/*
 * Copyright 2021 Boitakub
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
