/*
 * Copyright (c) 2023-2025, Boitakub
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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import fr.boitakub.boardgame_list.R
import fr.boitakub.bogadex.boardgame.model.CollectionType
import fr.boitakub.bogadex.boardgame.ui.component.CollectionTabBar
import fr.boitakub.bogadex.boardgame.ui.component.SearchInputField
import fr.boitakub.bogadex.common.ui.CommonComposable.DefaultScreenUI
import fr.boitakub.bogadex.filter.FilterLayout
import fr.boitakub.bogadex.filter.FilterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardGameCollectionScreen(
    navController: NavController,
    viewModel: BoardGameCollectionViewModel,
    activeCollection: CollectionType,
    filterViewModel: FilterViewModel,
    gridMode: Boolean = false,
) {
    val activeCollection = rememberSaveable { mutableStateOf(activeCollection) }
    val searchedTerms = rememberSaveable { mutableStateOf(filterViewModel.filter.value.searchTerms) }
    val filterState by filterViewModel.get().collectAsStateWithLifecycle()
    val isGridView = rememberSaveable { mutableStateOf(false) }

    var openBottomSheet = rememberSaveable { mutableStateOf(false) }
    var skipPartiallyExpanded = remember { mutableStateOf(false) }

    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded.value,
    )

    DefaultScreenUI(
        topBar = {
            CollectionScreenTopBar(
                activeCollection,
                navController,
                isGridView,
                searchedTerms,
                filterViewModel,
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { openBottomSheet.value = !openBottomSheet.value },
                shape = RoundedCornerShape(16.dp),
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_filter_solid),
                    contentDescription = "Add FAB",
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = stringResource(id = fr.boitakub.bogadex.common.R.string.filters),
                )
            }
        },
        errors = viewModel.errors,
    ) { padding ->
        BoardGameListScreen(
            modifier = Modifier.padding(padding),
            navController = navController,
            viewModel = viewModel,
            gridMode = gridMode,
        )
    }

    // Sheet content
    if (openBottomSheet.value) {
        ModalBottomSheet(
            onDismissRequest = { openBottomSheet.value = false },
            sheetState = bottomSheetState,
        ) {
            FilterLayout(
                filterState = filterState,
                updateFilter = {
                    filterViewModel.mutate(it.copy(searchTerms = filterViewModel.filter.value.searchTerms))
                },
            )
        }
    }
}

@Composable
private fun CollectionScreenTopBar(
    activeCollection: MutableState<CollectionType>,
    navController: NavController,
    isGridView: MutableState<Boolean>,
    searchedTerms: MutableState<String>,
    filterViewModel: FilterViewModel,
) = TopBar(
    collectionAvailableList = CollectionType.values().toList(),
    activeCollection = activeCollection.value,
    onCollectionChanged = {
        activeCollection.value = it
        navController.navigate(
            BoardGameCollectionNavigation.navigateTo(
                it,
                isGridView.value,
            ),
        )
    },
    currentTerm = searchedTerms.value,
    onSearchTermChange = {
        filterViewModel.mutate(
            filterViewModel.filter.value.copy(
                searchTerms = Regex("[^A-Za-z0-9]").replace(
                    it,
                    "",
                ),
            ),
        )
    },
    onToggleViewClick = {
        isGridView.value = !isGridView.value
        navController.navigate(
            BoardGameCollectionNavigation.navigateTo(
                activeCollection.value,
                isGridView.value,
            ),
        )
    },
    onSettingButtonClick = {
        navController.navigate("preferences")
    },
)

@Composable
private fun TopBar(
    collectionAvailableList: List<CollectionType>,
    activeCollection: CollectionType,
    onCollectionChanged: (selectedCollection: CollectionType) -> Unit,
    currentTerm: String,
    onSearchTermChange: (String) -> Unit,
    onToggleViewClick: () -> Unit,
    onSettingButtonClick: () -> Unit,
) {
    Column(
        modifier = Modifier.wrapContentSize(),
    ) {
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SearchInputField(
                modifier = Modifier.weight(1f),
                currentTerm = currentTerm,
                onSearchTermChange = onSearchTermChange,
            )
            IconButton(onClick = {
                onToggleViewClick()
            }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_list),
                    contentDescription = stringResource(id = fr.boitakub.bogadex.common.R.string.change_display),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp),
                )
            }
            IconButton(onClick = {
                onSettingButtonClick()
            }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_gear),
                    contentDescription = stringResource(id = fr.boitakub.bogadex.common.R.string.settings),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp),
                )
            }
        }

        CollectionTabBar(
            collectionAvailableList = collectionAvailableList,
            activeCollection = activeCollection,
            onCollectionChanged = onCollectionChanged,
        )
    }
}
