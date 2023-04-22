package fr.boitakub.bogadex.preferences.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.boitakub.bogadex.common.UserSettings
import fr.boitakub.bogadex.common.ui.theme.Theme
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class UserSettingsViewModel @Inject constructor(private val userSettingsRepository: UserSettingsRepository) :
    ViewModel() {
    fun updateBggUsername(bggUsername: String) {
        flow<UserSettings> {
            userSettingsRepository.setBggUserName(bggUsername)
        }.launchIn(viewModelScope)
    }

    fun updateFilterPreviouslyOwned(previouslyOwned: Boolean) {
        flow<UserSettings> {
            userSettingsRepository.setDisplayPreviouslyOwned(previouslyOwned)
        }.launchIn(viewModelScope)
    }

    fun updateTheme(theme: Theme) {
        flow<UserSettings> {
            userSettingsRepository.setActiveTheme(theme.value)
        }.launchIn(viewModelScope)
    }

    val uiState: StateFlow<UserSettings> =
        userSettingsRepository.userSettings()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = UserSettings(),
            )
}
