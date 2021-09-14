package fr.boitakub.bogadex

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl

@Module
@InstallIn(SingletonComponent::class)
open class BogadexApplicationModule {

    open fun baseUrl() = "https://www.boardgamegeek.com/".toHttpUrl()

    @Provides
    fun provideBaseUrl(): HttpUrl = baseUrl()

    @Provides
    fun provideExampleBggAccount(): String = "Cubenbois"
}
