/*
 * Copyright (c) 2025, Boitakub
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
package fr.boitakub.bogadex.di

import fr.boitakub.bogadex.UpdateExistingBoardGameWork
import fr.boitakub.bogadex.UpsetMissingBoardGameWork
import fr.boitakub.bogadex.boardgame.BoardGameCollectionRepository
import fr.boitakub.bogadex.boardgame.BoardGameRepository
import fr.boitakub.bogadex.boardgame.mapper.BoardGameMapper
import fr.boitakub.bogadex.boardgame.mapper.BoardGameStatusMapper
import fr.boitakub.bogadex.boardgame.mapper.CollectionMapper
import fr.boitakub.bogadex.boardgame.usecase.ListCollection
import fr.boitakub.bogadex.boardgame.usecase.ListCollectionFiller
import fr.boitakub.bogadex.boardgame.usecase.ListCollectionItemDuo
import fr.boitakub.bogadex.boardgame.usecase.ListCollectionItemOwned
import fr.boitakub.bogadex.boardgame.usecase.ListCollectionItemPreorder
import fr.boitakub.bogadex.boardgame.usecase.ListCollectionItemSolo
import fr.boitakub.bogadex.boardgame.usecase.ListCollectionItemWanted
import fr.boitakub.bogadex.boardgame.work.RetrieveMissingBoardGameWork
import fr.boitakub.bogadex.common.UserSettings
import fr.boitakub.bogadex.preferences.user.UserSettingsRepository
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.dsl.module

val applicationModule = module {

    workerOf(::RetrieveMissingBoardGameWork)
    workerOf(::UpdateExistingBoardGameWork)
    workerOf(::UpsetMissingBoardGameWork)
    single { CollectionMapper(get()) }
    single { BoardGameStatusMapper() }
    single { BoardGameMapper() }
    single { BoardGameCollectionRepository(get(), get(), get(), get()) }
    single { BoardGameRepository(get(), get()) }

    //region Use Case
    single<Flow<UserSettings>> { (get() as UserSettingsRepository).userSettings() }
    single { ListCollection(get(), get(), get()) }
    single { ListCollectionFiller(get(), get(), get()) }
    single { ListCollectionItemDuo(get(), get(), get()) }
    single { ListCollectionItemOwned(get(), get(), get()) }
    single { ListCollectionItemPreorder(get(), get(), get()) }
    single { ListCollectionItemSolo(get(), get(), get()) }
    single { ListCollectionItemWanted(get(), get(), get()) }
    //endregion
}
