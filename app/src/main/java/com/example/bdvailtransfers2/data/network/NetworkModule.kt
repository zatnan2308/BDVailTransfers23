package com.example.bdvailtransfers2.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Простейший модуль сети без DI-фреймворков.
 * Даёт готовый экземпляр BDVailApiService.
 */
object NetworkModule {

    /**
     * Базовый URL сайта (обязательно с / в конце).
     *
     * Сейчас тестовый сайт:
     *   https://bdvail.alexeykachan.com/
     *
     * Когда будешь заливать в прод, можно поменять обратно на:
     *   https://www.bdvail.com/
     */
    private const val BASE_URL = "https://bdvail.alexeykachan.com/"

    private val loggingInterceptor: HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            // В проде можно поставить BASIC или NONE
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: BDVailApiService by lazy {
        retrofit.create(BDVailApiService::class.java)
    }
}
