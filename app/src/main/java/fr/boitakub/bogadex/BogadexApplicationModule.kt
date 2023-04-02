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

import android.content.Context
import androidx.preference.PreferenceManager
import coil.ImageLoader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.boitakub.bogadex.common.UserSettings
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl

@Module
@InstallIn(SingletonComponent::class)
open class BogadexApplicationModule {

    open fun baseUrl() = "https://www.boardgamegeek.com/".toHttpUrl()

    open fun imageLoader(context: Context): ImageLoader {
        return ImageLoader.Builder(context).build()
    }

    @Provides
    open fun provideExampleBggAccount(@ApplicationContext context: Context): String {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("bgg_username", "cubenbois").orEmpty()
    }

    @Provides
    open fun provideUserSettings(@ApplicationContext context: Context): UserSettings {
        return UserSettings(
            PreferenceManager.getDefaultSharedPreferences(context).getBoolean("display_previously_own", true)
        )
    }

    @Provides
    fun provideBaseUrl(): HttpUrl = baseUrl()

    @Provides
    fun provideImageLoader(@ApplicationContext context: Context): ImageLoader = imageLoader(context)
}
