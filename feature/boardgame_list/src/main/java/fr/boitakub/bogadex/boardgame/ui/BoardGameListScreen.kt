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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fr.boitakub.bogadex.boardgame.model.CollectionItemWithDetails
import fr.boitakub.bogadex.boardgame.ui.component.BoardGameGridItem
import fr.boitakub.bogadex.boardgame.ui.component.BoardGameListItem
import kotlinx.coroutines.flow.Flow

@Composable
fun BoardGameListScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    gameList: Flow<List<CollectionItemWithDetails>>,
    gridMode: Boolean = false,
) {
    val viewState = remember { mutableStateOf(BoardGameListState("0", listOf())) }

    LaunchedEffect(Unit) {
        gameList.collect {
            viewState.value = viewState.value.copy(list = it)
        }
    }

    if (gridMode) {
        BoardGameListGrid(
            modifier = modifier,
            games = viewState.value.list,
            onClick = {
                openGameDetails(navController, it)
            },
        )
    } else {
        BoardGameList(
            modifier = modifier,
            games = viewState.value.list,
            onClick = {
                openGameDetails(navController, it)
            },
        )
    }
}

private fun openGameDetails(navController: NavController, item: CollectionItemWithDetails) {
    navController.navigate(
        BoardGameDetailNavigation.navigateTo(
            item.item.bggId,
        ),
    )
}

@Composable
fun BoardGameListGrid(
    modifier: Modifier,
    games: List<CollectionItemWithDetails>,
    onClick: (CollectionItemWithDetails) -> Unit,
    columns: Int = 2,
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(columns),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp),
        content = {
            items(games.size) { index ->
                BoardGameGridItem(games[index], onClick)
            }
        },
    )
}

@Composable
fun BoardGameList(
    modifier: Modifier,
    games: List<CollectionItemWithDetails>,
    onClick: (CollectionItemWithDetails) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        content = {
            items(games.size) { index ->
                BoardGameListItem(games[index], onClick)
            }
        },
    )
}
