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

import android.content.Context
import com.tickaroo.tikxml.TikXml
import dagger.hilt.android.qualifiers.ApplicationContext
import fr.boitakub.architecture.Repository
import fr.boitakub.bgg.client.BggService
import fr.boitakub.bgg.client.UserCollection
import fr.boitakub.bogadex.boardgame.mapper.CollectionMapper
import fr.boitakub.bogadex.boardgame.model.BoardGame
import fr.boitakub.bogadex.boardgame.model.CollectionItem
import fr.boitakub.bogadex.boardgame.model.CollectionItemWithDetails
import fr.boitakub.bogadex.boardgame.usecase.RefreshGameDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import okio.buffer
import okio.sink
import java.io.File
import javax.inject.Inject

class BoardGameCollectionRepository @Inject constructor(
    @ApplicationContext val context: Context,
    override val local: BoardGameListDao,
    override val remote: BggService,
    private val mapper: CollectionMapper,
    private val refreshGameDetails: RefreshGameDetails
) : Repository {

    fun get(user: String): Flow<List<CollectionItemWithDetails>> =
        local.getCollectionWithDetails().combine(getAllRemoteResorts(user)) { local, remote ->
            toUiModel(local, remote)
        }

    private fun getAllRemoteResorts(user: String): Flow<List<CollectionItem>> = flow {
        emit(emptyList())
        val networkResult = remote.userCollection(user)
        val result: List<CollectionItem> = mapper.map(networkResult).boardgames
        emit(result)
        local.updateCollection(result)
        writeMockData(user, networkResult)
    }

    private fun toUiModel(
        localList: List<CollectionItemWithDetails>,
        remoteList: List<CollectionItem>
    ):
        List<CollectionItemWithDetails> {
        return if (localList.isEmpty()) {
            remoteList.map {
                CollectionItemWithDetails(it, BoardGame())
            }
        } else {
            localList.forEach { refreshGameDetails.apply(it) }
            localList
        }
    }

    private fun writeMockData(input: String?, result: UserCollection?) {
        val parser: TikXml = TikXml.Builder()
            .build()
        val file = File(context.filesDir?.path + "/" + File.separator + input + ".xml")
        file.sink().buffer().use { sink ->
            parser.write(sink, result)
        }
    }
}
