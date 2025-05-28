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
package fr.boitakub.bogadex.tests

import android.app.Application
import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import androidx.test.runner.AndroidJUnitRunner
import coil3.ColorImage
import coil3.ImageLoader
import coil3.test.FakeImageLoaderEngine
import fr.boitakub.bgg.client.BggService
import fr.boitakub.bogadex.BogadexDatabase
import fr.boitakub.bogadex.di.applicationModule
import fr.boitakub.bogadex.di.viewModelModule
import fr.boitakub.bogadex.preferences.user.UserSettingsRepository
import okhttp3.HttpUrl.Companion.toHttpUrl
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit
import kotlin.random.Random

class InstrumentationTestRunner : AndroidJUnitRunner() {

    override fun newApplication(classLoader: ClassLoader?, className: String?, context: Context?): Application =
        super.newApplication(classLoader, TestApplication::class.java.name, context)
}

class TestApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TestApplication)
            workManagerFactory()
            modules(
                listOf(
                    applicationModule,
                    fakeNetworkModule,
                    fakeDatabaseModule,
                    fakePreferencesModule,
                    viewModelModule,
                ),
            )
        }
    }
}

val fakeNetworkModule = module {
    fun baseUrl() = "http://127.0.0.1:8080".toHttpUrl()
    fun imageLoader(context: Context): ImageLoader {
        val engine = FakeImageLoaderEngine.Builder()
            .intercept("https://example.com/image.jpg", ColorImage(Color.Red.toArgb()))
            .intercept({ it is String && it.endsWith("test.png") }, ColorImage(Color.Green.toArgb()))
            .default(ColorImage(Color.Blue.toArgb()))
            .build()
        val imageLoader = ImageLoader.Builder(context)
            .components { add(engine) }
            .build()
        return imageLoader
    }

    singleOf(::baseUrl)
    single<ImageLoader> { imageLoader(get()) }
    single { BggService.getDefaultOkHttpClient(null) }
    single { BggService.getDefaultRetrofitClient(get(), get()) }
    single { (get() as Retrofit).create(BggService::class.java) }
}

val fakeDatabaseModule = module {
    single { Room.inMemoryDatabaseBuilder(get(), BogadexDatabase::class.java).build() }
    single { (get() as BogadexDatabase).boardGameDao() }
    single { (get() as BogadexDatabase).boardGameListDao() }
}

val fakePreferencesModule = module {
    single<DataStore<Preferences>> {
        val random = Random.nextInt()
        PreferenceDataStoreFactory
            .create(
                produceFile = {
                    // creating a new file for every test case and finally
                    // deleting them all
                    get<Context>().preferencesDataStoreFile("test_pref_file-$random")
                },
            )
    }
    single { UserSettingsRepository(get()) }
}
