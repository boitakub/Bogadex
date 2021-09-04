package fr.boitakub.bogadex

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.boitakub.bgg_api_client.BggService
import fr.boitakub.bogadex.boardgame.BggServiceHelper
import fr.boitakub.bogadex.boardgame.BggServiceHelperImpl
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class BogadexApplicationModule {

    private fun baseUrl() = "https://www.boardgamegeek.com/".toHttpUrl()

    @Provides
    fun provideBaseUrl(): HttpUrl = baseUrl()

    @Provides
    fun provideExampleBggAccount(): String = "Cubenbois"

    @Provides
    @Singleton
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        BggService.getDefaultOkHttpClient(null)
    } else BggService.getDefaultOkHttpClient(null)

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL: HttpUrl): Retrofit =
        BggService.getDefaultRetrofitClient(BASE_URL, okHttpClient)

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) = retrofit.create(BggService::class.java)

    @Provides
    @Singleton
    fun provideApiHelper(apiHelper: BggServiceHelperImpl): BggServiceHelper = apiHelper
}
