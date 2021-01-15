package ru.gressor.nasa_picture.data.repo

import okhttp3.Interceptor
import okhttp3.Response
import ru.gressor.nasa_picture.BuildConfig

class ApiKeyInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
        val url = request()
            .url.newBuilder()
            .addQueryParameter("api_key", BuildConfig.NASA_API_KEY)
            .build()
        val newRequest = request().newBuilder()
            .url(url)
            .build()
        proceed(newRequest)
    }
}