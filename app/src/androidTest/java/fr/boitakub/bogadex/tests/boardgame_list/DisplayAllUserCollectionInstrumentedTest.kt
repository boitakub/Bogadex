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
package fr.boitakub.bogadex.tests.boardgame_list

import android.util.Log
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavHostController
import androidx.test.espresso.IdlingRegistry
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.Configuration
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import fr.boitakub.bogadex.NavigationGraph
import fr.boitakub.bogadex.boardgame.BoardGameCollectionRepository
import fr.boitakub.bogadex.boardgame.BoardGameDao
import fr.boitakub.bogadex.boardgame.ui.BoardGameCollectionNavigation
import fr.boitakub.bogadex.common.UserSettings
import fr.boitakub.bogadex.common.ui.theme.BogadexTheme
import fr.boitakub.bogadex.tests.OkHttp3IdlingResource
import fr.boitakub.bogadex.tests.tools.FileReader.readStringFromFile
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flow
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidTest
class DisplayAllUserCollectionInstrumentedTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockWebServer = MockWebServer()

    @Inject
    lateinit var okHttp3IdlingResource: OkHttp3IdlingResource

    @Inject
    lateinit var boardGameDao: BoardGameDao

    @Inject
    lateinit var repository: BoardGameCollectionRepository

    @MockK
    lateinit var navController: NavHostController

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltRule.inject()
        mockWebServer.start(8080)
        IdlingRegistry.getInstance().register(okHttp3IdlingResource)
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
        IdlingRegistry.getInstance().unregister(okHttp3IdlingResource)
    }

    @Test
    fun has_homeList_displayed() {
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                var response: MockResponse = MockResponse().setResponseCode(404)
                if (request.path!!.contains("/xmlapi2/collection")) {
                    response = MockResponse()
                        .setResponseCode(200)
                        .setBody(readStringFromFile("Cubenbois.xml"))
                        .setBodyDelay(1, TimeUnit.SECONDS)
                } else if (request.path!!.contains("/xmlapi2/thing")) {
                    response = MockResponse()
                        .setResponseCode(200)
                        .setBody(readStringFromFile("86246.xml"))
                        .setBodyDelay(1, TimeUnit.SECONDS)
                }
                return response
            }
        }

        val userSettingsFlow = flow<UserSettings> { UserSettings() }
        composeTestRule.setContent {
            BogadexTheme {
                NavigationGraph(
                    navController = navController,
                    startDestination = BoardGameCollectionNavigation.ROUTE,
                    repository = repository,
                    userSettingsFlow = userSettingsFlow,
                )
            }
        }
    }

    @Test
    fun has_collectionList_displayed() {
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                var response: MockResponse = MockResponse().setResponseCode(404)
                if (request.path!!.contains("/xmlapi2/collection")) {
                    response = MockResponse()
                        .setResponseCode(200)
                        .setBody(readStringFromFile("Cubenbois.xml"))
                        .setBodyDelay(1, TimeUnit.SECONDS)
                } else if (request.path!!.contains("/xmlapi2/thing")) {
                    response = MockResponse()
                        .setResponseCode(200)
                        .setBody(readStringFromFile("86246.xml"))
                        .setBodyDelay(1, TimeUnit.SECONDS)
                }
                return response
            }
        }

        val userSettingsFlow = flow<UserSettings> { UserSettings() }
        composeTestRule.setContent {
            BogadexTheme {
                NavigationGraph(
                    navController = navController,
                    startDestination = BoardGameCollectionNavigation.ROUTE,
                    repository = repository,
                    userSettingsFlow = userSettingsFlow,
                )
            }
        }
    }

    @Test
    fun has_wishList_displayed() {
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                var response: MockResponse = MockResponse().setResponseCode(404)
                if (request.path!!.contains("/xmlapi2/collection")) {
                    response = MockResponse()
                        .setResponseCode(200)
                        .setBody(readStringFromFile("Cubenbois.xml"))
                        .setBodyDelay(1, TimeUnit.SECONDS)
                } else if (request.path!!.contains("/xmlapi2/thing")) {
                    response = MockResponse()
                        .setResponseCode(200)
                        .setBody(readStringFromFile("86246.xml"))
                        .setBodyDelay(1, TimeUnit.SECONDS)
                }
                return response
            }
        }

        val userSettingsFlow = flow<UserSettings> { UserSettings() }
        composeTestRule.setContent {
            BogadexTheme {
                NavigationGraph(
                    navController = navController,
                    startDestination = BoardGameCollectionNavigation.ROUTE,
                    repository = repository,
                    userSettingsFlow = userSettingsFlow,
                )
            }
        }
    }

    @Test
    fun has_collectionList_displayedOnGrid() {
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                var response: MockResponse = MockResponse().setResponseCode(404)
                if (request.path!!.contains("/xmlapi2/collection")) {
                    response = MockResponse()
                        .setResponseCode(200)
                        .setBody(readStringFromFile("Cubenbois.xml"))
                        .setBodyDelay(1, TimeUnit.SECONDS)
                } else if (request.path!!.contains("/xmlapi2/thing")) {
                    response = MockResponse()
                        .setResponseCode(200)
                        .setBody(readStringFromFile("86246.xml"))
                        .setBodyDelay(1, TimeUnit.SECONDS)
                }
                return response
            }
        }

        val userSettingsFlow = flow<UserSettings> { UserSettings() }
        composeTestRule.setContent {
            BogadexTheme {
                NavigationGraph(
                    navController = navController,
                    startDestination = BoardGameCollectionNavigation.ROUTE,
                    repository = repository,
                    userSettingsFlow = userSettingsFlow,
                )
            }
        }
    }
}
