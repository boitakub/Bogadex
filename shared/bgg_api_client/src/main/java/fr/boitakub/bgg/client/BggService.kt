/*
 * Copyright (c) 2021-2025, Boitakub
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
package fr.boitakub.bgg.client

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import nl.adaptivity.xmlutil.serialization.XML
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface BggService {

    @GET("/xmlapi2/thing?versions=1&stats=1")
    @Headers("Cache-Control: max-age=1800")
    suspend fun boardGame(@Query("id") boardGameId: String?): BggGameInfoResult?

    @GET("/xmlapi2/collection?stats=1")
    @Headers("Cache-Control: max-age=1800")
    suspend fun userCollection(@Query("username") bggUserId: String?): UserCollection

    companion object {
        private const val DEFAULT_API_CONNECTION_TIMEOUT_IN_SECONDS: Long = 30
        private const val DEFAULT_API_READ_WRITE_TIMEOUT_IN_SECONDS: Long = 30

        fun getDefaultRetrofitClient(baseUrl: HttpUrl, okHttpClient: OkHttpClient): Retrofit {
            val contentType = "text/xml".toMediaType()
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(XML.asConverterFactory(contentType))
                .build()
        }

        fun getDefaultOkHttpClient(networkInterceptors: List<Interceptor>?): OkHttpClient {
            val okHttpClientBuild = OkHttpClient.Builder()
                .connectTimeout(
                    DEFAULT_API_CONNECTION_TIMEOUT_IN_SECONDS,
                    TimeUnit.SECONDS,
                )
                .readTimeout(
                    DEFAULT_API_READ_WRITE_TIMEOUT_IN_SECONDS,
                    TimeUnit.SECONDS,
                )
                .writeTimeout(
                    DEFAULT_API_READ_WRITE_TIMEOUT_IN_SECONDS,
                    TimeUnit.SECONDS,
                )
                .addInterceptor(XmlSanitizingInterceptor())
                .addInterceptor(CollectionRequestQueuedInterceptor())
            if (networkInterceptors != null) {
                for (networkInterceptor in networkInterceptors) {
                    okHttpClientBuild.addNetworkInterceptor(networkInterceptor)
                }
            }
            okHttpClientBuild.addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                },
            )
            return okHttpClientBuild.build()
        }
    }
}

class XmlSanitizingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())

        val originalBody = originalResponse.body?.string() ?: ""
        val sanitizedBody = sanitizeXml(originalBody)

        return originalResponse.newBuilder()
            .body(sanitizedBody.toResponseBody("application/xml".toMediaType()))
            .build()
    }

    fun sanitizeXml(xml: String): String {
        val attrRegex = Regex("""(=\s*")(.*?)(?=")""")
        return attrRegex.replace(xml) { matchResult ->
            val fullAttr = matchResult.groupValues[2]
            val escapedAttr = fullAttr.replace("&", "&amp;")
            matchResult.groupValues[1] + escapedAttr
        }
    }
}
