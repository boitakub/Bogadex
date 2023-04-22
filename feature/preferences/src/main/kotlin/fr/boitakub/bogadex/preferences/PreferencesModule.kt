package fr.boitakub.bogadex.preferences

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.boitakub.bogadex.preferences.user.UserSettingsRepository

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    @Provides
    fun provideUserSetting(userSettingsRepository: UserSettingsRepository) = userSettingsRepository.userSettings()
}
