package com.example.thenewsapp.api

import com.example.thenewsapp.models.NewsResponse
import com.example.thenewsapp.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit interface for the News API.
 * Defines endpoints for fetching news data from the remote server.
 *
 * Base URL: https://newsapi.org/
 * API Documentation: https://newsapi.org/docs
 *
 * Features:
 * - Fetch top headlines by country
 * - Search articles across all sources
 * - Pagination support
 * - Suspend functions for coroutine support
 */
interface NewsAPI {

    /**
     * Fetches top headlines for a specific country.
     * Endpoint: /v2/top-headlines
     *
     * @param countryCode Two-letter ISO 3166-1 country code (default: "us")
     * @param pageNumber Page number for pagination (default: 1)
     * @param apiKey API key for authentication (default: from Constants)
     * @return Response<NewsResponse> Wrapped response containing list of articles
     *
     * Example: /v2/top-headlines?country=us&page=1&apiKey=YOUR_API_KEY
     */
    @GET("/v2/top-headlines")
    suspend fun getHeadlines(
        @Query("country")
        countryCode: String = "us",
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = Constants.API_KEY
    ): Response<NewsResponse>

    /**
     * Searches for articles across all sources.
     * Endpoint: /v2/everything
     *
     * @param searchQuery Search query string
     * @param pageNumber Page number for pagination (default: 1)
     * @param apiKey API key for authentication (default: from Constants)
     * @return Response<NewsResponse> Wrapped response containing list of articles
     *
     * Example: /v2/everything?q=bitcoin&page=1&apiKey=YOUR_API_KEY
     *
     * Note: This endpoint searches through all available articles,
     * not just headlines.
     */
    @GET("/v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = Constants.API_KEY
    ): Response<NewsResponse>
}