package com.mohit.moviedbmachine.data.remote

import com.mohit.moviedbmachine.Constants
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader(
                "Authorization","Bearer ${Constants.ACCESS_TOKEN}"
            )
            .addHeader("accept", "application/json")
            .build()
        return chain.proceed(request)
    }
}