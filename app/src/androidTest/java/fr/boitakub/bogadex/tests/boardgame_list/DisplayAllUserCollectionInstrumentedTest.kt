/*
 * Copyright (c) 2021, Boitakub
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

import android.content.Intent
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.Configuration
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import fr.boitakub.bogadex.MainActivity
import fr.boitakub.bogadex.R
import fr.boitakub.bogadex.boardgame.BoardGameDao
import fr.boitakub.bogadex.boardgame.model.BoardGame
import fr.boitakub.bogadex.tests.OkHttp3IdlingResource
import fr.boitakub.bogadex.tests.tools.FileReader.readStringFromFile
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class DisplayAllUserCollectionInstrumentedTest {

    private val mockWebServer = MockWebServer()

    @Inject
    lateinit var okHttp3IdlingResource: OkHttp3IdlingResource

    @Inject
    lateinit var boardGameDao: BoardGameDao

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    lateinit var scenario: ActivityScenario<MainActivity>

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
    }

    @After
    fun teardown() {
        scenario.close()
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(okHttp3IdlingResource)
    }

    @Test
    fun has_homeList_displayed() {
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                if (request.path!!.contains("/xmlapi2/collection")) {
                    return MockResponse()
                        .setResponseCode(200)
                        .setBody(readStringFromFile("Cubenbois.xml"))
                        .setBodyDelay(1, TimeUnit.SECONDS)
                } else if (request.path!!.contains("/xmlapi2/thing")) {
                    return MockResponse()
                        .setResponseCode(200)
                        .setBody(readStringFromFile("86246.xml"))
                        .setBodyDelay(1, TimeUnit.SECONDS)
                }
                return MockResponse().setResponseCode(404)
            }
        }

        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        scenario = launchActivity(intent)

        onView(withId(R.id.recycler_view))
            .perform(scrollToPosition<RecyclerView.ViewHolder>(0))
        onView(withText("5211")).check(matches(isDisplayed()))
    }

    @Test
    fun has_collectionList_displayed() {
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                if (request.path!!.contains("/xmlapi2/collection")) {
                    return MockResponse()
                        .setResponseCode(200)
                        .setBody(readStringFromFile("Cubenbois.xml"))
                        .setBodyDelay(1, TimeUnit.SECONDS)
                } else if (request.path!!.contains("/xmlapi2/thing")) {
                    return MockResponse()
                        .setResponseCode(200)
                        .setBody(readStringFromFile("86246.xml"))
                        .setBodyDelay(1, TimeUnit.SECONDS)
                }
                return MockResponse().setResponseCode(404)
            }
        }

        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        scenario = launchActivity(intent)

        onView(withContentDescription("Open navigation drawer")).perform(click())
        onView(withId(R.id.display_collection)).perform(click())

        onView(withId(R.id.recycler_view))
            .perform(scrollToPosition<RecyclerView.ViewHolder>(0))
        onView(withText("7 Wonders Duel")).check(matches(isDisplayed()))
    }

    @Test
    fun has_wishList_displayed() {
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                if (request.path!!.contains("/xmlapi2/collection")) {
                    return MockResponse()
                        .setResponseCode(200)
                        .setBody(readStringFromFile("Cubenbois.xml"))
                        .setBodyDelay(1, TimeUnit.SECONDS)
                } else if (request.path!!.contains("/xmlapi2/thing")) {
                    return MockResponse()
                        .setResponseCode(200)
                        .setBody(readStringFromFile("86246.xml"))
                        .setBodyDelay(1, TimeUnit.SECONDS)
                }
                return MockResponse().setResponseCode(404)
            }
        }

        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        scenario = launchActivity(intent)

        onView(withContentDescription("Open navigation drawer")).perform(click())
        onView(withId(R.id.display_wishlist)).perform(click())

        onView(withId(R.id.recycler_view))
            .perform(scrollToPosition<RecyclerView.ViewHolder>(1))
        onView(withText("Anachrony")).check(matches(isDisplayed()))
    }

    @Test
    fun has_collectionList_displayedOnGrid() {
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                if (request.path!!.contains("/xmlapi2/collection")) {
                    return MockResponse()
                        .setResponseCode(200)
                        .setBody(readStringFromFile("Cubenbois.xml"))
                        .setBodyDelay(1, TimeUnit.SECONDS)
                } else if (request.path!!.contains("/xmlapi2/thing")) {
                    return MockResponse()
                        .setResponseCode(200)
                        .setBody(readStringFromFile("86246.xml"))
                        .setBodyDelay(1, TimeUnit.SECONDS)
                }
                return MockResponse().setResponseCode(404)
            }
        }

        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        scenario = launchActivity(intent)

        onView(withId(R.id.menu_switch_layout)).perform(click())

        onView(withId(R.id.recycler_view))
            .perform(scrollToPosition<RecyclerView.ViewHolder>(0))
        onView(withText("7 Wonders Duel")).check(matches(isDisplayed()))
    }

    @Test
    fun has_soloList_displayed() {
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                if (request.path!!.contains("/xmlapi2/collection")) {
                    return MockResponse()
                        .setResponseCode(200)
                        .setBody(readStringFromFile("Cubenbois.xml"))
                        .setBodyDelay(1, TimeUnit.SECONDS)
                } else if (request.path!!.contains("/xmlapi2/thing")) {
                    return MockResponse()
                        .setResponseCode(200)
                        .setBody(readStringFromFile("86246.xml"))
                        .setBodyDelay(1, TimeUnit.SECONDS)
                }
                return MockResponse().setResponseCode(404)
            }
        }

        runBlocking {
            boardGameDao.insertAllBoardGame(
                listOf(
                    BoardGame(
                        bggId = "180263",
                        minPlayer = 1,
                        title = "The 7th Continent"
                    )
                )
            )
        }

        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        scenario = launchActivity(intent)

        onView(withContentDescription("Open navigation drawer")).perform(click())
        onView(withId(R.id.display_solo)).perform(click())

        onView(withId(R.id.recycler_view))
            .perform(scrollToPosition<RecyclerView.ViewHolder>(0))
        onView(withText("The 7th Continent")).check(matches(isDisplayed()))
    }
}
