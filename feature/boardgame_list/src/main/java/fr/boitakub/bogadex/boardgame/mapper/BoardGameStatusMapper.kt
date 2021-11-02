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
import fr.boitakub.bgg.client.UserBoardGameStatus
import fr.boitakub.bogadex.boardgame.model.CollectionStatus
import fr.boitakub.bogadex.boardgame.model.Wished
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BoardGameStatusMapper @Inject constructor() : Mapper<CollectionStatus, UserBoardGameStatus> {

    override fun map(source: UserBoardGameStatus): CollectionStatus {
        return CollectionStatus(
            source.own.toBoolean(),
            source.preordered.toBoolean(),
            source.prevowned.toBoolean(),
            source.fortrade.toBoolean(),
            Wished.from(source.wishlistpriority) ?: Wished.isWished(source.wishlist)
        )
    }

    private fun Int.toBoolean() = this >= 1
}
