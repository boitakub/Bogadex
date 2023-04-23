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
package fr.boitakub.bogadex.tests.boardgame_list

import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.Configuration
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import fr.boitakub.bogadex.MainActivity
import fr.boitakub.bogadex.NavigationGraph
import fr.boitakub.bogadex.boardgame.BoardGameCollectionRepository
import fr.boitakub.bogadex.boardgame.model.CollectionType
import fr.boitakub.bogadex.boardgame.ui.BoardGameCollectionNavigation
import fr.boitakub.bogadex.common.UserSettings
import fr.boitakub.bogadex.common.ui.theme.BogadexTheme
import fr.boitakub.bogadex.tests.MockedWebResponseDispatcher
import io.mockk.MockKAnnotations
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@OptIn(ExperimentalTestApi::class)
class DisplayAllUserCollectionInstrumentedTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    val mockWebServer by lazy { MockWebServer() }

    @Inject
    lateinit var userSettingsFlow: Flow<UserSettings>

    @Inject
    lateinit var repository: BoardGameCollectionRepository

    @Before
    fun setUp() {
        hiltRule.inject()
        mockWebServer.start(8080)

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val config = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setExecutor(SynchronousExecutor())
            .build()

        // Initialize WorkManager for instrumentation tests.
        WorkManagerTestInitHelper.initializeTestWorkManager(context, config)

        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    @InternalCoroutinesApi
    fun has_homeList_displayed() {
        mockWebServer.dispatcher = MockedWebResponseDispatcher()
        composeTestRule.activity.setContent {
            val navController = rememberNavController()

            BogadexTheme(false) {
                NavigationGraph(
                    navController = navController,
                    startDestination = BoardGameCollectionNavigation.ROUTE,
                    repository = repository,
                    userSettingsFlow = userSettingsFlow,
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
        composeTestRule.activity.setContent {
            val navController = rememberNavController()

            BogadexTheme(false) {
                NavigationGraph(
                    navController = navController,
                    startDestination = BoardGameCollectionNavigation.ROUTE,
                    repository = repository,
                    userSettingsFlow = userSettingsFlow,
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
        composeTestRule.activity.setContent {
            val navController = rememberNavController()

            BogadexTheme(false) {
                NavigationGraph(
                    navController = navController,
                    startDestination = BoardGameCollectionNavigation.ROUTE,
                    repository = repository,
                    userSettingsFlow = userSettingsFlow,
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
        composeTestRule.activity.setContent {
            val navController = rememberNavController()

            BogadexTheme(false) {
                NavigationGraph(
                    navController = navController,
                    startDestination = BoardGameCollectionNavigation.ROUTE,
                    repository = repository,
                    userSettingsFlow = userSettingsFlow,
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
