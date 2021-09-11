package fr.boitakub.bogadex.tests

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import fr.boitakub.bogadex.BogadexApplicationModule
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [BogadexApplicationModule::class]
)
class FakeDataServiceModule : BogadexApplicationModule() {

    override fun baseUrl(): HttpUrl {
        return "http://localhost:8080/".toHttpUrl()
    }

    @Provides
    @Singleton
    fun provideIdlingResource(okHttpClient: OkHttpClient): OkHttp3IdlingResource {
        return OkHttp3IdlingResource.create(
            "okhttp",
            okHttpClient
        )
    }
}
