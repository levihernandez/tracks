package com.shipping.tracker.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import com.shipping.tracker.BuildConfig

object ApiClient {
    private var baseUrl = BuildConfig.API_BASE_URL

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    fun getCurrentUrl(): String = baseUrl

    fun updateBaseUrl(newUrl: String) {
        // Ensure URL ends with trailing slash
        baseUrl = if (newUrl.endsWith("/")) newUrl else "$newUrl/"
        _shippingService = null
    }

    private var _shippingService: ShippingService? = null
    val shippingService: ShippingService
        get() {
            if (_shippingService == null) {
                _shippingService = createShippingService()
            }
            return _shippingService!!
        }

    private fun createShippingService(): ShippingService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ShippingService::class.java)
    }
}