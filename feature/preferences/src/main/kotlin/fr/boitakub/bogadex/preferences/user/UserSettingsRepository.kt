/*
 * Copyright (c) 2023-2025, Boitakub
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
package fr.boitakub.bogadex.preferences.user

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import fr.boitakub.bogadex.common.UserSettings
import fr.boitakub.bogadex.common.ui.theme.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class UserSettingsRepository(private val userSettingsDataStore: DataStore<Preferences>) {
    fun userSettings(): Flow<UserSettings> = combine(
        bggUserName,
        activeTheme,
        displayPreviouslyOwned,
    ) { bggUserName, activeTheme, displayPreviouslyOwned ->
        UserSettings(
            bggUserName = bggUserName,
            activeTheme = Theme.getValueOf(activeTheme),
            displayPreviouslyOwned = displayPreviouslyOwned,
        )
    }

    // region BGG Username

    private val bggUserNamePreferencesKey = stringPreferencesKey("bgg_username")
    private val bggUserName: Flow<String> = userSettingsDataStore.data
        .map { preferences ->
            // No type safety.
            preferences[bggUserNamePreferencesKey] ?: "cubenbois"
        }

    suspend fun setBggUserName(bggUserName: String) {
        userSettingsDataStore.edit { settings ->
            settings[bggUserNamePreferencesKey] = bggUserName
        }
    }

    // endregion

    // region ActiveTheme

    private val activeThemePreferencesKey = stringPreferencesKey("key_night_mode")
    private val activeTheme: Flow<String> = userSettingsDataStore.data
        .map { preferences ->
            // No type safety.
            preferences[activeThemePreferencesKey] ?: ""
        }

    suspend fun setActiveTheme(nightMode: String) {
        userSettingsDataStore.edit { settings ->
            settings[activeThemePreferencesKey] = nightMode
        }
    }

    // endregion

    // region Display Previously Owned

    private val displayPreviouslyOwnedPreferencesKey = booleanPreferencesKey("display_previously_owned")
    private val displayPreviouslyOwned: Flow<Boolean> = userSettingsDataStore.data
        .map { preferences ->
            // No type safety.
            preferences[displayPreviouslyOwnedPreferencesKey] ?: false
        }

    suspend fun setDisplayPreviouslyOwned(displayPreviouslyOwn: Boolean) {
        userSettingsDataStore.edit { settings ->
            settings[displayPreviouslyOwnedPreferencesKey] = displayPreviouslyOwn
        }
    }

    // endregion
}
