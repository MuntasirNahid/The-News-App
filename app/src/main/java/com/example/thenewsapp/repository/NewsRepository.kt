package com.example.thenewsapp.repository

import com.example.thenewsapp.api.RetrofitInstance
import com.example.thenewsapp.db.ArticleDatabase
import com.example.thenewsapp.models.Article

/**
 * Repository class that acts as a single source of truth for all news data.
 * Handles both remote (API) and local (Database) data operations.
 *
 * Features:
 * - Fetches headlines from remote API
 * - Performs news searches
 * - Manages local database operations for favourites
 * - Provides clean API for data operations
 *
 * @property db ArticleDatabase instance for local data persistence
 */
class NewsRepository(val db: ArticleDatabase) {

    /**
     * Fetches headlines from the remote API.
     *
     * @param countryCode Two-letter country code (e.g., "us" for United States)
     * @param pageNumber Page number for pagination
     * @return Response<NewsResponse> API response containing headlines
     */
    suspend fun getHeadlines(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getHeadlines(countryCode, pageNumber)

    /**
     * Searches for news articles using the remote API.
     *
     * @param searchQuery Search term to query news articles
     * @param pageNumber Page number for pagination
     * @return Response<NewsResponse> API response containing search results
     */
    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    /**
     * Inserts or updates an article in the local database.
     * Uses upsert operation: updates if exists, inserts if not.
     *
     * @param article Article to be saved
     * @return Long ID of the inserted/updated article
     */
    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    /**
     * Retrieves all saved articles from the local database.
     *
     * @return LiveData<List<Article>> Observable list of saved articles
     */
    fun getFavouriteNews() = db.getArticleDao().getAllArticles()

    /**
     * Deletes an article from the local database.
     *
     * @param article Article to be deleted
     * @return Unit
     */
    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
}