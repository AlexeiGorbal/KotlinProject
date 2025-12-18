package org.example.project.web

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val newUrl = chain.request().url
            .newBuilder()
            .addQueryParameter("key", "eb77360fd72443f9811203156240203")
            .build()
        val newRequest = chain.request()
            .newBuilder()
            .url(newUrl)
            .build()
        return chain.proceed(newRequest)
    }
}