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
package fr.boitakub.bogadex.boardgame.mapper

import fr.boitakub.architecture.Mapper
import fr.boitakub.bgg.client.UserBoardGame
import fr.boitakub.bgg.client.UserCollection
import fr.boitakub.bogadex.boardgame.model.CollectionItem
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CollectionMapper @Inject constructor(private val statusMapper: BoardGameStatusMapper) :
    Mapper<fr.boitakub.bogadex.boardgame.model.Collection, UserCollection> {

    override fun map(source: UserCollection): fr.boitakub.bogadex.boardgame.model.Collection {
        return fr.boitakub.bogadex.boardgame.model.Collection(map(source.games))
    }

    private fun map(entity: UserBoardGame): CollectionItem =
        CollectionItem(
            entity.objectid,
            entity.name,
            entity.yearpublished,
            entity.thumbnail,
            Date(),
            statusMapper.map(entity.status)
        )

    private fun map(list: List<UserBoardGame>): List<CollectionItem> = list.map { map(it) }
}
