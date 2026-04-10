package com.example.myapplication.data.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkModule {
    // IMPORTANT: If using a physical device, replace 10.0.2.2 with your computer's local IP (e.g., 192.168.31.131)
    // Make sure your phone and computer are on the same Wi-Fi network.
    private const val BASE_URL = " https://nonatomically-necessitous-harley.ngrok-free.dev/api/v1/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(Interceptor { chain ->
            val requestBuilder = chain.request().newBuilder()
                .addHeader("Accept", "application/json")
            
            TokenManager.token?.let {
                requestBuilder.addHeader("Authorization", "Bearer $it")
            }
            
            chain.proceed(requestBuilder.build())
        })
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    val apiService: VitalityApiService = retrofit.create(VitalityApiService::class.java)
}
