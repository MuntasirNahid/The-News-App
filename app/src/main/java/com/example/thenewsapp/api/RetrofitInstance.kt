package com.example.thenewsapp.api

import com.example.thenewsapp.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton class for creating and maintaining a single Retrofit instance.
 * Handles the setup and configuration of Retrofit with OkHttp client for API calls.
 *
 * Features:
 * - Lazy initialization of Retrofit instance
 * - HTTP request/response logging
 * - GSON conversion for JSON parsing
 * - Single instance of API interface
 */
class RetrofitInstance {
    companion object {
        /**
         * Lazy-initialized Retrofit instance.
         * Configuration includes:
         * - HTTP logging for debugging
         * - OkHttp client setup
         * - GSON converter for JSON handling
         * - Base URL from Constants
         */
        private val retrofit by lazy {
            // Set up logging interceptor for debugging
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            
            // Configure OkHttp client with logging
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            // Build Retrofit instance
            Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        /**
         * Lazy-initialized API interface instance.
         * Creates an implementation of the NewsAPI interface using Retrofit.
         *
         * @see NewsAPI for available API endpoints
         */
        val api by lazy {
            retrofit.create(NewsAPI::class.java)
        }
    }
}