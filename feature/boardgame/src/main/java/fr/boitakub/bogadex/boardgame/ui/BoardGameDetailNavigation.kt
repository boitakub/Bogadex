package fr.boitakub.bogadex.boardgame.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import fr.boitakub.bogadex.boardgame.model.BoardGame

object BoardGameDetailNavigation {

    const val ROUTE: String = "detail?boardGameId={boardGameId}"

    val ARGUMENTS = listOf(
        navArgument("boardGameId") {
            defaultValue = ""
            type = NavType.StringType
        },
    )

    fun navigateTo(boardGameId: String): String {
        return ROUTE.replace(
            oldValue = "{boardGameId}",
            newValue = boardGameId,
        )
    }

    @Composable
    fun onNavigation(navController: NavHostController, navBackStackEntry: NavBackStackEntry) {
        val boardGameDetailViewModel: BoardGameDetailViewModel = hiltViewModel()
        val boardGameId: String? = navBackStackEntry.arguments?.getString("boardGameId")

        val boardGame = remember { mutableStateOf<BoardGame?>(BoardGame()) }

        LaunchedEffect(Unit) {
            boardGameId?.let {
                boardGameDetailViewModel
                    .load(it)
                    .collect { result ->
                        boardGame.value = result
                    }
            }
        }

        BoardGameDetailScreen(
            boardGame = boardGame.value,
        )
    }
}
