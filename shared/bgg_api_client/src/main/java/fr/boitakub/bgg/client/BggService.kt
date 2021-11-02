/*
 * Copyright 2021 Boitakub
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.boitakub.bgg.client

import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface BggService {
    @GET("/xmlapi2/search?type=boardgame")
    @Headers("Cache-Control: max-age=1800")
    suspend fun boardGames(@Query("query") query: String?): BggSearchResult?

    @GET("/xmlapi2/thing?versions=1&stats=1")
    @Headers("Cache-Control: max-age=1800")
    suspend fun boardGame(@Query("id") boardGameId: String?): BggGameInfoResult?

    @GET("/xmlapi2/collection?stats=1")
    @Headers("Cache-Control: max-age=1800")
    suspend fun userCollection(@Query("username") bggUserId: String?): UserCollection

    companion object {
        private const val DEFAULT_API_CONNECTION_TIMEOUT_IN_SECONDS: Long = 30
        private const val DEFAULT_API_READ_WRITE_TIMEOUT_IN_SECONDS: Long = 30

        fun getDefaultRetrofitClient(
            baseUrl: HttpUrl,
            okHttpClient: OkHttpClient
        ): Retrofit {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(TikXmlConverterFactory.create(buildXmlParser()))
                .build()
        }

        fun getDefaultOkHttpClient(
            networkInterceptors: List<Interceptor>?
        ): OkHttpClient {
            val okHttpClientBuild = OkHttpClient.Builder()
                .connectTimeout(
                    DEFAULT_API_CONNECTION_TIMEOUT_IN_SECONDS,
                    TimeUnit.SECONDS
                )
                .readTimeout(
                    DEFAULT_API_READ_WRITE_TIMEOUT_IN_SECONDS,
                    TimeUnit.SECONDS
                )
                .writeTimeout(
                    DEFAULT_API_READ_WRITE_TIMEOUT_IN_SECONDS,
                    TimeUnit.SECONDS
                )
                .addInterceptor(CollectionRequestQueuedInterceptor())
            if (networkInterceptors != null) {
                for (networkInterceptor in networkInterceptors) {
                    okHttpClientBuild.addNetworkInterceptor(networkInterceptor)
                }
            }
            return okHttpClientBuild.build()
        }

        private fun buildXmlParser(): TikXml {
            return TikXml.Builder()
                .exceptionOnUnreadXml(false)
                .build()
        }
    }
}
