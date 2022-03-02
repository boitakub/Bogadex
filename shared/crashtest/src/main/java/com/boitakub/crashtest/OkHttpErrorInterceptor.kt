package com.boitakub.crashtest

import okhttp3.Interceptor
import okhttp3.Response

// https://medium.com/@riteshakya037/okhttp-and-the-beauty-of-interceptor-cad4b78af0bf
class OkHttpErrorInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        if (!response.isSuccessful) {
            // Throw error based on response code
            when (response.code) {
                in 401 until 403 -> throw AuthenticationError()
                in 500 until 599 -> throw ServerException()
                else -> {
                    throw UnsuccessfullRequest()
                }
            }
        } else {
            // Is 2xx error but still hold behavior error to handle
            val genericResponse: BaseResponse = parseError(response) ?: return response
            if (!genericResponse.status) {
                Timber.e("Generic Message thrown with message: ${genericResponse.message}")
                throw GenericMessageException(genericResponse.message)
            }
        }
        return response
    }
}
