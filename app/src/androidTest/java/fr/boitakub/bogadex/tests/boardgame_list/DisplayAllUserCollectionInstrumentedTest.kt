package fr.boitakub.bogadex.tests.boardgame_list

import android.content.Intent
import android.util.Log
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.IdlingRegistry
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
import fr.boitakub.bogadex.tests.FileReader
import fr.boitakub.bogadex.tests.OkHttp3IdlingResource
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
    fun has_scenarioList_displayed() {
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                if (request.path!!.contains("/xmlapi2/collection")) {
                    return MockResponse()
                        .setResponseCode(200)
                        .setBody(FileReader.readStringFromFile("Cubenbois.xml"))
                        .setBodyDelay(1, TimeUnit.SECONDS)
                } else if (request.path!!.contains("/xmlapi2/thing")) {
                    return MockResponse()
                        .setResponseCode(200)
                        .setBody(FileReader.readStringFromFile("86246.xml"))
                        .setBodyDelay(1, TimeUnit.SECONDS)
                }
                return MockResponse().setResponseCode(404)
            }
        }

        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        scenario = launchActivity(intent)

        assertDisplayedAtPosition(R.id.recycler_view, 1, R.id.tv_title, "7 Wonders Duel")
    }

    @Test
    fun has_moduleList_displayedOnGrid() {
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                if (request.path!!.contains("/xmlapi2/collection")) {
                    return MockResponse()
                        .setResponseCode(200)
                        .setBody(FileReader.readStringFromFile("Cubenbois.xml"))
                        .setBodyDelay(1, TimeUnit.SECONDS)
                } else if (request.path!!.contains("/xmlapi2/thing")) {
                    return MockResponse()
                        .setResponseCode(200)
                        .setBody(FileReader.readStringFromFile("86246.xml"))
                        .setBodyDelay(1, TimeUnit.SECONDS)
                }
                return MockResponse().setResponseCode(404)
            }
        }

        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        scenario = launchActivity(intent)

        clickOn(R.id.menu_switch_layout)

        assertDisplayedAtPosition(R.id.recycler_view, 1, R.id.tv_title, "7 Wonders Duel")
    }
}
