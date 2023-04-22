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

import android.app.Activity
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavHostController
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.Configuration
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import fr.boitakub.bogadex.MainActivity
import fr.boitakub.bogadex.boardgame.BoardGameCollectionRepository
import fr.boitakub.bogadex.boardgame.model.CollectionType
import fr.boitakub.bogadex.boardgame.ui.BoardGameCollectionScreen
import fr.boitakub.bogadex.boardgame.usecase.ListCollection
import fr.boitakub.bogadex.boardgame.usecase.ListCollectionFiller
import fr.boitakub.bogadex.boardgame.usecase.ListCollectionItemOwned
import fr.boitakub.bogadex.boardgame.usecase.ListCollectionItemSolo
import fr.boitakub.bogadex.boardgame.usecase.ListCollectionItemWanted
import fr.boitakub.bogadex.common.UserSettings
import fr.boitakub.bogadex.common.ui.theme.BogadexTheme
import fr.boitakub.bogadex.filter.FilterViewModel
import fr.boitakub.bogadex.tests.tools.FileReader.readStringFromFile
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.Flow
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@OptIn(ExperimentalTestApi::class)
class DisplayAllUserCollectionInstrumentedTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val mockWebServer = MockWebServer()

    @Inject
    lateinit var userSettingsFlow: Flow<UserSettings>

    @Inject
    lateinit var repository: BoardGameCollectionRepository

    @MockK(relaxed = true)
    lateinit var navController: NavHostController

    @Before
    fun setUp() {
        hiltRule.inject()
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                var response: MockResponse = MockResponse().setResponseCode(404)
                if (request.path!!.contains("/xmlapi2/collection")) {
                    response = MockResponse()
                        .setResponseCode(200)
                        .setBody(readStringFromFile("Cubenbois.xml"))
                } else if (request.path!!.contains("/xmlapi2/thing")) {
                    response = MockResponse()
                        .setResponseCode(200)
                        .setBody(readStringFromFile("86246.xml"))
                }
                return response
            }
        }
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

    @Test
    fun has_homeList_displayed() {
        val collectionType: CollectionType = CollectionType.MY_COLLECTION
        val filterViewModel = FilterViewModel()
        val gridMode = false
        composeTestRule.activity.setContent {
            BogadexTheme {
                val factory = EntryPointAccessors.fromActivity(
                    LocalContext.current as Activity,
                    MainActivity.ViewModelFactoryProvider::class.java,
                ).boardGameCollectionViewModelFactory()

                BogadexTheme {
                    BoardGameCollectionScreen(
                        navController = navController,
                        activeCollection = collectionType,
                        viewModel = factory.create(
                            getCollection(
                                collectionType,
                                repository,
                                filterViewModel,
                                userSettingsFlow,
                            ),
                        ),
                        filterViewModel = filterViewModel,
                        gridMode = gridMode,
                    )
                }
            }
        }

        composeTestRule.waitUntilExactlyOneExists(hasText("7 Wonders Duel"), timeoutMillis = 20000)
        composeTestRule.onNodeWithText("7 Wonders Duel").assertIsDisplayed()
    }

    @Test
    fun has_collectionList_displayed() {
        val collectionType: CollectionType = CollectionType.MY_COLLECTION
        val filterViewModel = FilterViewModel()
        val gridMode = false
        composeTestRule.activity.setContent {
            BogadexTheme {
                val factory = EntryPointAccessors.fromActivity(
                    LocalContext.current as Activity,
                    MainActivity.ViewModelFactoryProvider::class.java,
                ).boardGameCollectionViewModelFactory()

                BogadexTheme {
                    BoardGameCollectionScreen(
                        navController = navController,
                        activeCollection = collectionType,
                        viewModel = factory.create(
                            getCollection(
                                collectionType,
                                repository,
                                filterViewModel,
                                userSettingsFlow,
                            ),
                        ),
                        filterViewModel = filterViewModel,
                        gridMode = gridMode,
                    )
                }
            }
        }

        composeTestRule.waitUntilExactlyOneExists(hasText("7 Wonders Duel"), timeoutMillis = 20000)
        composeTestRule.onNodeWithText("7 Wonders Duel").assertIsDisplayed()
    }

    @Test
    fun has_wishList_displayed() {
        val collectionType: CollectionType = CollectionType.WISHLIST
        val filterViewModel = FilterViewModel()
        val gridMode = false
        composeTestRule.activity.setContent {
            val factory = EntryPointAccessors.fromActivity(
                LocalContext.current as Activity,
                MainActivity.ViewModelFactoryProvider::class.java,
            ).boardGameCollectionViewModelFactory()

            BogadexTheme {
                BoardGameCollectionScreen(
                    navController = navController,
                    activeCollection = collectionType,
                    viewModel = factory.create(
                        getCollection(
                            collectionType,
                            repository,
                            filterViewModel,
                            userSettingsFlow,
                        ),
                    ),
                    filterViewModel = filterViewModel,
                    gridMode = gridMode,
                )
            }
        }

        composeTestRule.waitUntilExactlyOneExists(hasText("7 Wonders Duel"), timeoutMillis = 20000)
        composeTestRule.onNodeWithText("7 Wonders Duel").assertIsDisplayed()
    }

    @Test
    fun has_collectionList_displayedOnGrid() {
        val collectionType: CollectionType = CollectionType.MY_COLLECTION
        val filterViewModel = FilterViewModel()
        val gridMode = false
        composeTestRule.activity.setContent {
            val factory = EntryPointAccessors.fromActivity(
                LocalContext.current as Activity,
                MainActivity.ViewModelFactoryProvider::class.java,
            ).boardGameCollectionViewModelFactory()

            BogadexTheme {
                BoardGameCollectionScreen(
                    navController = navController,
                    activeCollection = collectionType,
                    viewModel = factory.create(
                        getCollection(
                            collectionType,
                            repository,
                            filterViewModel,
                            userSettingsFlow,
                        ),
                    ),
                    filterViewModel = filterViewModel,
                    gridMode = gridMode,
                )
            }
        }

        composeTestRule.waitUntilExactlyOneExists(hasText("7 Wonders Duel"), timeoutMillis = 20000)
        composeTestRule.onNodeWithText("7 Wonders Duel").assertIsDisplayed()
    }
}
