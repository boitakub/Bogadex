/*
 * Copyright (c) 2021-2022, Boitakub
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
package fr.boitakub.bogadex

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ActivityComponent
import fr.boitakub.bogadex.boardgame.BoardGameCollectionRepository
import fr.boitakub.bogadex.boardgame.ui.BoardGameCollectionNavigation
import fr.boitakub.bogadex.boardgame.ui.BoardGameCollectionViewModel
import fr.boitakub.bogadex.boardgame.ui.BoardGameDetailNavigation
import fr.boitakub.bogadex.common.UserSettings
import fr.boitakub.bogadex.common.ui.theme.BogadexTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var repository: BoardGameCollectionRepository

    @Inject
    lateinit var userSettings: UserSettings

    @EntryPoint
    @InstallIn(ActivityComponent::class)
    interface ViewModelFactoryProvider {
        fun boardGameCollectionViewModelFactory(): BoardGameCollectionViewModel.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navHostController = rememberNavController()

            BogadexTheme {
                NavigationGraph(
                    navController = navHostController,
                    startDestination = BoardGameCollectionNavigation.ROUTE,
                    repository = repository,
                    userSettings = userSettings,
                )
            }
        }
    }
}

@Composable
fun NavigationGraph(
    navController: NavHostController,
    startDestination: String,
    repository: BoardGameCollectionRepository,
    userSettings: UserSettings,
) {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        MainActivity.ViewModelFactoryProvider::class.java,
    ).boardGameCollectionViewModelFactory()

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(
            route = BoardGameCollectionNavigation.ROUTE,
            arguments = BoardGameCollectionNavigation.ARGUMENTS,
            content = {
                BoardGameCollectionNavigation.onNavigation(
                    navController = navController,
                    navBackStackEntry = it,
                    factory = factory,
                    repository = repository,
                    userSettings = userSettings,
                )
            },
        )
        composable(
            route = BoardGameDetailNavigation.ROUTE,
            arguments = BoardGameDetailNavigation.ARGUMENTS,
            content = {
                BoardGameDetailNavigation.onNavigation(
                    navController = navController,
                    navBackStackEntry = it,
                )
            },
        )
    }
}
