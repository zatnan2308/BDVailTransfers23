package com.example.bdvailtransfers2.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Простейший "модуль" сети без DI-фреймворков.
 * Даёт готовый экземпляр BDVailApiService.
 */
object NetworkModule {

    // Базовый URL сайта (с обязательным / в конце)
    private const val BASE_URL = "https://www.bdvail.com/"

    // Логирование запросов (на проде можно понизить уровень)
    private val loggingInterceptor: HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            // BASIC — только старт/строка запроса/код ответа,
            // BODY — полный запрос/ответ (осторожно с логами)
            level = HttpLoggingInterceptor.Level.BASIC
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
