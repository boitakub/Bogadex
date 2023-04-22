/*
 * Copyright (c) 2023, Boitakub
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
package fr.boitakub.bogadex.boardgame.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import fr.boitakub.bogadex.boardgame.BoardGameCollectionRepository
import fr.boitakub.bogadex.boardgame.model.CollectionType
import fr.boitakub.bogadex.boardgame.usecase.ListCollection
import fr.boitakub.bogadex.boardgame.usecase.ListCollectionFiller
import fr.boitakub.bogadex.boardgame.usecase.ListCollectionItemOwned
import fr.boitakub.bogadex.boardgame.usecase.ListCollectionItemSolo
import fr.boitakub.bogadex.boardgame.usecase.ListCollectionItemWanted
import fr.boitakub.bogadex.common.UserSettings
import fr.boitakub.bogadex.filter.FilterViewModel
import kotlinx.coroutines.flow.Flow

object BoardGameCollectionNavigation {

    const val ROUTE: String = "listBoardgames?collection={collection}&isGridView={isGridView}"

    val ARGUMENTS = listOf(
        navArgument("collection") {
            defaultValue = CollectionType.DEFAULT.key
            type = NavType.StringType
        },
        navArgument("isGridView") {
            defaultValue = false
            type = NavType.BoolType
        },
    )

    fun navigateTo(collection: CollectionType, isGridView: Boolean): String {
        return ROUTE.replace(
            oldValue = "{collection}",
            newValue = collection.key,
        ).replace(
            oldValue = "{isGridView}",
            newValue = isGridView.toString(),
        )
    }

    @Composable
    fun onNavigation(
        navController: NavHostController,
        navBackStackEntry: NavBackStackEntry,
        factory: BoardGameCollectionViewModel.Factory,
        repository: BoardGameCollectionRepository,
        filterViewModel: FilterViewModel = hiltViewModel(),
        userSettingsFlow: Flow<UserSettings>,
    ) {
        val collectionType: CollectionType = CollectionType.from(navBackStackEntry.arguments?.getString("collection"))
        val gridMode: Boolean = navBackStackEntry.arguments?.getBoolean("isGridView") ?: false
        BoardGameCollectionScreen(
            navController = navController,
            activeCollection = collectionType,
            viewModel = factory.create(getCollection(collectionType, repository, filterViewModel, userSettingsFlow)),
            filterViewModel = filterViewModel,
            gridMode = gridMode,
        )
    }

    private fun getCollection(
        collectionType: CollectionType,
        repository: BoardGameCollectionRepository,
        filterViewModel: FilterViewModel,
        userSettingsFlow: Flow<UserSettings>,
    ): ListCollection {
        return when (collectionType) {
            CollectionType.FILLER -> ListCollectionFiller(repository, filterViewModel, userSettingsFlow)
            CollectionType.MY_COLLECTION -> ListCollectionItemOwned(repository, filterViewModel, userSettingsFlow)
            CollectionType.WISHLIST -> ListCollectionItemWanted(repository, filterViewModel, userSettingsFlow)
            CollectionType.SOLO -> ListCollectionItemSolo(repository, filterViewModel, userSettingsFlow)
            CollectionType.ALL -> ListCollection(repository, filterViewModel, userSettingsFlow)
        }
    }
}
