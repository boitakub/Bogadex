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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import fr.boitakub.bogadex.boardgame.model.BoardGame
import kotlinx.coroutines.flow.catch
import org.koin.compose.viewmodel.koinViewModel

object BoardGameDetailNavigation {

    const val ROUTE: String = "detail?boardGameId={boardGameId}"

    val ARGUMENTS = listOf(
        navArgument("boardGameId") {
            defaultValue = ""
            type = NavType.StringType
        },
    )

    fun navigateTo(boardGameId: String): String = ROUTE.replace(
        oldValue = "{boardGameId}",
        newValue = boardGameId,
    )

    @Composable
    fun onNavigation(navController: NavHostController, navBackStackEntry: NavBackStackEntry) {
        val boardGameDetailViewModel: BoardGameDetailViewModel = koinViewModel()
        val boardGameId: String? = navBackStackEntry.arguments?.getString("boardGameId")

        val boardGame = remember { mutableStateOf<BoardGame?>(BoardGame()) }

        LaunchedEffect(Unit) {
            boardGameId?.let {
                boardGameDetailViewModel
                    .load(it)
                    .catch { error ->
                        boardGameDetailViewModel.addError(error)
                    }
                    .collect { result ->
                        boardGame.value = result
                    }
            }
        }

        BoardGameDetailScreen(
            navController = navController,
            boardGame = boardGame.value,
            errors = boardGameDetailViewModel.errors,
        )
    }
}
