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
package fr.boitakub.bogadex.boardgame.usecase

import fr.boitakub.architecture.UseCase
import fr.boitakub.bogadex.boardgame.BoardGameCollectionRepository
import fr.boitakub.bogadex.boardgame.model.CollectionItemWithDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ListCollectionItemOwned @Inject constructor(
    private val repository: BoardGameCollectionRepository,
) : UseCase<Flow<List<CollectionItemWithDetails>>, String>, ListCollection(repository) {
    override fun apply(input: String): Flow<List<CollectionItemWithDetails>> {
        return super.apply(input).map { it.filter { boardGame -> boardGame.item.status.own } }
    }
}
