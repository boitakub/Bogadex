/*
 * Copyright (c) 2021-2025, Boitakub
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of mosquitto nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package fr.boitakub.bogadex.boardgame

import android.content.Context
import fr.boitakub.architecture.Repository
import fr.boitakub.bgg.client.BggService
import fr.boitakub.bgg.client.UserCollection
import fr.boitakub.bogadex.boardgame.mapper.CollectionMapper
import fr.boitakub.bogadex.boardgame.model.BoardGame
import fr.boitakub.bogadex.boardgame.model.CollectionItem
import fr.boitakub.bogadex.boardgame.model.CollectionItemWithDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow

class BoardGameCollectionRepository(
    val context: Context,
    override val local: BoardGameListDao,
    override val remote: BggService,
    private val mapper: CollectionMapper,
) : Repository {
    fun get(user: String): Flow<List<CollectionItemWithDetails>> =
        local.collectionWithDetailsFlow().combine(getAllRemoteResorts(user)) { local, remote ->
            toUiModel(local, remote)
        }

    suspend fun getRemote(user: String): UserCollection = remote.userCollection(user)

    private fun getAllRemoteResorts(user: String): Flow<List<CollectionItem>> = flow {
        emit(emptyList())
        val networkResult = remote.userCollection(user)
        val result: List<CollectionItem> = mapper.map(networkResult).boardgames
        emit(result)
        local.updateCollection(result)
    }

    private fun toUiModel(
        localList: List<CollectionItemWithDetails>,
        remoteList: List<CollectionItem>,
    ): List<CollectionItemWithDetails> = localList.ifEmpty {
        remoteList.map {
            CollectionItemWithDetails(it, BoardGame())
        }
    }
}
