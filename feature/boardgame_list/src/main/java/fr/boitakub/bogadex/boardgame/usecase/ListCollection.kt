/*
 * Copyright (c) 2021-2023, Boitakub
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
package fr.boitakub.bogadex.boardgame.usecase

import fr.boitakub.architecture.UseCase
import fr.boitakub.bogadex.boardgame.BoardGameCollectionRepository
import fr.boitakub.bogadex.boardgame.model.CollectionItemWithDetails
import fr.boitakub.bogadex.common.UserSettings
import fr.boitakub.bogadex.filter.FilterViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import javax.inject.Inject

open class ListCollection @Inject constructor(
    private val repository: BoardGameCollectionRepository,
    private val filterViewModel: FilterViewModel,
    private val userSettingsFlow: Flow<UserSettings>,
) : UseCase<Flow<List<CollectionItemWithDetails>>, String> {
    override fun apply(): Flow<List<CollectionItemWithDetails>> {
        return userSettingsFlow
            .flatMapConcat { userSettings ->
                repository.get(userSettings.bggUserName)
                    .combine(filterViewModel.filter) { collectionList, filter ->
                        // Apply session filters
                        collectionList.asSequence().filter { item ->
                            item.averageRating() in filter.ratingFilter.second.minValue..filter.ratingFilter.second.maxValue
                        }.filter { item ->
                            item.averageWeight() in filter.weightFilter.second.minValue..filter.weightFilter.second.maxValue
                        }.filter { item ->
                            item.averageDuration() in filter.durationFilter.second.minValue..filter.durationFilter.second.maxValue
                        }.filter {
                            // Apply global app filters
                            if (!userSettings.displayPreviouslyOwned) {
                                !it.item.status.previouslyOwned
                            } else {
                                true
                            }
                        }.filter { item ->
                            if (filter.searchTerms.isNotBlank()) {
                                item.item.title?.contains(filter.searchTerms.lowercase()) == true || item.details?.title?.contains(
                                    filter.searchTerms.lowercase(),
                                ) == true
                            } else {
                                true
                            }
                        }.toList()
                        // Apply grouping
                    }
            }
    }
}
