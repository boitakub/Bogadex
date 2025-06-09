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
package fr.boitakub.bogadex.tests.ui

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import fr.boitakub.bogadex.NavigationGraph
import fr.boitakub.bogadex.boardgame.BoardGameCollectionRepository
import fr.boitakub.bogadex.boardgame.model.CollectionType
import fr.boitakub.bogadex.boardgame.ui.BoardGameCollectionNavigation
import fr.boitakub.bogadex.common.ui.theme.BogadexTheme
import fr.boitakub.bogadex.preferences.user.UserSettingsRepository
import fr.boitakub.bogadex.tests.MockedWebResponseDispatcher
import io.mockk.MockKAnnotations
import kotlinx.coroutines.InternalCoroutinesApi
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

@OptIn(ExperimentalTestApi::class)
class DisplayAllUserCollectionInstrumentedTest : KoinTest {

    @get:Rule val composeTestRule = createComposeRule()

    lateinit var mockWebServer: MockWebServer
    val repository: BoardGameCollectionRepository by inject()
    val settingsRepository: UserSettingsRepository by inject()

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    @InternalCoroutinesApi
    fun has_homeList_displayed() {
        mockWebServer.dispatcher = MockedWebResponseDispatcher()
        composeTestRule.setContent {
            val navController = rememberNavController()

            BogadexTheme {
                NavigationGraph(
                    navController = navController,
                    startDestination = BoardGameCollectionNavigation.ROUTE,
                    repository = repository,
                    userSettingsFlow = settingsRepository.userSettings(),
                )
            }
        }

        composeTestRule.waitUntilExactlyOneExists(hasText("7 Wonders Duel"), timeoutMillis = 20000)
        composeTestRule.onNodeWithText("Display all").performClick()
        composeTestRule.waitUntilExactlyOneExists(hasText("7 Wonders Duel"), timeoutMillis = 20000)
        composeTestRule.onNodeWithText("7 Wonders Duel").assertIsDisplayed()
    }

    @Test
    @InternalCoroutinesApi
    fun has_collectionList_displayed() {
        mockWebServer.dispatcher = MockedWebResponseDispatcher()
        composeTestRule.setContent {
            val navController = rememberNavController()

            BogadexTheme {
                NavigationGraph(
                    navController = navController,
                    startDestination = BoardGameCollectionNavigation.ROUTE,
                    repository = repository,
                    userSettingsFlow = settingsRepository.userSettings(),
                )
            }
        }

        composeTestRule.waitUntilExactlyOneExists(hasText("7 Wonders Duel"), timeoutMillis = 20000)
        composeTestRule.onNodeWithText("My collection").performClick()
        composeTestRule.waitUntilExactlyOneExists(hasText("7 Wonders Duel"), timeoutMillis = 20000)
        composeTestRule.onNodeWithText("7 Wonders Duel").assertIsDisplayed()
    }

    @Test
    @InternalCoroutinesApi
    fun has_wishList_displayed() {
        mockWebServer.dispatcher = MockedWebResponseDispatcher()
        composeTestRule.setContent {
            val navController = rememberNavController()

            BogadexTheme {
                NavigationGraph(
                    navController = navController,
                    startDestination = BoardGameCollectionNavigation.ROUTE,
                    repository = repository,
                    userSettingsFlow = settingsRepository.userSettings(),
                )
            }
        }

        composeTestRule.waitUntilExactlyOneExists(hasText("My wishlist"))
        composeTestRule.onNodeWithText("My wishlist").performClick()
        composeTestRule.waitUntilExactlyOneExists(hasText("Anachrony"), timeoutMillis = 20000)
        composeTestRule.onNodeWithText("Anachrony").assertIsDisplayed()
    }

    @Test
    @InternalCoroutinesApi
    fun has_collectionList_displayedOnGrid() {
        mockWebServer.dispatcher = MockedWebResponseDispatcher()
        composeTestRule.setContent {
            val navController = rememberNavController()

            BogadexTheme {
                NavigationGraph(
                    navController = navController,
                    startDestination = BoardGameCollectionNavigation.ROUTE,
                    repository = repository,
                    userSettingsFlow = settingsRepository.userSettings(),
                )
            }

            navController.navigate(
                BoardGameCollectionNavigation.navigateTo(
                    CollectionType.MY_COLLECTION,
                    true,
                ),
            )
        }

        composeTestRule.waitUntilExactlyOneExists(hasText("7 Wonders Duel"), timeoutMillis = 20000)
        composeTestRule.onNodeWithText("7 Wonders Duel").assertIsDisplayed()
    }
}
