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
