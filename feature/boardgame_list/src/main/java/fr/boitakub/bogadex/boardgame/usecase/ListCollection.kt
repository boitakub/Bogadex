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
package fr.boitakub.bogadex.boardgame.usecase

import fr.boitakub.architecture.UseCase
import fr.boitakub.bogadex.boardgame.BoardGameCollectionRepository
import fr.boitakub.bogadex.boardgame.model.CollectionItemWithDetails
import fr.boitakub.bogadex.common.UserSettings
import fr.boitakub.bogadex.filter.FilterState
import fr.boitakub.bogadex.filter.FilterViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class)
open class ListCollection(
    private val repository: BoardGameCollectionRepository,
    private val filterViewModel: FilterViewModel,
    private val userSettingsFlow: Flow<UserSettings>,
) : UseCase<Flow<List<CollectionItemWithDetails>>, String> {
    override fun apply(): Flow<List<CollectionItemWithDetails>> = userSettingsFlow
        .flatMapLatest { userSettings ->
            repository.get(userSettings.bggUserName)
                .combine(filterViewModel.filter) { collectionList, filter ->
                    // Apply session filters
                    collectionList.asSequence().filter { item ->
                        ratingFilter(item, filter)
                    }.filter { item ->
                        weightFilter(item, filter)
                    }.filter { item ->
                        durationFilter(item, filter)
                    }.filter {
                        // Apply global app filters
                        if (!userSettings.displayPreviouslyOwned) {
                            !it.item.status.previouslyOwned
                        } else {
                            true
                        }
                    }.filter { item ->
                        searchTermFilter(filter, item)
                    }.toList()
                    // Apply grouping
                }
        }

    private fun searchTermFilter(filter: FilterState, item: CollectionItemWithDetails) =
        if (filter.searchTerms.isNotBlank()) {
            item.item.title?.contains(filter.searchTerms.lowercase()) == true ||
                item.details?.title?.contains(
                    filter.searchTerms.lowercase(),
                ) == true
        } else {
            true
        }

    private fun ratingFilter(item: CollectionItemWithDetails, filter: FilterState): Boolean {
        if (filter.ratingFilter.second.minValue == filter.ratingFilter.first.minValueRange &&
            filter.ratingFilter.second.maxValue == filter.ratingFilter.first.maxValueRange
        ) {
            return true
        }
        return item.averageRating() in filter.ratingFilter.second.minValue..filter.ratingFilter.second.maxValue
    }

    private fun weightFilter(item: CollectionItemWithDetails, filter: FilterState): Boolean {
        if (filter.weightFilter.second.minValue == filter.weightFilter.first.minValueRange &&
            filter.weightFilter.second.maxValue == filter.weightFilter.first.maxValueRange
        ) {
            return true
        }
        return item.averageWeight() in filter.weightFilter.second.minValue..filter.weightFilter.second.maxValue
    }

    private fun durationFilter(item: CollectionItemWithDetails, filter: FilterState): Boolean {
        if (filter.durationFilter.second.minValue == filter.durationFilter.first.minValueRange &&
            filter.durationFilter.second.maxValue == filter.durationFilter.first.maxValueRange
        ) {
            return true
        }
        return item.averageDuration() in filter.durationFilter.second.minValue..filter.durationFilter.second.maxValue
    }
}
