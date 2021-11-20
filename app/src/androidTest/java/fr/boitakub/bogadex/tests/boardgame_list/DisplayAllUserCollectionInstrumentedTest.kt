/*
 * Copyright 2021 Boitakub
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.boitakub.bogadex.tests.boardgame_list

import android.content.Intent
import android.util.Log
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.Configuration
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import com.adevinta.android.barista.assertion.BaristaListAssertions.assertDisplayedAtPosition
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import fr.boitakub.bogadex.MainActivity
import fr.boitakub.bogadex.R
import fr.boitakub.bogadex.tests.OkHttp3IdlingResource
import fr.boitakub.bogadex.tests.tools.FileReader.readStringFromFile
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
                        .setBody(fr.boitakub.bogadex.tests.tools.FileReader.readStringFromFile("86246.xml"))
                        .setBodyDelay(1, TimeUnit.SECONDS)
                }
                return MockResponse().setResponseCode(404)
            }
        }

        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        scenario = launchActivity(intent)

        onView(withId(R.id.recycler_view))
        assertDisplayedAtPosition(R.id.recycler_view, 0, R.id.tv_title, "5211")
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
        clickOn(R.id.display_collection)

        onView(withId(R.id.recycler_view))
        assertDisplayedAtPosition(R.id.recycler_view, 0, R.id.tv_title, "7 Wonders Duel")
    }

    @Test
    fun has_wishLit_displayed() {
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
        clickOn(R.id.display_wishlist)

        onView(withId(R.id.recycler_view))
        assertDisplayedAtPosition(R.id.recycler_view, 1, R.id.tv_title, "Anachrony")
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

        clickOn(R.id.menu_switch_layout)

        onView(withId(R.id.recycler_view))
        assertDisplayedAtPosition(R.id.recycler_view, 1, R.id.tv_title, "7 Wonders Duel")
    }
}
