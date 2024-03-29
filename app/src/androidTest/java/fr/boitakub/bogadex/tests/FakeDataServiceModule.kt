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
package fr.boitakub.bogadex.tests

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import coil.Coil
import coil.ImageLoader
import coil.test.FakeImageLoaderEngine
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import fr.boitakub.bogadex.BogadexApplicationModule
import fr.boitakub.bogadex.preferences.PreferencesModule
import fr.boitakub.bogadex.preferences.user.UserSettingsRepository
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import javax.inject.Singleton
import kotlin.random.Random

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [BogadexApplicationModule::class, PreferencesModule::class]
)
class FakeDataServiceModule : BogadexApplicationModule() {

    override fun baseUrl(): HttpUrl {
        return "http://127.0.0.1:8080".toHttpUrl()
    }

    override fun imageLoader(context: Context): ImageLoader {
        val engine = FakeImageLoaderEngine.Builder()
            .intercept("https://www.example.com/image.jpg", ColorDrawable(Color.RED))
            .intercept({ it is String && it.endsWith("test.png") }, ColorDrawable(Color.GREEN))
            .default(ColorDrawable(Color.BLUE))
            .build()
        val imageLoader = ImageLoader.Builder(context)
            .components { add(engine) }
            .build()
        Coil.setImageLoader(imageLoader)
        return imageLoader
    }

    @Singleton
    @Provides
    fun provideFakePreferences(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> {
        val random = Random.nextInt()
        return PreferenceDataStoreFactory
            .create(
                produceFile = {
                    // creating a new file for every test case and finally
                    // deleting them all
                    context.preferencesDataStoreFile("test_pref_file-$random")
                }
            )
    }

    @Provides
    fun provideUserSetting(userSettingsRepository: UserSettingsRepository) = userSettingsRepository.userSettings()
}
