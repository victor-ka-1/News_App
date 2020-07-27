package com.example.newsapp.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NewsApiClient {
    companion object {
        private const val BASE_URL="https://newsapi.org/v2/"
        private const val apiKeyHeader = "x-api-key"
        private const val apiKey = "1a969e59c42f4412a7a965c3331d4eb8"

        fun getApiService(retrofit: Retrofit): NewsApiService{
            return retrofit.create(NewsApiService::class.java)
        }

        fun getApiClient(okHttpClient: OkHttpClient) :Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun okHttpClient(): OkHttpClient {
            val loggingInterceptor = HttpLoggingInterceptor()
            if (   true/*BuildConfig.DEBUG*/) {
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            } else {
                loggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
            }
            return OkHttpClient.Builder()
                .readTimeout(1200, TimeUnit.SECONDS)
                .connectTimeout(1200, TimeUnit.SECONDS)
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader(
                            apiKeyHeader,
                            apiKey
                        )
                        .build()
                    chain.proceed(request)
                }
                .addInterceptor(loggingInterceptor)
                .build()
        }
    }
}