package fr.boitakub.bogadex.preferences.user

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import fr.boitakub.bogadex.common.UserSettings
import fr.boitakub.bogadex.common.ui.theme.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSettingsRepository @Inject constructor(
    @ApplicationContext val context: Context,
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_settings")

    fun userSettings(): Flow<UserSettings> {
        return combine(
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
    }

    // region BGG Username

    private val bggUserNamePreferencesKey = stringPreferencesKey("bgg_username")
    private val bggUserName: Flow<String> = context.dataStore.data
        .map { preferences ->
            // No type safety.
            preferences[bggUserNamePreferencesKey] ?: "cubenbois"
        }

    suspend fun setBggUserName(bggUserName: String) {
        context.dataStore.edit { settings ->
            settings[bggUserNamePreferencesKey] = bggUserName
        }
    }

    // endregion

    // region ActiveTheme

    private val activeThemePreferencesKey = stringPreferencesKey("key_night_mode")
    private val activeTheme: Flow<String> = context.dataStore.data
        .map { preferences ->
            // No type safety.
            preferences[activeThemePreferencesKey] ?: ""
        }

    suspend fun setActiveTheme(nightMode: String) {
        context.dataStore.edit { settings ->
            settings[activeThemePreferencesKey] = nightMode
        }
    }

    // endregion

    // region Display Previously Owned

    private val displayPreviouslyOwnedPreferencesKey = booleanPreferencesKey("display_previously_owned")
    private val displayPreviouslyOwned: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            // No type safety.
            preferences[displayPreviouslyOwnedPreferencesKey] ?: false
        }

    suspend fun setDisplayPreviouslyOwned(displayPreviouslyOwn: Boolean) {
        context.dataStore.edit { settings ->
            settings[displayPreviouslyOwnedPreferencesKey] = displayPreviouslyOwn
        }
    }

    // endregion
}
